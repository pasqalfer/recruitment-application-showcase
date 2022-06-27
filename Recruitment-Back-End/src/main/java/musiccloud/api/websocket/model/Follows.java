package musiccloud.api.websocket.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class Follows {
        @Id
        @GeneratedValue
        private Long id;

        private String timeFollowed;

        @TargetNode
        private Profile followingProfile;

}
