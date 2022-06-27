package musiccloud.api.websocket.service;

import musiccloud.api.websocket.config.WebSocketConfig;
import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.AudioFile;
import musiccloud.api.websocket.entity.Playlist;
import musiccloud.api.websocket.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Service
public class EventService implements ApplicationListener<SessionSubscribeEvent>{
    private ApplicationEventPublisher publisher;
    private UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final String PLAYLIST_DEST = "/topic/playlist";

    @Autowired
    public EventService(SimpMessagingTemplate messagingTemplate, ApplicationEventPublisher publisher, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.publisher = publisher;
    }

    @Override
    @Transactional(readOnly = true)
    public void onApplicationEvent(SessionSubscribeEvent evt) {

        System.out.println(evt.getUser().getName());
        Principal principal = evt.getUser();
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(evt.getMessage());
        String destination = headers.getDestination();
        if(destination.equals("/users/topic/profile")){
            messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/profile", new UserDTO(userService.findByUsername(principal.getName())));
        }

        else if(destination.equals("/users/topic/playlist")){
            messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/playlist", userService.findUserPlaylistById(principal.getName()));
        }

        else if(destination.equals("/users/topic/player")){
            AudioFile audioFile = userService.findMostRecentlyPlayedByUsername(principal.getName());
            AudioDTO dto = AudioDTO
                    .builder()
                    .metadata(audioFile)
                    .url("http://localhost:8080/api/user/audio/" + audioFile.getId()+  "/download")
                    .username(principal.getName())
                    .build();
            messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/player",dto);
        }
    }

    @EventListener
    public void userUpdatedEventHandler(PlayTrackEvent evt){
        AudioDTO object = evt.getObject();
        messagingTemplate.convertAndSendToUser(object.getUsername(),"/topic/player",object);
    }

    @EventListener
    public void userUpdatedEventHandler(UserUpdateEvent evt){
        AppUser object = evt.getObject();
        messagingTemplate.convertAndSendToUser(object.getUsername(),"/topic/profile", new UserDTO(object));
    }

    @EventListener
    public void userUpdatedEventHandler(PlaylistUpdatedEvent evt){
        Playlist object = evt.getObject();
        messagingTemplate.convertAndSendToUser(object.getId(), "/topic/playlist", object);
    }

    public EventService triggerEvent(ApplicationEvent event){
        publisher.publishEvent(event);
        return this;
    }
}