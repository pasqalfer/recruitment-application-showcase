package musiccloud.api.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/subscribe").setAllowedOrigins("http://localhost:3000").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("topic");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/users");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new UserInterceptor());
    }

    public static class User implements Principal {

        private String name;

        public User(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    public static class Playlist implements Principal {

        private String name;

        public Playlist(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }



}

class UserInterceptor implements ChannelInterceptor {
    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
                Object name = ((Map) raw).get("username");
                Object id = ((Map) raw).get("playlist");

                if (name instanceof ArrayList && id instanceof ArrayList) {
                    return message;
                } else if (id instanceof ArrayList) {
                        accessor.setUser(new WebSocketConfig.Playlist(((ArrayList<String>) id).get(0).toString()));
                } else if (name instanceof ArrayList) {
                        accessor.setUser(new WebSocketConfig.User(((ArrayList<String>) name).get(0).toString()));
                }

            }
        }
        return message;
    }
}

