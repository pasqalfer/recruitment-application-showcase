package musiccloud.api.websocket.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
public class AudioFile {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean sharable;

    private byte [] albumImage;

    private String album;

    @Column(nullable = false)
    private Timestamp timeUploaded;

    private String genre;

    private String title;

    private String artist;


}
