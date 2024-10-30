//package com.sweet.controller;
//
//import com.sweet.config.ChatOption;
//import jakarta.websocket.*;
//import jakarta.websocket.server.ServerEndpoint;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.chat.prompt.ChatOptions;
//import org.springframework.ai.model.ModelOptions;
//import org.springframework.ai.openai.OpenAiChatModel;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//
//@ServerEndpoint(value = "/chat")
//@Component
//public class ChatEndpoint {
//
//    ChatModel myChatModel = new OpenAiChatModel(new OpenAiApi(
//            "https://api.xty.app",
//            "sk-udFqBatjMgxnqj4Y42D482D8C9A64e1a943a89653d2e9949"
//    ));
//
//    ChatClient.Builder builder = ChatClient.builder(myChatModel);
//    ChatClient chatClient = ChatClient.create(myChatModel);
//
//
//    @OnOpen
//    public void onOpen(Session session, EndpointConfig config) {
//        System.out.println("websocket open");
//    }
//
//    @OnMessage
//    public String onMessage(String message) {
//        ChatOption chatOption = new ChatOption();
//        return this.chatClient.prompt()
//                .options(chatOption)
//                .user(message)
//                .call()
//                .content();
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        System.out.println("websocket close");
//    }
//
//    @OnError
//    public void onError(Session session, Throwable t) {
//        System.out.println(t.getMessage());
//        System.out.println("websocket error");
//    }
//}
