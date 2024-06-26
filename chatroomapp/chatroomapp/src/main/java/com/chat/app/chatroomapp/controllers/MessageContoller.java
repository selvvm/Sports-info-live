package com.chat.app.chatroomapp.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.chat.app.chatroomapp.models.Message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;





@RestController

public class MessageContoller {

    @MessageMapping("/message")
    @SendTo("/topic/return-to")
    public Message getContent(@RequestBody Message message) {
         try{
            Thread.sleep(2000);
         }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        return message;
    }

}
