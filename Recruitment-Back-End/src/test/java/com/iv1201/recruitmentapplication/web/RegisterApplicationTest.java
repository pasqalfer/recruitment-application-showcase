package com.iv1201.recruitmentapplication.web;

import com.iv1201.recruitmentapplication.TestUtil;
import com.iv1201.recruitmentapplication.model.payload.request.LoginRequest;
import com.iv1201.recruitmentapplication.model.payload.request.UserSignupRequest;
import com.iv1201.recruitmentapplication.model.payload.response.JwtResponse;
import com.iv1201.recruitmentapplication.util.DTOUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterApplicationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DTOUtil dtoUtil;

    @TestConfiguration
    public static class TestConfig{

    }

    /**
     * Assert that correct response code is returned from the http response
     *
     * Expected response code : 200
     */
    @Test
    public void testRegisterApplication(){
        URI uri = TestUtil.localURI(port, "api/users/applicant/register");
        UserSignupRequest userSignupRequest = new UserSignupRequest();
        userSignupRequest.email = "mail@mail.com";
        userSignupRequest.name = "name";
        userSignupRequest.password = "123456";
        userSignupRequest.surname = "name";
        userSignupRequest.pnr = "19440412-1313";
        userSignupRequest.username = "testUser";
        ResponseEntity<Object> response = restTemplate.postForEntity(uri,userSignupRequest, Object.class);

        URI uri2 = TestUtil.localURI(port, "api/users/applicant/authenticate");
        LoginRequest loginRequest = new LoginRequest( userSignupRequest.username,  userSignupRequest.password);
        ResponseEntity<Object> response2 = restTemplate.postForEntity(uri2,loginRequest, Object.class);
        JwtResponse jwt = dtoUtil.fromMap(response.getBody(), JwtResponse.class);

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Authorization", jwt.getType() + " " + jwt.getToken());
                    return execution.execute(request, body);
                }));

    }
}
