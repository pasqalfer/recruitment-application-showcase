package musiccloud.api.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import musiccloud.api.websocket.config.FirebaseConfig;
import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.model.payload.AuthorizationToken;
import musiccloud.api.websocket.repository.jpa.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class FirebaseService {


    private final AppUserRepository appUserRepository;

    private final FirebaseConfig config;


    @Autowired
    public FirebaseService(AppUserRepository repo, FirebaseConfig config){
        this.appUserRepository = repo;
        this.config = config;
    }

    public AuthorizationToken emailPasswordAuthentication(String email, String password) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HashMap requestBody = new HashMap();
        requestBody.put("email",email);
        requestBody.put("password", password);
        requestBody.put("returnSecureToken","true");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String jsonBody = new ObjectMapper().writeValueAsString(requestBody);
        try {
            HashMap<String,String> response = restTemplate.postForObject(config.emailPasswordAuthURL(), jsonBody, HashMap.class);
            AuthorizationToken authToken = AuthorizationToken.builder()
                    .expiresIn(Long.parseLong(response.get("expiresIn")))
                    .refreshToken(response.get("refreshToken"))
                    .token(response.get("idToken"))
                    .startTime(LocalDateTime.now().toString())
                    .userId(response.get("localId"))
                    .build();
            return authToken;
        } catch (HttpClientErrorException e){
            return null;
        }
    }

    public AppUser userFromToken(String token) throws FirebaseAuthException {
        //checks if token is there
        FirebaseToken decodedToken = decodeToken(token);
        if(decodedToken == null)
            return null;
        AppUser appUser = appUserRepository.getById(decodedToken.getUid());
        return appUser;
    }

    public FirebaseToken decodeToken(String token) throws FirebaseAuthException {
        //checks if token is there
        FirebaseToken decodedToken = null;
        //verifies token to firebase server
        decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        return decodedToken;
    }



}
