package musiccloud.api.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import musiccloud.api.websocket.model.payload.AuthorizationToken;
import musiccloud.api.websocket.model.payload.LoginRequest;
import musiccloud.api.websocket.model.RegistrationRequest;
import musiccloud.api.websocket.service.FirebaseService;
import musiccloud.api.websocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private final UserService userService;
    private final FirebaseService firebaseService;

    @Autowired
    public AuthenticationController(UserService userService, FirebaseService firebaseService){
        this.userService = userService;
        this.firebaseService = firebaseService;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = {"application/json"})
    @CrossOrigin
    public ResponseEntity<AuthorizationToken> login(@RequestBody LoginRequest loginRequest) throws JsonProcessingException {
          AuthorizationToken authorizationToken = firebaseService.emailPasswordAuthentication(loginRequest.email,loginRequest.password);
          if(Objects.nonNull(authorizationToken)){
              return ResponseEntity.ok(authorizationToken);
          }
          else {
              return ResponseEntity.badRequest().build();
          }
    }

    @GetMapping(value = "/register", consumes = "application/json", produces = {"application/json"})
    @CrossOrigin
    public ResponseEntity<Boolean> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.registerUser(registrationRequest.email,registrationRequest.username,registrationRequest.password));
    }

    @PostMapping(value = "/current-user", consumes = "application/json", produces = {"application/json"})
    @CrossOrigin
    public ResponseEntity<String> current(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }
}
