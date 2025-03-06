package com.ll.hereispaw.domain_msa.missing.missing.service;

import com.ll.hereispaw.domain_msa.missing.missing.dto.request.MissingRequest;
import com.ll.hereispaw.domain_msa.missing.missing.dto.response.MissingResponse;
import com.ll.hereispaw.domain_msa.missing.missing.entity.Missing;
import com.ll.hereispaw.domain_msa.missing.missing.repository.MissingRepository;
import com.ll.hereispaw.global_msa.error.ErrorCode;
import com.ll.hereispaw.global_msa.exception.CustomException;
import com.ll.hereispaw.global_msa.member.dto.MemberDto;
import com.ll.hereispaw.standard.Ut_msa.GeoUt;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Value("${custom.bucket.name}")
    private String bucketName;

    @Value("${custom.bucket.region}")
    private String region;

    @Value("${custom.bucket.missing}")
    private String dirName;

    private final S3Client s3Client;

    private final MissingRepository missingRepository;

    @Transactional
    public MissingResponse write(MemberDto author, MissingRequest missingRequest) {

        String pathUrl = s3Upload(missingRequest.getFile());

        System.out.println("geo: " + missingRequest.getGeo());


        Missing missing = missingRepository.save(
                Missing.builder()
                        .name(missingRequest.getName())
                        .breed(missingRequest.getBreed())
                        .geo(GeoUt.wktToPoint(missingRequest.getGeo()))
                        .location(missingRequest.getLocation())
                        .color(missingRequest.getColor())
                        .serialNumber(missingRequest.getSerialNumber())
                        .gender(missingRequest.getGender())
                        .neutered(missingRequest.getNeutered())
                        .age(missingRequest.getAge())
                        .lostDate(missingRequest.getLostDate())
                        .etc(missingRequest.getEtc())
                        .reward(missingRequest.getReward())
                        .missingState(missingRequest.getMissingState())
                        .pathUrl(pathUrl)
                        .authorId(author.getId())
                        .nickname(author.getNickname())
                        .build()
        );

        return new MissingResponse(missing);
    }

//    public List<MissingResponseDto> list() {
//        List<Missing> missings = missingRepository.findAll();
//        List<MissingResponseDto> missingResponseDtos = new ArrayList<>();
//
//        for (Missing missing : missings) {
//
//            missingResponseDtos.add(
//                    new MissingResponseDto(missing)
//            );
//        }
//
//        return missingResponseDtos;
//    }

    // 전체 조회 페이지 적용
    public Page<MissingResponse> list(Pageable pageable) {
        Page<Missing> missingPage = missingRepository.findAll(pageable);

        return missingPage.map(MissingResponse::new);
    }

    public MissingResponse findById(Long missingId) {
        Missing missing = missingRepository.findById(missingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        return new MissingResponse(missing);
    }

    @Transactional
    public MissingResponse update(
            MemberDto author,
            MissingRequest missingRequest,
            Long missingId) {

        Missing missing = missingRepository.findById(missingId).orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        if (!author.getId().equals(missing.getId())) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }

        String pathUrl = s3Upload(missingRequest.getFile());

        missing.setName(missingRequest.getName());
        missing.setBreed(missingRequest.getBreed());
        missing.setGeo(GeoUt.wktToPoint(missingRequest.getGeo()));
        missing.setLocation(missingRequest.getLocation());
        missing.setColor(missingRequest.getColor());
        missing.setSerialNumber(missingRequest.getSerialNumber());
        missing.setGender(missingRequest.getGender());
        missing.setNeutered(missingRequest.getNeutered());
        missing.setAge(missingRequest.getAge());
        missing.setLostDate(missingRequest.getLostDate());
        missing.setEtc(missingRequest.getEtc());
        missing.setMissingState(missingRequest.getMissingState());
        missing.setReward(missingRequest.getReward());
        missing.setPathUrl(pathUrl);

        missingRepository.save(missing);

        return new MissingResponse(missing);
    }

    @Transactional
    public void delete(MemberDto author, Long missingId) {
        Missing missing = missingRepository.findById(missingId).orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        if (!author.getId().equals(missing.getId())) {
            throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
        }

        s3Delete(missing.getPathUrl());
        missingRepository.delete(missing);
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

