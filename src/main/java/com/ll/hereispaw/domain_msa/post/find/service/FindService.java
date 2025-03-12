package com.ll.hereispaw.domain_msa.post.find.service;

import com.ll.hereispaw.domain_msa.post.find.dto.request.FindCreateRequest;
import com.ll.hereispaw.domain_msa.post.find.dto.request.FindPatchRequest;
import com.ll.hereispaw.domain_msa.post.find.dto.response.FindListResponse;
import com.ll.hereispaw.domain_msa.post.find.dto.response.FindResponse;
import com.ll.hereispaw.domain_msa.post.find.entity.Finding;
import com.ll.hereispaw.domain_msa.post.find.repository.FindRepository;
import com.ll.hereispaw.global_msa.enums.PostMethode;
import com.ll.hereispaw.global_msa.enums.PostState;
import com.ll.hereispaw.global_msa.enums.Topics;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import com.ll.hereispaw.global_msa.globalDto.GlobalResponse;
import com.ll.hereispaw.global_msa.kafka.dto.CreatePostEventDto;
import com.ll.hereispaw.global_msa.kafka.dto.DogFaceRequest;
import com.ll.hereispaw.global_msa.member.dto.MemberDto;
import com.ll.hereispaw.standard.Ut_msa.GeoUt;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FindService {
    private static String POST_TYPE = "finding";
    private final FindRepository findRepository;
    //카프카 발행자 템플릿
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${custom.bucket.name}")
    private String bucketName;
    @Value("${custom.bucket.region}")
    private String region;
    @Value("${custom.bucket.finding}")
    private String dirName;

    private final S3Client s3Client;


    @Transactional
    public FindResponse write(MemberDto author, FindCreateRequest request, MultipartFile file) {

        System.out.println(request.getDetailAddr());

        String pathUrl = s3Upload(file);

        Point point = GeoUt.createPoint(request.getX(), request.getY());

        Finding finding = Finding.builder()
                .name(request.getName())
                .breed(request.getBreed())
                .geo(point)
                .location(request.getLocation())
                .detailAddr(request.getDetailAddr())
                .pathUrl(pathUrl)
                .color(request.getColor())
                .serialNumber(request.getSerialNumber())
                .gender(request.getGender())
                .neutered(request.getNeutered())
                .age(request.getAge())
                .memberId(author.getId())
                .nickname(author.getNickname())
                .etc(request.getEtc())
                .findDate(request.getFindDate())
                // 상태 0: 발견(OPEN), 1: 보호(IN_CARE), 2: 완료(DONE)
                .state(PostState.OPEN.getCode())
                .title(request.getTitle())
                .situation(request.getSituation())
                .shelterId(request.getShelter_id())
                .build();

        Finding savedPost = findRepository.save(finding);

        //카프카 메시지 발행
//        DogFaceRequest dogFaceRequest = DogFaceRequest.builder()
//                .type("save")
//                .image(savedPost.getPathUrl())
//                .postType(POST_TYPE)
//                .postId(savedPost.getId())
//                .postMemberId(savedPost.getMemberId())
//                .build();
//        kafkaTemplate.send(Topics.DOG_FACE.getTopicName(), dogFaceRequest);
//
//        kafkaTemplate.send(Topics.SEARCH.getTopicName(),
//                new CreatePostEventDto(savedPost, PostMethode.CREATE.getCode()));

        return new FindResponse(savedPost);
    }

    // 전체 조회 페이지 적용
    public Page<FindListResponse> list(Pageable pageable) {
        // 기존 pageable에 정렬 조건을 추가합니다
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "modifiedDate")
        );

        Page<Finding> findingPage = findRepository.findAll(sortedPageable);

        return findingPage.map(FindListResponse::new);
    }

    public List<FindListResponse> radiusList(Double lat, Double lng, Double radius, String keyword, String category) {
        List<Finding> findingList = switch (category) {
            case "품종" -> findRepository.findWithinRadiusWithKeywordSearchBreed(lat, lng, radius, keyword);
            case "지역" -> findRepository.findWithinRadiusWithKeywordSearchLocation(lat, lng, radius, keyword);
            default -> findRepository.findWithinRadiusWithKeyword(lat, lng, radius, keyword);
        };


        return findingList.stream()
                .map(FindListResponse::new)
                .toList();
    }

    public FindResponse findById(Long postId) {

        Finding finding = findRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.FINDING_NOT_FOUND));

        return new FindResponse(finding);
    }

    @Transactional
    public FindResponse update(
            MemberDto author,
            FindPatchRequest request,
            Long findingId
    ) {
        // 상태 0: 발견, 1: 보호, 2: 완료
        int state = PostState.OPEN.getCode();

        double x = request.getX();
        double y = request.getY();

        // Point 객체 생성
        Point geo = GeoUt.createPoint(x, y);

        Finding finding = findRepository.findById(findingId)
                .orElseThrow(() -> new CustomException(ErrorCode.FINDING_NOT_FOUND));

        String pathUrl = "";

        if (request.hasFile()) {
            s3Delete(finding);
            pathUrl = s3Upload(request.getFile());
        } else {
            pathUrl = request.getPathUrl();
        }

        finding.setBreed(request.getBreed());
        finding.setGeo(geo);
        finding.setLocation(request.getLocation());
        finding.setDetailAddr(request.getDetailAddr());
        finding.setPathUrl(pathUrl);

        finding.setName(request.getName());
        finding.setColor(request.getColor());
        finding.setSerialNumber(request.getSerialNumber());
        finding.setGender(request.getGender());
        finding.setNeutered(request.getNeutered());
        finding.setAge(request.getAge());

        finding.setMemberId(author.getId());
        finding.setNickname(author.getNickname());
        finding.setEtc(request.getEtc());
        finding.setState(state);
        finding.setFindDate(request.getFindDate());

        finding.setTitle(request.getTitle());
        finding.setSituation(request.getSituation());
        finding.setShelterId(request.getShelter_id());

        findRepository.save(finding);

        kafkaTemplate.send(Topics.SEARCH.getTopicName(),
                new CreatePostEventDto(finding, PostMethode.PATH.getCode()));
        return new FindResponse(finding);
    }

    // 발견 신고 삭제
    @Transactional
    public void delete(MemberDto author, Long postId) {
        Finding finding = findRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        if (!author.getId().equals(finding.getMemberId())) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }

        s3Delete(finding);

        findRepository.delete(finding);

        kafkaTemplate.send(Topics.SEARCH.getTopicName(),
                new CreatePostEventDto(finding, PostMethode.DELETE.getCode()));
    }

    // 일주일 이전에 작성된 게시글 삭제
    @Transactional
    public void findExpiredPosts() {
        // 현재 날짜에서 7일 뺀 날짜
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // 일주일 전에 작성된 게시글을 가져와서 각각의 id를 deleteFind 메소드로 넘겨서 삭제
        findRepository.findByModifiedDateBefore(sevenDaysAgo).forEach(e -> {
            findRepository.deleteById(e.getId());
        });
    }

    // s3 매서드
    public String s3Upload(
            MultipartFile file) {
        try {
            String filename = getUuidFilename(file);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(dirName + "/" + filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return getS3FileUrl(filename);
        } catch (IOException e) {
            return "error: " + e;
        }
    }

    public GlobalResponse<String> s3Delete(Finding finding) {
        try {
            String fileName = getFileNameFromS3Url(finding.getPathUrl());
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(dirName + "/" + fileName)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            finding.setPathUrl(getS3FileUrl("defaultAvatar.jpg"));
        } catch (Exception e) {
            log.warn("Failed to delete old profile image", e);
        }
        return GlobalResponse.success("삭제 성공");
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
