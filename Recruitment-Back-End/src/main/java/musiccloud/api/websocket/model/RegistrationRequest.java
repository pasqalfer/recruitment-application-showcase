package musiccloud.api.websocket.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationRequest {
    public String email;
    public String username;
    public String password;
}
