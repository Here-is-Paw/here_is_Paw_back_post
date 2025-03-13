package com.ll.hereispaw.domain_msa.post.missing.service;

import com.ll.hereispaw.domain_msa.post.find.entity.Finding;
import com.ll.hereispaw.domain_msa.post.missing.dto.request.MissingCreateRequest;
import com.ll.hereispaw.domain_msa.post.missing.dto.request.MissingDoneRequest;
import com.ll.hereispaw.domain_msa.post.missing.dto.request.MissingPatchRequest;
import com.ll.hereispaw.domain_msa.post.missing.dto.response.MissingListResponse;
import com.ll.hereispaw.domain_msa.post.missing.dto.response.MissingResponse;
import com.ll.hereispaw.domain_msa.post.missing.entity.Missing;
import com.ll.hereispaw.domain_msa.post.missing.repository.MissingRepository;
import com.ll.hereispaw.global_msa.enums.PostMethode;
import com.ll.hereispaw.global_msa.enums.PostState;
import com.ll.hereispaw.global_msa.enums.Topics;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import com.ll.hereispaw.global_msa.kafka.dto.CreatePostEventDto;
import com.ll.hereispaw.global_msa.kafka.dto.DogFaceRequest;
import com.ll.hereispaw.global_msa.member.dto.MemberDto;
import com.ll.hereispaw.standard.Ut_msa.GeoUt;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Tag(name = " 실종 신고 API", description = "Missing")
public class MissingService {
    private static String POST_TYPE = "missing";
    private final MissingRepository missingRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${custom.bucket.name}")
    private String bucketName;
    @Value("${custom.bucket.region}")
    private String region;
    @Value("${custom.bucket.missing}")
    private String dirName;

    private final S3Client s3Client;


    @Transactional
    public MissingResponse write(MemberDto author, MissingCreateRequest request) {
        int state = PostState.OPEN.getCode(); // 0 완료

        String pathUrl = request.hasfile() ? s3Upload(request.getFile()) : request.getPathUrl();

        Point point = GeoUt.wktToPoint(request.getGeo());
        System.out.println("geo: " + request.getGeo());

        Missing missing = missingRepository.save(
                Missing.builder()
                        .name(request.getName())
                        .breed(request.getBreed())
                        .geo(point)
                        .location(request.getLocation())
                        .pathUrl(pathUrl)
                        .color(request.getColor())
                        .serialNumber(request.getSerialNumber())
                        .gender(request.getGender())
                        .neutered(request.getNeutered())
                        .age(request.getAge())
                        .memberId(author.getId())
                        .nickname(author.getNickname())
                        .etc(request.getEtc())
                        .lostDate(request.getLostDate())
                        // 상태 0: 발견(OPEN), 1: 보호(IN_CARE), 2: 완료(DONE)
                        .state(PostState.OPEN.getCode())
                        .reward(request.getReward())
                        .build()
        );

        DogFaceRequest dogFaceRequest = DogFaceRequest.builder()
                .type("save")
                .image(missing.getPathUrl())
                .postType(POST_TYPE)
                .postId(missing.getId())
                .postMemberId(missing.getMemberId())
                .build();
        kafkaTemplate.send(Topics.DOG_FACE.getTopicName(), dogFaceRequest);

        kafkaTemplate.send(Topics.SEARCH.getTopicName(),
                new CreatePostEventDto(missing, PostMethode.CREATE.getCode()));

        return new MissingResponse(missing);
    }

    // 전체 조회 페이지 적용
    public Page<MissingListResponse> list(Pageable pageable) {
        // 기존 pageable에 정렬 조건을 추가합니다
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "modifiedDate")
        );

        Page<Missing> missingPage = missingRepository.findByStateNot(2, sortedPageable);

        return missingPage.map(MissingListResponse::new);
    }

    public List<MissingListResponse> radiusList(Double lat, Double lng, Double radius, String keyword, String category) {
        List<Missing> missingList = switch (category) {
            case "품종" -> missingRepository.findWithinRadiusWithKeywordSearchBreed(lat, lng, radius, keyword);
            case "지역" -> missingRepository.findWithinRadiusWithKeywordSearchLocation(lat, lng, radius, keyword);
            default -> missingRepository.findWithinRadiusWithKeyword(lat, lng, radius, keyword);
        };

        return Objects.requireNonNull(missingList).stream()
                .map(MissingListResponse::new)
                .toList();
    }

    public MissingResponse findById(Long missingId) {
        Missing missing = missingRepository.findById(missingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        return new MissingResponse(missing);
    }

    @Transactional
    public MissingResponse update(
            MemberDto author,
            MissingPatchRequest request,
            Long missingId
    ) {
        Missing missing = missingRepository.findById(missingId).orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        if (!author.getId().equals(missing.getMemberId())) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }

        String pathUrl;

        if (request.hasFile()) {
            s3Delete(missing.getPathUrl());
            pathUrl = s3Upload(request.getFile());
        }else {
            pathUrl = missing.getPathUrl();
        }

        missing.setName(request.getName());
        missing.setBreed(request.getBreed());
        missing.setGeo(GeoUt.wktToPoint(request.getGeo()));
        missing.setLocation(request.getLocation());
        missing.setPathUrl(pathUrl);

        missing.setColor(request.getColor());
        missing.setSerialNumber(request.getSerialNumber());
        missing.setGender(request.getGender());
        missing.setNeutered(request.getNeutered());
        missing.setAge(request.getAge());

        missing.setLostDate(request.getLostDate());
        missing.setEtc(request.getEtc());
        missing.setState(PostState.OPEN.getCode());
        missing.setReward(request.getReward());

        missingRepository.save(missing);

        kafkaTemplate.send(Topics.SEARCH.getTopicName(),
                new CreatePostEventDto(missing, PostMethode.PATH.getCode()));

        return new MissingResponse(missing);
    }

    @Transactional
    public void delete(MemberDto author, Long missingId) {
        Missing missing = missingRepository.findById(missingId).orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        if (!author.getId().equals(missing.getMemberId())) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }

        s3Delete(missing.getPathUrl());
        missingRepository.delete(missing);

        kafkaTemplate.send(Topics.SEARCH.getTopicName(),
                new CreatePostEventDto(missing, PostMethode.DELETE.getCode()));
    }

    @Transactional
    public MissingResponse done(MemberDto login, Long postId, MissingDoneRequest missingDoneRequest) {
        Missing missing = missingRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        if (!login.getId().equals(missing.getMemberId())) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }

        missing.setState(missingDoneRequest.getState());

        Missing newMissing = missingRepository.save(missing);

        MissingResponse missingResponse = new MissingResponse(newMissing);

        kafkaTemplate.send(Topics.SEARCH.getTopicName(),
                new CreatePostEventDto(missing, PostMethode.DELETE.getCode()));

        return missingResponse;
    }

    // s3 매서드
    public String s3Upload(
            MultipartFile file) {

        String filename = getUuidFilename(file);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(dirName + "/" + filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            //return getS3FileUrl(filename)
//            missing.setPathUrl(getS3FileUrl(filename));

        } catch (IOException e) {
            return new CustomException(ErrorCode.S3_UPLOAD_ERROR).toString();
        }

        return getS3FileUrl(filename);
    }

    public void s3Delete(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(dirName + "/" + fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    private String getUuidFilename(MultipartFile file) {
        // ContentType으로부터 확장자 추출
        String contentType = file.getContentType();
        String extension = switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> "jpg";  // 기본값 설정
        };

        // UUID 파일명 생성
        return UUID.randomUUID().toString() + "." + extension;
    }

    public String getS3FileUrl(String fileName) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + dirName + "/" + fileName;
    }

    public String getFileNameFromS3Url(String s3Url) {
        return s3Url.substring(s3Url.lastIndexOf('/') + 1);
    }
}

