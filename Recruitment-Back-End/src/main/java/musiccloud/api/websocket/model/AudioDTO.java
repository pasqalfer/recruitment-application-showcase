package musiccloud.api.websocket.model;

import lombok.*;
import musiccloud.api.websocket.entity.AudioFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AudioDTO {
    public AudioFile metadata;
    public String username;
    public String url;
}
