package com.iv1201.recruiterwebapp.recruiterwebapp.model;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * This class is a client template where the users jwt token is stored.
 */
public class ClientTemplate  {
    private final JwtToken jwtToken;

    /**
     * Constructor for client template.
     * @param jwtToken A token representing claims (username and role?) to be transferred between components.
     */
    public ClientTemplate(JwtToken jwtToken){
        this.jwtToken = jwtToken;
    }

    /**
     * To send an authorization request to backend.
     * @return Response from backend.
     */
    public RestTemplate getTemplate(){
        RestTemplate restTemplate = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", jwtToken.getType() + " " +  jwtToken.getToken());
            return execution.execute(request, body);
        })).build();
        /*final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);

        restTemplate.setRequestFactory(requestFactory);*/
        return restTemplate;
    }
}

