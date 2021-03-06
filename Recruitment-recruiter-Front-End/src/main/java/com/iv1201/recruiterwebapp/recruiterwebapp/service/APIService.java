package com.iv1201.recruiterwebapp.recruiterwebapp.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iv1201.recruiterwebapp.recruiterwebapp.error.APIException;
import com.iv1201.recruiterwebapp.recruiterwebapp.error.FetchApplicationException;
import com.iv1201.recruiterwebapp.recruiterwebapp.model.*;
import com.iv1201.recruiterwebapp.recruiterwebapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The class handles the API calls to and from the server.
 * The service provides the requested data, as well as sends
 * given data to the server.
 */
@Service
public class APIService {
    private RestTemplate loginTemplate;
    private final ClientRepository clientRepository;
       /* @Value("${back-end.dev-local.api.login.url}")
    private String loginURL;
    @Value("${back-end.dev-local.api.filter.url}")
    private String filterURL;
    @Value("${back-end.dev-local.api.application.url}")
    private String applicationUrl;
    @Value("${back-end.dev-local.api.competence.url}")
    private String competenceURL;
    @Value("${back-end.dev-local.api.update-application.url}")
    private String updateApplicationURL;
    @Value("${back-end.dev-local.api.application-by-competence.url}")
    private String findApplicationByCompetence;
    @Value("${back-end.dev-local}")
    private String host;
*/

    @Value("${back-end.dev-heroku.api.login.url}")
    private String loginURL;
    @Value("${back-end.dev-heroku.api.filter.url}")
    private String filterURL;
    @Value("${back-end.dev-heroku.api.application.url}")
    private String applicationUrl;
    @Value("${back-end.dev-heroku.api.competence.url}")
    private String competenceURL;
    @Value("${back-end.dev-heroku.api.update-application.url}")
    private String updateApplicationURL;
    @Value("${back-end.dev-heroku.api.application-by-competence.url}")
    private String findApplicationByCompetence;
    @Value("${backend.dev-heroku}")
    private String host;

    @Autowired
    public APIService(RestTemplateBuilder restTemplateBuilder, ClientRepository clientRepository) {
        loginTemplate = restTemplateBuilder
                .build();
        this.clientRepository = clientRepository;
    }

    /**
     * Service method to authenticate against the back-end API
     * On success : Returns a session id that maps to the jwt token that is generated by the back-end server
     * On failure : Throws HttpClientErrorException that is handled in Exception handling controller
     * */
    public String usernamePasswordAuthentication(String username, String password){
        String url = loginURL;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = username;
        loginRequest.password = password;
        ResponseEntity<Map>response;

        response = loginTemplate.postForEntity(url,loginRequest,Map.class);

        if(response.getStatusCodeValue() != 200)
        {
            throw new HttpClientErrorException(response.getStatusCode());
        }

        JwtToken jwtToken = JwtToken.builder()
                .token((String) response.getBody().get("token"))
                .type((String) response.getBody().get("type"))
                .build();

        String key = UUID.randomUUID().toString();
        clientRepository.save(key,new ClientTemplate(jwtToken));
        return key;
    }

    /**
     * Service method to get predefined competences from back-end (database)
     * On success : Returns the applicant found
     * If user doesn't exist : Throws HttpClientErrorException that is handled in Exception handling controller
     * On failure : TODO throw exception if getForObject fails? check assert
     * Get list of predefined competences from database.
     * @param clientId user token for authorization.
     * @return list of competences.
     */
    public List<Competence> getPredefinedCompetences(String clientId){
        String url = competenceURL;
        if(!clientRepository.exists(clientId)){
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        } else {
            ClientTemplate clientTemplate = clientRepository.find(clientId);
            Competence[] competences = clientTemplate.getTemplate().getForObject(url, Competence[].class);
            assert competences != null;
            return Arrays.asList(competences);
        }
    }

    /**
     * Find the application based on the application identifier.
     * @param clientId The user identifier
     * @param id The application identifier
     * @return the application
     * @throws URISyntaxException
     */
    public Application findApplicationById(String clientId, long id) throws URISyntaxException {
        String url = String.format(applicationUrl,id);
        URI uri = new URI(url);

        if(Objects.isNull(clientId)){
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseEntity<Application> response = clientRepository.find(clientId).getTemplate().getForEntity(uri, Application.class);

        if(response.getStatusCodeValue() != 200)
        {
            throw new HttpClientErrorException(response.getStatusCode());
        }

        return response.getBody();
    }

    /** Fetch Page of applications filtered by competence
     *  Return paginated result of applications that have a competence present in @param competences
     *  Each page contain 100 elements
     * @param filterRequest contains filtering options for the back-end GET request
     *
     * */
    public List<Application> findApplicationsByCompetence(String clientId, FilterRequest filterRequest) {
        try {
            String cmp = filterRequest.getCompetence();
            String url;
            if(cmp.equals("all")){
                url = String.format(findApplicationByCompetence,filterRequest.getSize(),filterRequest.getPage());
            }
            else{
                String path = UriUtils.encodePath(String.format("/api/users/recruiter/applications/competences/%s/page",cmp), StandardCharsets.UTF_8.toString());
                String params = String.format("?size=%s&page=%s", filterRequest.getSize(),filterRequest.getPage());
                url = host +path + params;
            }
            URI uri = null;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                throw APIException.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .msg("INVALID URI")
                        .cause(e)
                        .build();
            }

            if(Objects.isNull(uri)){
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if(Objects.isNull(clientId)){
                throw APIException.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .msg("Something went wrong on the")
                        .build();
            }

            ResponseEntity<Map> response = clientRepository.find(clientId).getTemplate().getForEntity(uri, Map.class);

            if(response.getStatusCodeValue() != 200)
            {
                throw new HttpClientErrorException(response.getStatusCode());
            }


            List<LinkedHashMap> data = (List<LinkedHashMap>) response.getBody().get("content");

            List<Application> applications = new ArrayList<>();

            data.forEach(o -> {

                List<AvailabilityPeriod> availability = new ArrayList<>();
                List<ApplicationCompetence> competences = new ArrayList<>();

                List lst = mapTo(o.get("competences"),List.class);

                lst.forEach(o1 -> competences.add(mapTo(o1, ApplicationCompetence.class)));

                mapTo(o.get("availability"),List.class).forEach(o1 -> availability.add(mapTo(o1, AvailabilityPeriod.class)));

                Applicant applicant = mapTo(o.get("applicant"), Applicant.class);

                AvailabilityPeriod availabilityPeriod;
                String state = (String) o.get("state");
                Integer id = (int) o.get("id");
                Integer version = (int) o.get("version");

                Application app = Application.builder()
                        .availability(availability)
                        .competences(competences)
                        .id(id)
                        .state(state)
                        .version(version)
                        .build();
                app.setApplicant(applicant);
                applications.add(app);
            });

            return applications;

        } catch (Exception e){
            throw new FetchApplicationException("Failed to fetch applications", e);
        }

    }

    public static <T> T mapTo(Object map, Class<T> tClass){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T t = mapper.convertValue(map, tClass);
        return t;
    }

    /**
     * Updating the state of the application and based on version check conflicts for two users updating
     * the same application.
     * @param clientId The user identifier
     * @param applicationId The application id
     * @param version The current version if another user/recruiter is viewing same app
     * @param newState The new state for the application
     * @return the application
     * @throws URISyntaxException
     */
    public Application updateApplicationState(String clientId, long applicationId, long version, String newState) throws URISyntaxException {
        String url = String.format(updateApplicationURL,applicationId);
        URI uri = new URI(url);

        if(Objects.isNull(clientId)){
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // TODO : error handling
        Map<String, Object> body = new HashMap<>();

        body.put("newState", newState);
        body.put("version", version);

        ResponseEntity<Application> response = clientRepository.find(clientId).getTemplate().postForEntity(uri,body,Application.class);
        if(!response.getStatusCode().is2xxSuccessful()){
            System.out.println("APIService: Error in status when updating state");
            throw new HttpClientErrorException(response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Service method to find client by userToken
     * On success : True, if user was found. False, if user was not found.
     * On failure : False, if user was null.
     * */
    public boolean clientExists(String clientId){
        if(Objects.isNull(clientId)){
            return false;
        }
        return clientRepository.exists(clientId);
    }
}
