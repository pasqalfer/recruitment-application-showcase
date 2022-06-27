package musiccloud.api.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"musiccloud.api.websocket.repository.jpa"})
@EnableNeo4jRepositories(basePackages = {"musiccloud.api.websocket.repository.neo4j"})
public class WebsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }
}
