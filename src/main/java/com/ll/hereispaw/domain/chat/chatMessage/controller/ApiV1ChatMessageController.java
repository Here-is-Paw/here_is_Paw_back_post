package com.ll.hereispaw.domain.chat.chatMessage.controller;

import com.ll.hereispaw.domain.chat.chatMessage.dto.ChatMessageDto;
import com.ll.hereispaw.domain.chat.chatMessage.dto.ChatMessageResponseDto;
import com.ll.hereispaw.domain.chat.chatMessage.dto.CreateChatMessage;
import com.ll.hereispaw.domain.chat.chatMessage.entity.ChatMessage;
import com.ll.hereispaw.domain.chat.chatMessage.service.ChatMessageService;
import com.ll.hereispaw.domain.member.member.entity.Member;
import com.ll.hereispaw.global.globalDto.GlobalResponse;
import com.ll.hereispaw.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ApiV1ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    //메세지 생성
    @PostMapping("/{chatRoomId}/messages")
    public GlobalResponse<ChatMessageDto> writeMessage(@PathVariable("chatRoomId") Long chatRoomId, @RequestBody CreateChatMessage createChatMessage, @LoginUser Member member){
        ChatMessage chatMessage = this.chatMessageService.writeMessage(chatRoomId, createChatMessage.getChatMessageId(), createChatMessage.getContent(), member);
        ChatMessageDto messages = new ChatMessageDto(chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/api/v1/chat/"+ chatRoomId +"/messages", messages);
        return GlobalResponse.success(messages);
    }

    //메세지 조회
    @GetMapping("/{chatRoomId}/messages")
    public GlobalResponse<List<ChatMessageResponseDto>> listMessage(@LoginUser Member loginUser, @PathVariable("chatRoomId")Long chatRoomId){
        List<ChatMessageResponseDto> list = this.chatMessageService.listMessage(loginUser, chatRoomId);
        return GlobalResponse.success(list);
    }
}
