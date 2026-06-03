package pl.ur8an5ky.livematch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket + STOMP configuration for real-time match event broadcasting.
 * Architecture:
 *  - clients connect to ws://host/ws and authenticate via STOMP
 *  - clients subscribe to /topic/matches/{matchId}/* channels
 *  - server pushes events to those topics via SimpMessagingTemplate
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefix for messages SERVER → CLIENT (broadcasts)
        config.enableSimpleBroker("/topic");

        // Prefix for messages CLIENT → SERVER routed to @MessageMapping methods.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
//                        "http://localhost:5173",
//                        "http://localhost:3000"
                        "http://localhost:*",
                        "http://127.0.0.1:*"
                );
    }
}