// ChatRoomController.java
package com.crick.api.controllers;

import com.crick.api.entities.ChatRoom;
import com.crick.api.services.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin("*")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody Long matchId) {
        ChatRoom createdChatRoom = chatRoomService.createChatRoom(matchId);
        return new ResponseEntity<>(createdChatRoom, HttpStatus.CREATED);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable Long matchId) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(matchId);
        if (chatRoom != null) {
            return new ResponseEntity<>(chatRoom, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add more endpoints as needed
}
