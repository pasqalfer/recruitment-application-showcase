package musiccloud.api.websocket.model;

import lombok.Builder;
import musiccloud.api.websocket.entity.AppUser;
import org.springframework.context.ApplicationEvent;

public class PlayTrackEvent extends ApplicationEvent {

    private AudioDTO object;

    @Builder
    public PlayTrackEvent(Object source,  AudioDTO object) {
        super(source);
        this.object = object;
    }

    public AudioDTO getObject() {
        return object;
    }
}
