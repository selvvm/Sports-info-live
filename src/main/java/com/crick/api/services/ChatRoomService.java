// ChatRoomService.java
package com.crick.api.services;

import com.crick.api.entities.ChatRoom;
import com.crick.api.repositories.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom createChatRoom(Long matchId) {
        ChatRoom chatRoom = new ChatRoom(matchId);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoom(Long matchId) {
        return chatRoomRepository.findByMatchId(matchId);
    }

}
