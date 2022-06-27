package musiccloud.api.websocket.model.payload;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthorizationToken {
    private String userId;
    private long expiresIn;
    private String token;
    private String refreshToken;
    private String startTime;
}
