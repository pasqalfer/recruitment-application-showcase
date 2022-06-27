package com.iv1201.applicantwebapp.applicantwebapp.controller;

import com.iv1201.applicantwebapp.applicantwebapp.model.Applicant;
import com.iv1201.applicantwebapp.applicantwebapp.model.Application;
import com.iv1201.applicantwebapp.applicantwebapp.model.ApplicationCompetence;
import com.iv1201.applicantwebapp.applicantwebapp.model.Competence;
import com.iv1201.applicantwebapp.applicantwebapp.service.APIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.concurrent.TimeUnit;

/**
 * This controller handles login, log out and registration of a user.
 */
@Controller
@Slf4j
public class ApplicationController {
    private final APIService apiService;

    /**
     * Constructor for application controller.
     * @param apiService Service to contact backend.
     */
    @Autowired
    public ApplicationController(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * When user wants to register application, application object is created and state is set to default (undhandled)
     * and sent to model.
     * @param model Current state of model.
     * @param userToken Cookie string value to authorize user.
     * @return Apply page.
     */
    @GetMapping("/apply")
    public String apply(Model model, @CookieValue(value = "token", required = false) String userToken) throws Exception {
        if(!apiService.clientExists(userToken)){
            return "redirect:/login";
        }

        List<Competence> competenceList = apiService.getPredefinedCompetences(userToken);

        List<ApplicationCompetence> userCompetences = new ArrayList<>();
        for(Competence c : competenceList){
            userCompetences.add(new ApplicationCompetence(c.getCompetence(), 0));
        }

        Application application = new Application();
        application.setState("unhandled");
        application.setCompetences(userCompetences);
        Applicant applicant = apiService.fetchUser(userToken);
        model.addAttribute("user", applicant);
        model.addAttribute("application", application);

        model.addAttribute("predefinedCompetenceList", competenceList);
        return "public/applyPage";
    }


    /**
     * User will first fill in available dates for the application.
     * The dates are validated with help methods.
     * Next step of application submission is to fill in competences.
     * This is saved in application object from current state of model.
     * @param model Current state of model.
     * @param application Current state of application object.
     * @param userToken Cookie string value to authorize user.
     * @return Redirect to next step of application, Application competence submission.
     * @throws ParseException For validating dates.
     */
    @PostMapping("/apply")
    public String registerApplication(RedirectAttributes redirectAttributes, Model model, @ModelAttribute("application") Application application, @CookieValue(value = "token") String userToken) throws ParseException, HttpServerErrorException, Exception {
        if(!apiService.clientExists(userToken)){
            return "redirect:/login";
        }
        // TODO : Validate with backend

        Date availableFrom = application.getDateFrom();
        Date availableTo = application.getDateTo();
        Applicant applicant = apiService.fetchUser(userToken);
        List<Competence> competenceList = apiService.getPredefinedCompetences(userToken);

        model.addAttribute("application", application);
        model.addAttribute("user", applicant);
        model.addAttribute("predefinedCompetenceList", competenceList);

        String message = controlDateDifferences(availableFrom, availableTo, "available");
        if (message.contains("today")) {
            model.addAttribute("message", message);
            log.info("User error: " + message);
            return "public/applyPage";
        } else if (message.contains("time")) {
            model.addAttribute("message", message);
            log.info("User error: " + message);
            return "public/applyPage";
        } else {
            List<ApplicationCompetence> fixed = new ArrayList<>();
            for (ApplicationCompetence c : application.getCompetences()) {
                if (c.getFrom().isEmpty() || c.getTo().isEmpty())
                    continue;
                else {
                    String messageComp = controlDateDifferences(new SimpleDateFormat("yyyy-MM").parse(c.getFrom()), new SimpleDateFormat("yyyy-MM").parse(c.getTo()), "competence");
                    if (messageComp.contains("today")) {
                        String newMessage = messageComp + " for competence \"" + c.getCompetence() + "\"";
                        model.addAttribute("message", newMessage);
                        log.info("User error: " + newMessage);
                        return "public/applyPage";
                    } else if (messageComp.contains("time")) {
                        String newMessage = messageComp + " for competence \"" + c.getCompetence() + "\"";
                        model.addAttribute("message", newMessage);
                        log.info("User error: " + newMessage);
                        return "public/applyPage";
                    } else {
                        fixed.add(new ApplicationCompetence(c.getCompetence(), getYearsOfExperience(c.getFrom(), c.getTo())));
                    }
                }
            }
            application.setCompetences(fixed);
            apiService.applicationRegistration(application, userToken);
        }
        return "redirect:/";
    }

    /**
     * Method checking if the two dates are correct in time,
     * both in respect to each other and also the current date
     * @param availableFrom The starting date
     * @param availableTo The ending date
     * @return The correct message for the check
     * @throws ParseException
     */
    private String controlDateDifferences(Date availableFrom, Date availableTo, String subject) throws ParseException, Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date today = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().format(formatter));
        Long diffFrom = getDateDiff(availableFrom, today, TimeUnit.HOURS);
        Long diffTo = getDateDiff(availableTo, today, TimeUnit.HOURS);
        Long diff = getDateDiff(availableTo, availableFrom, TimeUnit.HOURS);
        String message = "";
        if (subject.contains("available")) {
            if (diffFrom < 10 || diffTo < 10) {
                message = "Available date \"From\" or \"To\" cannot be before today";
                return message;
            }
            else if (diff < 0) {
                message = "Available date \"To\" cannot be before date \"From\" in time";
                return message;
            }
        }
        else if (subject.contains("competence")) {
            if (diffFrom > 0 || diffTo > 10){
                message = "Date \"From\" or \"To\" cannot be after today";
                return message;
            } else if (diff < 0) {
                message = "Date \"To\" cannot be before date \"From\" in time";
                return message;
            }
        }
        return message;
    }

    /**
     * Get the difference between two date
     * @param olderDate the oldest date
     * @param newerDate the newest date
     * @param timeUnit  the unit in which the difference will be returned
     * @return the difference, in the provided unit
     */
    private static long getDateDiff(Date olderDate, Date newerDate, TimeUnit timeUnit) throws Exception{
        long diffInMillies = olderDate.getTime() - newerDate.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Get the amount of time between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @return the difference in months
     * @throws ParseException
     */
    private double getYearsOfExperience(String date1, String date2) throws ParseException, Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date from = sdf.parse(date1);
        Date to = sdf.parse(date2);

        Calendar c = Calendar.getInstance(); //Gregorian calendar
        c.setTimeInMillis(to.getTime() - from.getTime());
        int y = c.get(Calendar.YEAR) - 1970; //starts at 1970
        int m = c.get(Calendar.MONTH);

        double diff = y + m / 12.0;

        return diff;
    }

}
