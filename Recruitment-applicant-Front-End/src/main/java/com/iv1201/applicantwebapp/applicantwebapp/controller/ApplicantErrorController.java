package com.iv1201.applicantwebapp.applicantwebapp.controller;

import com.iv1201.applicantwebapp.applicantwebapp.repository.ClientRepository;
import com.iv1201.applicantwebapp.applicantwebapp.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles the default error page.
 */
@Controller
public class ApplicantErrorController implements ErrorController {
    private final APIService apiService;
    private final ClientRepository clientRepository;

    /**
     * Constructor for error controller.
     * @param apiService Service to contact backend.
     * @param clientRepository Repo to acces client template.
     */
    @Autowired
    public ApplicantErrorController(APIService apiService, ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.apiService = apiService;
    }

    /**
     * Get error page. When something wrong.
     * @param model The current state of the model.
     * @return Error page.
     */
    @GetMapping("/error")
    public String error(Model model, @ModelAttribute("message") String message){
        //String message = (String) model.getAttribute("message");
        //model.addAttribute("message", message);
        return "public/errorPage";
    }
}
