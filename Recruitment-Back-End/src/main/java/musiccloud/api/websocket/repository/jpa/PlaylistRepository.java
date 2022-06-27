package musiccloud.api.websocket.repository.jpa;

import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,String> {
    @Query("select p.owner from Playlist p where p.id = ?1 ")
    AppUser findOwnerById(String id);
}
