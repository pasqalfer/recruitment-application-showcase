package musiccloud.api.websocket.service;

import musiccloud.api.websocket.repository.jpa.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileService {
    @Autowired
    private AppUserRepository userRepository;

}
