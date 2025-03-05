package com.ll.hereispaw.domain.find.find.service;

import com.ll.hereispaw.domain.find.find.dto.DogFaceRequestDto;
import com.ll.hereispaw.domain.find.find.dto.FindDto;
import com.ll.hereispaw.domain.find.find.entity.FindPost;
import com.ll.hereispaw.domain.find.find.entity.Photo;
import com.ll.hereispaw.domain.find.find.repository.FindPhotoRepository;
import com.ll.hereispaw.domain.find.find.repository.FindRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.processing.Find;
import org.locationtech.jts.geom.Point;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FindService {
    private static String POST_TYPE = "finding";

    private final FindRepository findRepository;
    private final FindPhotoRepository findPhotoRepository;

    //카프카 발행자 템플릿
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public Long saveFind(
            String title,
            String situation,
            String breed,
            Point geo,
            String location,
            String name,
            String color,
            String etc,
            String gender,
            int age,
            int state,
            boolean neutered,
            LocalDateTime find_date,
            Long member_id,
            Long shelter_id
    ) {
        FindPost findPost = new FindPost();

        findPost.setTitle(title);
        findPost.setSituation(situation);
        findPost.setBreed(breed);
//        findPost.setGeo(geo);
        findPost.setLocation(location);
        findPost.setName(name);
        findPost.setColor(color);
        findPost.setEtc(etc);
        findPost.setGender(gender);
        findPost.setAge(age);
        findPost.setState(state);
        findPost.setNeutered(neutered);
        findPost.setFind_date(find_date);
        findPost.setMember_id(member_id);
        findPost.setShelter_id(shelter_id);

        FindPost savedPost = findRepository.save(findPost);

        //카프카 메시지 발행
        DogFaceRequestDto dogFaceRequestDto = DogFaceRequestDto.builder()
            .type("save")
            .image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZAHxgFBNiIPBx5mXMtIoMN7udx45JgJOoq0aZqnhHhxcQEKsjcjMivntAgRt_tUIP8ZsY5wOuNyXscwNWrBoHYGZfhFzhCPAO2lq87Ag")
            .postType(POST_TYPE)
            .postId(savedPost.getId())
            .postMemberId(savedPost.getMember_id())
            .build();
        kafkaTemplate.send("dog-face-request", dogFaceRequestDto);
        return savedPost.getId(); // 저장된 find_post_id 반환
    }

    public String saveFindPhoto(String path_url, Long member_id, Long findPostId) {
        Photo photo = new Photo();
        photo.setPath_url(path_url);
        photo.setMember_id(member_id);
        photo.setPostId(findPostId);

        Photo savedPhoto = findPhotoRepository.save(photo);

//        //카프카 메시지 발행
//        DogFaceRequestDto dogFaceRequestDto = DogFaceRequestDto.builder()
//                .type("save")
//                .image(path_url)
//                .postType(POST_TYPE)
//                .postId(findPostId)
//                .postMemberId(savedPost.getMember_id())
//                .build();
//        kafkaTemplate.send("dog-face-request", dogFaceRequestDto);

        return savedPhoto.getPath_url(); // 저장된 photo ID 반환
    }

    public List<FindDto> findAll() {
        List<FindDto> findDtos = new ArrayList<>();
        findRepository.findAll().forEach(e -> {
            // find_post_id를 이용해 첫 번째 이미지 URL 가져오기
            Photo photos = findPhotoRepository.findByPostId(e.getId());
            String path_url = photos.getPath_url(); // 첫 번째 사진의 URL 사용

            findDtos.add(
                    FindDto.builder()
                            .id(e.getId())
                            .breed(e.getBreed())
//                            .geo(e.getGeo())
                            .location(e.getLocation())
                            .name(e.getName())
                            .color(e.getColor())
                            .gender(e.getGender())
                            .etc(e.getEtc())
                            .age(e.getAge())
                            .neutered(e.isNeutered())
                            .find_date(e.getFind_date())
                            .member_id(e.getMember_id())
                            .shelter_id(e.getShelter_id())
                            .path_url(path_url) // 이미지 URL 추가
                            .build()
            );
        });

        return findDtos;
    }

    public FindDto findById(Long postId) {

        FindPost findPost = findRepository.findById(postId).get();

        Photo photo = findPhotoRepository.findByPostId(findPost.getId());
        String path_url = photo.getPath_url(); // 첫 번째 사진의 URL 사용

        FindDto findDto = FindDto.builder()
                .id(findPost.getId())
                .title(findPost.getTitle())
                .situation(findPost.getSituation())
                .age(findPost.getAge())
                .breed(findPost.getBreed())
                .color(findPost.getColor())
                .etc(findPost.getEtc())
                .find_date(findPost.getFind_date())
                .gender(findPost.getGender())
//                .geo(findPost.getGeo())
                .location(findPost.getLocation())
                .member_id(findPost.getMember_id())
                .name(findPost.getName())
                .neutered(findPost.isNeutered())
                .path_url(path_url)
                .member_id(findPost.getMember_id())
                .build();

        return findDto;
    }

    public String updateFind(
            Long postId,
            String title,
            String situation,
            String breed,
            Point geo,
            String location,
            String name,
            String color,
            String etc,
            String gender,
            int age,
            int state,
            boolean neutered,
            LocalDateTime find_date,
            Long member_id,
            Long shelter_id
    ) {
        FindPost findPost = findRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        findPost.setTitle(title);
        findPost.setSituation(situation);
        findPost.setBreed(breed);
//        findPost.setGeo(geo);
        findPost.setLocation(location);
        findPost.setName(name);
        findPost.setColor(color);
        findPost.setEtc(etc);
        findPost.setGender(gender);
        findPost.setAge(age);
        findPost.setState(state);
        findPost.setNeutered(neutered);
        findPost.setFind_date(find_date);
        findPost.setMember_id(member_id);
        findPost.setShelter_id(shelter_id);

        FindPost savedPost = findRepository.save(findPost);

        return "수정 완료";
    }

    public String updateFindPhoto(String path_url, Long member_id, Long postId) {
        Photo photo = findPhotoRepository.findByPostId(postId);

        photo.setPath_url(path_url);
        photo.setMember_id(member_id);
        photo.setPostId(postId);

        Photo savedPhoto = findPhotoRepository.save(photo);

        //카프카 메시지 발행
//        DogFaceRequestDto dogFaceRequestDto = DogFaceRequestDto.builder()
//                .type("save")
//                .image(path_url)
//                .postType(POST_TYPE)
//                .postId(postId)
//                .build();
//        kafkaTemplate.send("dog-face-request", dogFaceRequestDto);
        return "사진 수정 완료";
    }

}
