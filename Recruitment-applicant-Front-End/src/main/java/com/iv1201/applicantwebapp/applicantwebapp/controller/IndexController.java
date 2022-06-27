package com.iv1201.applicantwebapp.applicantwebapp.controller;

import com.iv1201.applicantwebapp.applicantwebapp.model.*;
import com.iv1201.applicantwebapp.applicantwebapp.repository.ClientRepository;
import com.iv1201.applicantwebapp.applicantwebapp.service.APIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * This controller handles login, log out and registration of a user.
 */

@Controller
@Slf4j
public class IndexController {
    private final APIService apiService;
    private final ClientRepository clientRepository;

    /**
     * Constructor for index controller.
     * @param apiService Service to contact backend.
     * @param clientRepository Repo to acces client template.
     */
    @Autowired
    public IndexController(APIService apiService, ClientRepository clientRepository){
        this.clientRepository = clientRepository;
        this.apiService = apiService;
    }

    /**
     * To get index page if user is logged in, else redirect to login page
     * @param userToken Cookie string value to authorize user.
     * @param model The current state of the model.
     * @return Index page or login page.
     */
    @GetMapping("/")
    public String home(@CookieValue(name = "token", required = false) String userToken, Model model) throws Exception {
        if(!apiService.clientExists(userToken)){
            log.info("Client not authenticated in \"home\" - redirected to log in");
            return "redirect:/login";
        }
        Applicant applicant = apiService.fetchUser(userToken);

        if (!ObjectUtils.isEmpty(applicant.getApplication())) {
            model.addAttribute("app", applicant.getApplication());
        }

        model.addAttribute("user", applicant);

        return "index";
    }


    /**
     * Get login page for unauthenticated user. If user is already authenticated get to index page.
     * @param userToken Cookie string value to authorize user.
     * @return Login page or index page.
     */
    @GetMapping("/login")
    public String login(@CookieValue(value = "token", required = false) String userToken) throws Exception{
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
     * Get register page. Create register request and send as attribute to model.
     * @param model The current state of the model.
     * @return Register page.
     */
    @GetMapping("/register")
    public String register(Model model) throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        model.addAttribute("req", registerRequest);
        return "public/registerPage";
    }

    /**
     * POST method for the user registration page when filled out by user, to save the registration
     * @param registerRequest
     * @return Login page
     */
    @PostMapping("/register")
    public String registerUser(Model model, @ModelAttribute("req") RegisterRequest registerRequest) throws HttpClientErrorException {
        try {
            apiService.registerUser(registerRequest);
            return "redirect:/";
        } catch (HttpClientErrorException e) {
            String message = "User already exists";

            //TODO: all combinations?
            if(e.getLocalizedMessage().contains("email already exists") && e.getLocalizedMessage().contains("username already exists") && e.getLocalizedMessage().contains("pnr already exists"))
                message = "User with " + registerRequest.email + " as email and " + registerRequest.username + "as username and " + registerRequest.pnr + "as social security number already exists";
            else if(e.getLocalizedMessage().contains("pnr already exists"))
                message = "User with " + registerRequest.pnr + " as social security number already exists";
            else if(e.getLocalizedMessage().contains("email already exists"))
                message = "User with " + registerRequest.email + " as email already exists";
            else if(e.getLocalizedMessage().contains("username already exists"))
                message = "User with " + registerRequest.username + " as username already exists";

            model.addAttribute("message", message);
            log.error("Message: " + message + " Ex: " + e);
            return "public/registerPage";
        }
    }

    /**
     * Get new password page. Create new password request and send as attribute to model.
     * @param model The current state of the model.
     * @return New password page.
     */
    @GetMapping("/newPassword")
    public String getNewPassword(Model model) throws Exception {
        NewPasswordRequest passwordRequest = new NewPasswordRequest();
        model.addAttribute("req", passwordRequest);
        return "public/newPasswordPage";
    }

    /**
     * POST method for the user new password page when filled out by user, to check creds against database.
     * @param passwordRequest The request containing user creds to be sent to backend.
     * @return Login page
     */
    @PostMapping("/newPassword")
    public String registerPassword(Model model, @ModelAttribute("req") NewPasswordRequest passwordRequest) throws HttpClientErrorException{
        if(passwordRequest.email.isEmpty() || passwordRequest.pnr.isEmpty()){
            model.addAttribute("req", passwordRequest);
            return "public/newPasswordPage";
        }
        try{
            apiService.getNewPassword(passwordRequest);

        } catch(HttpServerErrorException.InternalServerError e){
            if(e.getMessage().contains("user already has password")){
                String message = "You already have a password";
                model.addAttribute("newPasswordMessage", message);
                return "public/loginPage";
            }
        } catch (HttpClientErrorException e) {
            String message = "Invalid e-mail/social secruity number";
            model.addAttribute("message", message);
            log.error("Message: " + message + " Ex: " + e);
            return "public/newPasswordPage";
        }

        String message = "An e-mail has been sent to you";
        model.addAttribute("newPasswordMessage", message);
        return "public/loginPage";
    }

    /**
     * GET update password page if token is valid.
     * @param userToken Cookie string value to authorize user.
     * @return The login page.
     */
    @GetMapping("/updatePassword")
    public String getUpdatePassword(@CookieValue(value = "token", required = false) String userToken, @RequestParam String token, Model model) throws Exception {
        if(Objects.nonNull(userToken) && apiService.clientExists(userToken)){
            return "redirect:/";
        }
        try{
            apiService.validatePasswordToken(token);
            model.addAttribute("token", token);
            return "public/resetPasswordPage";
        }catch (HttpClientErrorException e) {
            String message = "Invalid URL";
            model.addAttribute("newPasswordMessage", message);
            log.error("Message: " + message + " Ex: " + e);
            return "public/loginPage";
        }
    }

    /**
     * POST to update users password.
     * @param userToken Cookie string value to authorize user if it's already logged in.
     * @return The login page.
     */
    @PostMapping("/updatePassword/{token}")
    public String postUpdatePassword(@CookieValue(value = "token", required = false) String userToken, @RequestParam String password, @PathVariable String token, Model model) throws Exception {
        if(Objects.nonNull(userToken) && apiService.clientExists(userToken)){
            return "redirect:/";
        }
        try{
            apiService.updatePassword(token, password);
            String message = "Your new password is set!";
            model.addAttribute("newPasswordMessage", message);
            return "public/loginPage";
        }catch (HttpClientErrorException e) {
            String message = "Unable to reset password";
            model.addAttribute("newPasswordMessage", message);
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
}
