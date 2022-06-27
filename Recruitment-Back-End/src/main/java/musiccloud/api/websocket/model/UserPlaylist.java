package musiccloud.api.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.Playlist;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPlaylist {
    private AppUser userId;
    private Playlist playlistId;
}
