package musiccloud.api.websocket.repository.neo4j;

import musiccloud.api.websocket.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends Neo4jRepository<Profile,String> {
    Optional<Profile> findById(String id);

    @Query("MATCH (n)-[r]->() where n.id={0} RETURN COUNT(r)")
    Long findOutDegree(String id);

    @Query("MATCH (n)<-[r]-() where n.id={0} RETURN COUNT(r)")
    Long findInDegree(String id);

    @Query("MATCH (n1:Profile{ id:{0} }), (n2:Profile{id:{1} }) RETURN EXISTS((n1)-[:FOLLOWS]->(n2))")
    boolean isFollowing(String idA, String idB);

    @Query("MATCH (n:Profile{id:{0}})<--(f:Profile) Return f")
    List<Profile> findFollowers(String id);

    @Query(value = "MATCH (n:Profile{id:{0}})<--(f:Profile) Return f",
            countQuery = "MATCH (n:Profile{id:{0}})<--(f:id) Return count(f)")
    Page<Profile> findFollowers(String id, Pageable pageable);

    @Query("MATCH (n:Profile{id:{0}})-->(f:Profile) Return f")
    List<Profile> findFollowing(String id);
}
