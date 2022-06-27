package com.iv1201.recruiterwebapp.recruiterwebapp.controller;

import com.iv1201.recruiterwebapp.recruiterwebapp.error.FetchApplicationException;
import com.iv1201.recruiterwebapp.recruiterwebapp.model.Application;
import com.iv1201.recruiterwebapp.recruiterwebapp.model.Competence;
import com.iv1201.recruiterwebapp.recruiterwebapp.model.FilterRequest;
import com.iv1201.recruiterwebapp.recruiterwebapp.repository.ClientRepository;
import com.iv1201.recruiterwebapp.recruiterwebapp.service.APIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This controller handles login and log out of a user.
 */
@Controller
@Slf4j
public class IndexController {
    private final APIService apiService;
    private final ClientRepository clientRepository;

    /**
     * Constructor for index controller.
     * @param apiService Service to contact backend.
     * @param clientRepository Repo to access client template.
     */
    @Autowired
    public IndexController(APIService apiService, ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.apiService = apiService;

        /*log.trace("A TRACE Message");
        log.debug("A DEBUG Message");
        log.info("An INFO Message");
        log.warn("A WARN Message");
        log.error("An ERROR Message");*/
    }

    /**
     * To get index page if user is logged in, else redirect to login page
     * @param userToken Cookie string value to authorize user.
     * @param model The current state of the model.
     * @return Index page or login page.
     */
    @GetMapping("/")
    public String home(@CookieValue(name = "token", required = false) String userToken, Model model) throws URISyntaxException, Exception {
        if(!apiService.clientExists(userToken)){
            log.info("Client not authenticated in \"home\" - redirected to log in");
            return "redirect:/login";
        }

        List<Competence> competenceList  = apiService.getPredefinedCompetences(userToken);
        List<Competence> modifiedCompetenceList = new ArrayList<>();
        List<String> competenceForFilter = new ArrayList<>();
        competenceForFilter.add("lotteries");

        modifiedCompetenceList.add(new Competence("all"));
        modifiedCompetenceList.addAll(competenceList);

        model.addAttribute("competences", modifiedCompetenceList);

        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setCompetence("all");
        filterRequest.setPage(0);
        filterRequest.setSize(10);
        List<Application> applicationList = apiService.findApplicationsByCompetence(userToken,filterRequest);
        model.addAttribute("applicationList", applicationList);
        model.addAttribute("length", applicationList.size());

        return "index";
    }

    /**
     * Get login page for unauthenticated user. If user is already authenticated get to index page.
     * @param userToken Cookie string value to authorize user.
     * @return Login page or index page.
     */
    @GetMapping("/login")
    public String login(@CookieValue(value = "token", required = false) String userToken) throws Exception {
        if(Objects.nonNull(userToken) && apiService.clientExists(userToken)){
            return "redirect:/";
        }
        return "public/loginPage";
    }

    /**
     * POST method for login request handling the input and redirection from the login page.
     * @param model The current state of the model
     * @param userToken The cookie string value for authorization
     * @param username The users input of username
     * @param password The users input of password
     * @param response The HTTP response to add and access cookies
     * @return Login page if user input is invalid and Home page if user is authenticated
     * @throws Exception for an instance of HttpClientErrorException when invalid user input
     */
    @PostMapping("/login")
    public String login(Model model, @CookieValue(value = "token", required = false) String userToken, @RequestParam String username ,@RequestParam String password, HttpServletResponse response) throws HttpClientErrorException, Exception {
        try{
            if(Objects.nonNull(userToken) && apiService.clientExists(userToken)){
                return "redirect:/";
            }

            String token = apiService.usernamePasswordAuthentication(username,password);
            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);
            return "redirect:/";
        } catch (HttpClientErrorException e) {
            String message = "Invalid username/password";
            model.addAttribute("message", message);
            log.error("Message: " + message + " Ex: " + e);
            return "public/loginPage";
        }
    }

    /**
     * To log out and be sent back to login page. UserToken is deleted from Client and Cookies.
     * @param userToken Cookie string value to authorize user.
     * @param response Http response to access and modify cookies.
     * @return The login page.
     */
    @GetMapping("/logout")
    public String logout(@CookieValue(value = "token", required = false) String userToken, HttpServletResponse response) throws Exception {
        clientRepository.delete(userToken);
        Cookie cookie = new Cookie("token", null);
        response.addCookie(cookie);
        return "public/loginPage";
    }

    /**
     * To update list of applications with potential new size, page nr and competence.
     * @param userToken Cookie string value to authorize user.
     * @param competence The competence to filter against.
     * @param size The number of applications to fetch.
     * @param page The page number to be fetched.
     * @param model The current state of the model.
     * @return The updated table-of-applications-fragment.
     */
    @GetMapping("/components/table")
    public String fetchTable(@CookieValue(name = "token") String userToken,
                             @RequestParam(value = "competence", required = false) String competence,
                             @RequestParam(value = "size", required = false) int size,
                             @RequestParam(value = "page", required = false) int page,
                             Model model
    ) throws FetchApplicationException, Exception {
        try{
            FilterRequest filterRequest = new FilterRequest();
            filterRequest.setSize(size);
            filterRequest.setPage(page);
            filterRequest.setCompetence(competence);

            List<Application> applications = apiService.findApplicationsByCompetence(userToken,filterRequest);
            model.addAttribute("applicationList", applications);
            model.addAttribute("competence",competence);
            return "fragments/table :: paginatedTable";
        } catch (FetchApplicationException e) {
            log.error("Ex: " + e);
            return "index";
        }
    }
}

