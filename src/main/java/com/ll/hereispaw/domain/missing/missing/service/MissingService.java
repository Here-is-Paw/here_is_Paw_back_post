package com.ll.hereispaw.domain.missing.missing.service;

import com.ll.hereispaw.domain.member.member.entity.Member;
import com.ll.hereispaw.domain.missing.Auhtor.entity.Author;
import com.ll.hereispaw.domain.missing.Auhtor.repository.AuthorRepository;
import com.ll.hereispaw.domain.missing.missing.dto.request.MissingRequestDTO;
import com.ll.hereispaw.domain.missing.missing.dto.response.MissingDTO;
import com.ll.hereispaw.domain.missing.missing.entity.Missing;
import com.ll.hereispaw.domain.missing.missing.repository.MissingRepository;
import com.ll.hereispaw.global.error.ErrorCode;
import com.ll.hereispaw.global.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissingService {
    private final MissingRepository missingRepository;
    private final AuthorRepository authorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MissingDTO write(MissingRequestDTO missingRequestDto) {

        log.debug("author : {}", missingRequestDto.getAuthor());

        Missing missing = missingRepository.save(
                Missing.builder()
                        .name(missingRequestDto.getName())
                        .breed(missingRequestDto.getBreed())
                        .geo(missingRequestDto.getGeo())
                        .location(missingRequestDto.getLocation())
                        .color(missingRequestDto.getColor())
                        .serialNumber(missingRequestDto.getSerialNumber())
                        .gender(missingRequestDto.isGender())
                        .neutered(missingRequestDto.isNeutered())
                        .age(missingRequestDto.getAge())
                        .lostDate(missingRequestDto.getLostDate())
                        .etc(missingRequestDto.getEtc())
                        .reward(missingRequestDto.getReward())
                        .state(missingRequestDto.getState())
                        .pathUrl(missingRequestDto.getPathUrl())
                        .author(missingRequestDto.getAuthor())
                        .build()
        );

        return new MissingDTO(missing);
    }

    public Author of(Member member) {
        return entityManager.getReference(Author.class, member.getId());
    }

    public List<MissingDTO> list() {
        List<Missing> missings = missingRepository.findAll();
        List<MissingDTO> missingDTOS = new ArrayList<>();

        for (Missing missing : missings) {

            Author author = authorRepository.findById(missing.getAuthor().getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            missingDTOS.add(
                    MissingDTO.builder()
                            .id(missing.getId())
                            .name(missing.getName())
                            .breed(missing.getBreed())
                            .geo(missing.getGeo())
                            .location(missing.getLocation())
                            .color(missing.getColor())
                            .serialNumber(missing.getSerialNumber())
                            .gender(missing.isGender())
                            .neutered(missing.isNeutered())
                            .age(missing.getAge())
                            .lostDate(missing.getLostDate())
                            .etc(missing.getEtc())
                            .reward(missing.getReward())
                            .state(missing.getState())
                            .pathUrl(missing.getPathUrl())
                            .nickname(author.getNickname())
                            .build()
            );
        }

        return missingDTOS;
    }

    public MissingDTO findById(Long missingId) {
        Missing missing = missingRepository.findById(missingId)
                .orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        return new MissingDTO(missing);
    }

    public MissingDTO update(MissingRequestDTO missingRequestDTO, Long missingId) {

        Missing missing = missingRepository.findById(missingId).orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        log.debug("missing : {}", missing);
        log.debug("missing.name : {}", missing.getName());

        // 수정 시 유저 확인
        // - 코드 작성해야함.

        missing.setName(missingRequestDTO.getName());
        missing.setBreed(missingRequestDTO.getBreed());
        missing.setGeo(missingRequestDTO.getGeo());
        missing.setLocation(missingRequestDTO.getLocation());
        missing.setColor(missingRequestDTO.getColor());
        missing.setSerialNumber(missingRequestDTO.getSerialNumber());
        missing.setGender(missingRequestDTO.isGender());
        missing.setNeutered(missingRequestDTO.isNeutered());
        missing.setAge(missingRequestDTO.getAge());
        missing.setLostDate(missingRequestDTO.getLostDate());
        missing.setEtc(missingRequestDTO.getEtc());
        missing.setState(missingRequestDTO.getState());
        missing.setReward(missingRequestDTO.getReward());
        missing.setPathUrl(missingRequestDTO.getPathUrl());

        missingRepository.save(missing);

        return new MissingDTO(missing);

    }

    public String delete(Long missingId) {
        Missing missing = missingRepository.findById(missingId).orElseThrow(() -> new CustomException(ErrorCode.MISSING_NOT_FOUND));

        missingRepository.delete(missing);

        return "신고글 삭제";
    }
}

