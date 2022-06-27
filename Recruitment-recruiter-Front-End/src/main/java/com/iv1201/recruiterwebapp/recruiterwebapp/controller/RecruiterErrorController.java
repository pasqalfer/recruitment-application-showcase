package com.iv1201.recruiterwebapp.recruiterwebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * This controller handles the default error page.
 */
@Controller
public class RecruiterErrorController implements ErrorController {

    /**
     * Constructor for error controller.
     */
    @Autowired
    public RecruiterErrorController() {
    }

    /**
     * Get error page. When something wrong.
     * @return Error page.
     */
    @GetMapping("/error")
    public String error(@ModelAttribute("message") String message, HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // handle HTTP 404 Not Found error
                return "error/404";
            }
        }

        return "public/errorPage";
    }
}
