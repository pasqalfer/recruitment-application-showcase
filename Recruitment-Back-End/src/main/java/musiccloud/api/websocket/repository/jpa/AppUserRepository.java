package musiccloud.api.websocket.repository.jpa;

import musiccloud.api.websocket.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
    AppUser findAppUserByUsername(String username);
    List<AppUser> findByEmailContainingOrUsernameContaining(String email, String username);



}
