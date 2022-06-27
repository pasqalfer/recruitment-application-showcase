package musiccloud.api.websocket.repository.jpa;

import musiccloud.api.websocket.entity.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends JpaRepository<AudioFile,String> {
}
