package musiccloud.api.websocket.model.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    public String email;
    public String password;
}
