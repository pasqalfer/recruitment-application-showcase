package musiccloud.api.websocket.model;

import lombok.Builder;
import musiccloud.api.websocket.entity.Playlist;
import org.springframework.context.ApplicationEvent;

public class PlaylistUpdatedEvent extends ApplicationEvent {
    private Playlist object;

    @Builder
    public PlaylistUpdatedEvent(Object source, Playlist object) {
        super(source);
        this.object = object;
    }

    public Playlist getObject() {
        return object;
    }
}
