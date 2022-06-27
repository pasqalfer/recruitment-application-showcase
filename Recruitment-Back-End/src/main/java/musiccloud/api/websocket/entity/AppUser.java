package musiccloud.api.websocket.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import musiccloud.api.websocket.model.ObservableEntity;

import javax.persistence.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements ObservableEntity {
    @Id
    private String id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String password;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY,mappedBy = "owner")
    @JsonManagedReference
    private List<Playlist> playlists;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AudioFile> userUploads;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecentlyPlayedFeed> recentlyPlayedFeeds;
}
