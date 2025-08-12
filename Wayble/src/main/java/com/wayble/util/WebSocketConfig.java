package com.wayble.util;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages = "com.wayble.controller")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // "/topic" 접두사를 사용하는 클라이언트는 메시지 브로커를 통해 메시지를 수신합니다.
        config.setApplicationDestinationPrefixes("/app"); // "/app" 접두사를 사용하여 클라이언트가 메시지를 보내는 엔드포인트를 정의합니다.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS(); // 웹소켓 엔드포인트 "/ws"를 등록하고 SockJS를 사용합니다.
    }
    
    @PostConstruct
    public void init() {
        System.out.println("==== WebSocketConfig 로딩됨 ====");
    }
}
