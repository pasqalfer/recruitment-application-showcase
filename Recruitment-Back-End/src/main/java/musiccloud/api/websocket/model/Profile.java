package musiccloud.api.websocket.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;
@Data
@Builder
@Node("Profile")
public class Profile {
    @Id
    private String id;

    @Relationship(type = "Follows", direction = Relationship.Direction.INCOMING)
    private Set<Follows> followers;



}
