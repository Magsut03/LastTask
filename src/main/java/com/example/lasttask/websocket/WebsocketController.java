package com.example.lasttask.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebsocketController {

//    SimpMessagingTemplate template;
//
//    @PostMapping("/send")
//    public ResponseEntity<Void> sendMessage(@RequestBody TextMessageDto textMessageDTO) {
//        template.convertAndSend("/topic/message", textMessageDTO);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @MessageMapping("/sendMessage")
//    public void receiveMessage(@Payload TextMessageDto textMessageDTO) {
//        // receive message from client
//    }
//
//
//    @SendTo("/topic/message")
//    public TextMessageDto broadcastMessage(@Payload TextMessageDto textMessageDTO) {
//        return textMessageDTO;
//    }
}