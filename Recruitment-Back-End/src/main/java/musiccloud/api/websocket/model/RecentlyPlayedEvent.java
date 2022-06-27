package musiccloud.api.websocket.model;

import lombok.Builder;
import musiccloud.api.websocket.entity.AppUser;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class RecentlyPlayedEvent extends ApplicationEvent {
    private List<RecentlyPlayedEvent> object;

    @Builder
    public RecentlyPlayedEvent(Object source, List<RecentlyPlayedEvent> object) {
        super(source);
        this.object = object;
    }

    public List<RecentlyPlayedEvent> getObject() {
        return object;
    }
}
