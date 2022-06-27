package com.iv1201.recruitmentapplication.web;

import com.iv1201.recruitmentapplication.TestUtil;
import com.iv1201.recruitmentapplication.model.payload.response.JwtResponse;
import com.iv1201.recruitmentapplication.util.DTOUtil;
import com.iv1201.recruitmentapplication.model.payload.request.LoginRequest;
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

// TODO : Method that asserts that authenticated Recruiter can not authenticate as Applicant
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicantAuthenticationTest {
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
    public void testLoginSuccessResponseCode(){
        URI uri = TestUtil.localURI(port, "api/users/applicant/authenticate");
        LoginRequest loginRequest = new LoginRequest("user", "123456");
        ResponseEntity<Object> response = restTemplate.postForEntity(uri,loginRequest, Object.class);
        Assertions.assertEquals(response.getStatusCodeValue(),200);
    }

    /**
     * Assert that correct response code is returned from the http response
     *
     * Expected response : 403 "Bad Credentials"
     */
    @Test
    public void testLoginWrongPasswordResponseCode(){
        URI uri = TestUtil.localURI(port, "api/users/applicant/authenticate");
        LoginRequest loginRequest = new LoginRequest("user", "123457");
        ResponseEntity<Object> response = restTemplate.postForEntity(uri,loginRequest, Object.class);
        Assertions.assertEquals(response.getStatusCodeValue(), 403);
    }


    /**
     * Assert that correct response code is returned from the http response when applicant tries to
     * authenticate as a recruiter.
     *
     *
     * Expected response : 403 "Bad Credentials"
     */
    @Test
    public void testLoginWrongEndpoint(){
        URI uri = TestUtil.localURI(port, "api/users/recruiter/authenticate");
        LoginRequest loginRequest = new LoginRequest("user", "123456");
        ResponseEntity<Object> response = restTemplate.postForEntity(uri,loginRequest, Object.class);
        Assertions.assertEquals(response.getStatusCodeValue(), 403);
    }



    /**
     * Assert that correct response body is returned from the http response
     *
     * Expected response body : <code>{@link com.iv1201.recruitmentapplication.model.payload.response.JwtResponse}</code>
     */
    @Test
    public void testLoginSuccessBody(){
        URI uri = TestUtil.localURI(port, "api/users/applicant/authenticate");
        LoginRequest loginRequest = new LoginRequest("user", "123456");
        ResponseEntity<Object> response = restTemplate.postForEntity(uri,loginRequest, Object.class);
        JwtResponse jwtResponse = dtoUtil.fromMap(response.getBody(), JwtResponse.class);
        Assertions.assertEquals(jwtResponse.getClass(), JwtResponse.class);
    }


    /**
     * Assert that roles in response body are correct
     *
     * Expected role in response body : "ROLE_APPLICANT"
     */
    @Test
    public void testIsApplicant(){
        URI uri = TestUtil.localURI(port, "api/users/applicant/authenticate");
        LoginRequest loginRequest = new LoginRequest("user", "123456");
        ResponseEntity<Object> response = restTemplate.postForEntity(uri,loginRequest, Object.class);
        JwtResponse jwtResponse = dtoUtil.fromMap(response.getBody(), JwtResponse.class);
        boolean hasRole = TestUtil.collectionContains(jwtResponse.getRoles(), "ROLE_APPLICANT");
        Assertions.assertTrue(hasRole);
    }

    /**
     * Assert that roles in response body are correct
     *
     * Expected roles in response body : "ROLE_APPLICANT"
     */
    @Test
    public void testNotIsRecruiter(){
        URI uri = TestUtil.localURI(port, "api/users/applicant/authenticate");
        LoginRequest loginRequest = new LoginRequest("user", "123456");
        ResponseEntity<Object> response = restTemplate.postForEntity(uri,loginRequest, Object.class);
        JwtResponse jwtResponse = dtoUtil.fromMap(response.getBody(), JwtResponse.class);
        boolean hasRole = TestUtil.collectionContains(jwtResponse.getRoles(), "ROLE_RECRUITER");
        Assertions.assertFalse(hasRole);
    }

    /**
     * Assert that logged in applicant is authorized
     *
     */
    @Test
    public void testIsAuthorized(){
        URI uri = TestUtil.localURI(port, "api/users/applicant/authenticate");
        LoginRequest loginRequest = new LoginRequest("user", "123456");
        ResponseEntity<Object> response = restTemplate.postForEntity(uri,loginRequest, Object.class);
        JwtResponse jwt = dtoUtil.fromMap(response.getBody(), JwtResponse.class);
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Authorization", jwt.getType() + " " + jwt.getToken());
                    return execution.execute(request, body);
                }));
        URI uri2 = TestUtil.localURI(port, "api/users/applicant/application");
        ResponseEntity<Object> responseEntity  = restTemplate.getForEntity(uri2, Object.class);
        Assertions.assertEquals(responseEntity.getStatusCodeValue(),200);
    }


}

