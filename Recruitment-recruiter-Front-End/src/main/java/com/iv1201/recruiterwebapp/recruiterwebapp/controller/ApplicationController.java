package com.iv1201.recruiterwebapp.recruiterwebapp.controller;


import com.iv1201.recruiterwebapp.recruiterwebapp.model.Application;
import com.iv1201.recruiterwebapp.recruiterwebapp.model.FilterRequest;
import com.iv1201.recruiterwebapp.recruiterwebapp.service.APIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * This controller handles  application logic; state changes on applications and filtering.
 */
@Controller
@Slf4j
public class ApplicationController {
    private final APIService apiService;

    /**
     * Constructor for application controller.
     */
    @Autowired
    public ApplicationController(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * When user/recruiter wants to filter through all applications and not only visible applications
     * on one page. The form is sent to backend to receive an already filtered list of applications.
     * @param userToken Cookie string value to authorize user.
     * @param model Current state of model.
     * @param filterRequest The elements to filter the application by from backend.
     * @return Filter applications page.
     * @throws ParseException
     */
    @PostMapping("/filter")
    public String filterApplications(@CookieValue(value = "token") String userToken, Model model, @ModelAttribute("filter") FilterRequest filterRequest) throws Exception {
        if(!apiService.clientExists(userToken)){
            return "redirect:/login";
        }

        return "index";
    }

    /**
     * When an application is chosen from the list, it is redirected to a new page to visualize
     * the application alone through its id.
     * @param userToken Cookie string value to authorize user.
     * @param model Current state of model.
     * @param id Application identifier
     * @return Application Page.
     * @throws ParseException
     */
    @GetMapping("/application/{id}")
    public String application(@CookieValue(name = "token") String userToken, Model model, @PathVariable Integer id) throws ParseException, URISyntaxException, Exception {
        if(!apiService.clientExists(userToken)){
            return "redirect:/login";
        }

        //TODO: get application from backend when backend is fixed for availability object
        Application app = apiService.findApplicationById(userToken, id);

        model.addAttribute("app", app);
        model.addAttribute("applicant", app.getApplicant());

        return "public/applicationPage";
    }

    /**
     * When the user/recruiter want to change the state of an application from unhandled,
     * the new chosen state is set for the application.
     * @param userToken Cookie string value to authorize user.
     * @param model Current state of model.
     * @param id Application identifier
     * @param newState The new state to set on the application.
     * @return Application page.
     * @throws ParseException
     */
    @PostMapping("/application/{id}/{version}/updateState")
    public String applicationStateChange(@CookieValue(name = "token") String userToken, Model model,@PathVariable Integer id, @RequestParam String newState, @RequestParam String username, @RequestParam String password, @PathVariable int version) throws URISyntaxException, Exception {
        if(!apiService.clientExists(userToken)){
            return "redirect:/login";
        }

        Application app = apiService.findApplicationById(userToken, id);

        try{
            apiService.usernamePasswordAuthentication(username,password);
            if(app.getVersion() != version){
                String message = "The application has been updated since you started editing.\r\nThe application page has been updated";
                model.addAttribute("errorMsg", message);
            } else {
                app = apiService.updateApplicationState(userToken, id, app.getVersion(), newState);
                String message = "The state was successfully changed!";
                model.addAttribute("successMsg", message);
            }

            model.addAttribute("app", app);
            model.addAttribute("applicant", app.getApplicant());

            return "public/applicationPage";
        } catch (HttpClientErrorException e) {
            String message = "Could not change state of application: Invalid username/password";
            model.addAttribute("errorMsg", message);
            model.addAttribute("app", app);
            model.addAttribute("applicant", app.getApplicant());
            return "public/applicationPage";
        }
    }
}

