package musiccloud.api.websocket.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentlyPlayedFeed {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private Date timePlayed;

    @PrePersist
    protected void onCreate() {
        timePlayed = new Date();
    }

    @ManyToOne
    private AudioFile playedFile;
}
