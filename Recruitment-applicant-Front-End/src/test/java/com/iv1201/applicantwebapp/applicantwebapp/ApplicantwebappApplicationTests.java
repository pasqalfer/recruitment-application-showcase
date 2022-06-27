package com.iv1201.applicantwebapp.applicantwebapp;

import com.iv1201.applicantwebapp.applicantwebapp.controller.IndexController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest
class ApplicantwebappApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private IndexController indexController;
    private TestRestTemplate restTemplate;

    //@Test
    public void contextLoads() {
        assertThat(indexController).isNotNull();
    }

    //@Test
    public void login() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/login",
                String.class)).contains("public/loginPage");
    }

}
