package com.ll.hereispaw.domain.find.find.controler;

import com.ll.hereispaw.domain.chat.chatRoom.service.ChatRoomService;
import com.ll.hereispaw.domain.find.find.dto.FindDto;
import com.ll.hereispaw.domain.find.find.dto.FindWithPhotoRequest;
import com.ll.hereispaw.domain.find.find.service.FindImageService;
import com.ll.hereispaw.domain.find.find.service.FindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/find")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173",
        allowedHeaders = "*",
        allowCredentials = "true")
public class FindController {

    private final FindService findService;
    private final FindImageService findImageService;
    //채팅방 연동
    private final ChatRoomService chatRoomService;

    // 유기견 발견 전체 조회
    @GetMapping
    public List<FindDto> showFind() {
        List<FindDto> findDtos = findService.findAll();

        return findDtos;
    }

    @GetMapping("/{postId}")
    public FindDto findPostById(@PathVariable("postId") Long postId) {
        FindDto findDto = findService.findById(postId);

        return findDto;
    }

    // 유기견 발견 신고시 저장
    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> newFind(@RequestBody FindWithPhotoRequest request) {
        // 상태 0: 발견, 1: 보호, 2: 완료
        int state = 0;

        // Base64 -> URL 변환
        String imageUrl = findImageService.saveBase64Image(request.getPath_url());

        // saveFind 호출하여 새롭게 저장하고 id 반환
        Long findPostId = findService.saveFind(
                request.getTitle(), request.getSituation(),
                request.getBreed(), request.getGeo(), request.getLocation(),
                request.getName(), request.getColor(), request.getEtc(), request.getGender(),
                request.getAge(),
                state, request.isNeutered(), request.getFind_date(),
                request.getMember_id(), request.getShelter_id()
        );

        // 변환된 URL을 저장
        String pathUrl = findService.saveFindPhoto(
                imageUrl,
                request.getMember_id(), findPostId
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "저장 완료");
        response.put("findPostId", findPostId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<String> updateReview(
            @PathVariable("postId") Long postId,
            @RequestBody FindWithPhotoRequest request) {

        // 상태 0: 발견, 1: 보호, 2: 완료
        int state = 0;

        // Base64 -> URL 변환
        String imageUrl = findImageService.saveBase64Image(request.getPath_url());

        findService.updateFind(postId, request.getTitle(), request.getSituation(),
                request.getBreed(), request.getGeo(), request.getLocation(),
                request.getName(), request.getColor(), request.getEtc(), request.getGender(),
                request.getAge(),
                state, request.isNeutered(), request.getFind_date(),
                request.getMember_id(), request.getShelter_id());

        // 변환된 URL을 저장
        String pathUrl = findService.updateFindPhoto(
                imageUrl,
                request.getMember_id(), postId
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "저장 완료");
        response.put("findPostId", postId);
        return ResponseEntity.ok("발견 게시글 수정 성공");
    }
/*
    @PostMapping("/{postId}/chat")
    public GlobalResponse<ChatRoomDto> Chat(@PathVariable("postId") Long postId, @LoginUser Member chatUser) {
        FindDto findDto = findService.findById(postId);

        // 게시글 작성자 조회
        Long targetUserId = findDto.getMember_id();

        // 채팅방 생성 또는 조회
        ChatRoom chatRoom = chatRoomService.createRoomOrView(chatUser, targetUserId);
        ChatRoomDto chatRoomDto = new ChatRoomDto(chatRoom);

        return GlobalResponse.success(chatRoomDto);
    }
    */
}
