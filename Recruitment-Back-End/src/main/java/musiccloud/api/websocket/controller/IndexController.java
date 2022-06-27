package musiccloud.api.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.Playlist;
import musiccloud.api.websocket.model.UserDTO;
import musiccloud.api.websocket.service.SecurityService;
import musiccloud.api.websocket.service.UserDetailsServiceImpl;
import musiccloud.api.websocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Objects;


@Controller
public class IndexController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final SecurityService securityService;


    @Autowired
    public IndexController(UserDetailsServiceImpl userDetailsService, UserService userService, SecurityService securityService){
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping(value = {"/"})
    public String index(Model model, Principal principal){
        model.addAttribute("authenticatedUser", principal.getName());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userForm", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("userForm") AppUser userForm, BindingResult bindingResult) {
        userService.register(AppUser.builder().email(userForm.getEmail()).username(userForm.getUsername()).password(userForm.getPassword()).build());
        securityService.autoLogin(userForm.getUsername(), userForm.getPassword());
        return "redirect:/login";
    }

    @GetMapping("/playlist/{id}")
    public String playlist(Principal principal, Model model, @PathVariable("id") String id) {
        Playlist playlist = userService.findUserPlaylistById(id);
        if(playlist != null){
            model.addAttribute("playlist",playlist);
        }
        model.addAttribute("currentUser", principal.getName());
        return "playlist";
    }


    @GetMapping("/profile/{username}")
    public String profile(Principal principal, Model model, @PathVariable("username") String username) {
        AppUser appUser = userService.findByUsername(username);
        if(appUser != null){
            model.addAttribute("profile", new UserDTO(appUser));
        }
        model.addAttribute("currentUser", principal.getName());
        return "pages/profile";
    }

    @GetMapping("/player")
    public String player() {
        return "player/player";
    }
}
