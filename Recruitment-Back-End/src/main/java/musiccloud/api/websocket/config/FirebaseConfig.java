package musiccloud.api.websocket.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
@PropertySource("classpath:firebase.properties")
public class FirebaseConfig {

    @Value("${apiKey}")
    private String apiKey;

    @Value("${singup.url}")
    private String signUpUrl;

    @Value("${emailPasswordAuth.url}")
    private String emailPasswordAuthURL;

    @Value("${token.url}")
    private String tokenURL;

    @Value("${name}")
    private String _bucketName;

    @Value("${keyPath}")
    private String keyPath;

    public String bucketName(){
        return _bucketName;
    }

    public String signUpUrl(){
        return signUpUrl + apiKey;
    }

    public String emailPasswordAuthURL(){
        return emailPasswordAuthURL+apiKey;
    }

    public String tokenURL(){
        return tokenURL + apiKey;
    }

    public String keyPath(){
        return tokenURL + apiKey;
    }
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(100000000L));
        factory.setMaxRequestSize(DataSize.ofBytes(100000000L));
        return factory.createMultipartConfig();
    }


    @PostConstruct
    public void initialize() {
        FileInputStream serviceAccount = null;

        try {
            serviceAccount = new FileInputStream(keyPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(_bucketName)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseApp.initializeApp(options);
    }
}