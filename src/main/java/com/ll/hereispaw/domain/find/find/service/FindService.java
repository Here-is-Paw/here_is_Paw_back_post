package com.ll.hereispaw.domain.find.find.service;

import com.ll.hereispaw.domain.find.find.dto.FindDto;
import com.ll.hereispaw.domain.find.find.entity.FindPost;
import com.ll.hereispaw.domain.find.find.entity.Photo;
import com.ll.hereispaw.domain.find.find.repository.FindPhotoRepository;
import com.ll.hereispaw.domain.find.find.repository.FindRepository;
import lombok.RequiredArgsConstructor;
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

    private final FindRepository findRepository;
    private final FindPhotoRepository findPhotoRepository;

    public Long saveFind(
            String breed,
            String geo,
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

        findPost.setBreed(breed);
        findPost.setGeo(geo);
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
        return savedPost.getId(); // 저장된 find_post_id 반환
    }

    public String saveFindPhoto(String path_url, Long member_id, Long findPostId) {
        Photo photo = new Photo();
        photo.setPath_url(path_url);
        photo.setMember_id(member_id);
        photo.setPostId(findPostId);

        Photo savedPhoto = findPhotoRepository.save(photo);
        return savedPhoto.getPath_url(); // 저장된 photo ID 반환
    }

    public List<FindDto> findAll() {
        List<FindDto> findDtos = new ArrayList<>();
        findRepository.findAll().forEach(e -> {
            // find_post_id를 이용해 첫 번째 이미지 URL 가져오기
            String path_url = null;
            List<Photo> photos = findPhotoRepository.findByPostId(e.getId());
            if (photos != null && !photos.isEmpty()) {
                path_url = photos.get(0).getPath_url(); // 첫 번째 사진의 URL 사용
            } else {
                path_url = "test";
            }
            System.out.println(path_url);

            findDtos.add(
                    FindDto.builder()
                            .id(e.getId())
                            .breed(e.getBreed())
                            .geo(e.getGeo())
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

        String path_url = null;

        FindPost findPost = findRepository.findById(postId).get();

        List<Photo> photos = findPhotoRepository.findByPostId(findPost.getId());
        if (photos != null && !photos.isEmpty()) {
            path_url = photos.get(0).getPath_url(); // 첫 번째 사진의 URL 사용
        } else {
            path_url = "test";
        }

        FindDto findDto = FindDto.builder()
                .id(findPost.getId())
                .age(findPost.getAge())
                .breed(findPost.getBreed())
                .color(findPost.getColor())
                .etc(findPost.getEtc())
                .find_date(findPost.getFind_date())
                .gender(findPost.getGender())
                .geo(findPost.getGeo())
                .location(findPost.getLocation())
                .member_id(findPost.getMember_id())
                .name(findPost.getName())
                .neutered(findPost.isNeutered())
                .path_url(path_url)
                .build();

        return findDto;
    }
}
