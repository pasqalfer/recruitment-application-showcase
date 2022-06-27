package musiccloud.api.websocket.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import musiccloud.api.websocket.model.ObservableEntity;
import musiccloud.api.websocket.model.UserDTO;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Playlist implements ObservableEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean sharable;

    @ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AudioFile> playlistSongs;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser owner;

    @Transient
    private String ownerName;
}
