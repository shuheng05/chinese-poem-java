//package com.sweet.controller;
//
//import com.sweet.mapper.PoemMapper;
//import jakarta.annotation.Resource;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//
///**
// * @author shuheng
// */
//@RestController
//public class ChatController {
//
//
//    private final ChatClient chatClient;
//
//    public ChatController(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }
//    @PostMapping("/chat")
//    public Flux<String> chat(@RequestBody String message) {
//        return chatClient.prompt()
//                .user(message)
//                .stream()
//                .content();
//    }
//
//
//}
