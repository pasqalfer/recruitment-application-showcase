package musiccloud.api.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.AudioFile;
import musiccloud.api.websocket.entity.Playlist;
import musiccloud.api.websocket.entity.RecentlyPlayedFeed;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String id;

    private String username;

    private List<Playlist> playlists;

    private List<AudioFile> userUploads;

    private List<RecentlyPlayedFeed> recentlyPlayedFeeds;

    public UserDTO(AppUser appUser){
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.playlists = appUser.getPlaylists();
        this.userUploads = appUser.getUserUploads();
        this.recentlyPlayedFeeds = appUser.getRecentlyPlayedFeeds();
    }

}
