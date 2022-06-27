package musiccloud.api.websocket.model;

import lombok.Builder;
import musiccloud.api.websocket.entity.AppUser;
import org.springframework.context.ApplicationEvent;


public class UserUpdateEvent extends ApplicationEvent {
    private AppUser object;

    @Builder
    public UserUpdateEvent(Object source,  AppUser object) {
        super(source);
        this.object = object;
    }

    public AppUser getObject() {
        return object;
    }
}
