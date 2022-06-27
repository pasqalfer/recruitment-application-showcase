package musiccloud.api.websocket.controller;

import musiccloud.api.websocket.entity.AppUser;
import musiccloud.api.websocket.entity.AudioFile;
import musiccloud.api.websocket.model.UserDTO;
import musiccloud.api.websocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private UserService userService;

    @Autowired
    public SearchController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<Object> searchUser(@RequestParam String name){
        List<AppUser> users = userService.searchByName(name);
        List<Object> objects = new ArrayList<>();

        for(AppUser a : users)
            objects.add(new UserDTO(a));

        return objects;
    }

    @GetMapping("/song")
    public List<Object> searchSong(@RequestParam String name){
        List<AppUser> users = userService.searchByName(name);
        List<Object> objects = new ArrayList<>();

        for(AppUser a : users)
            objects.add(new UserDTO(a));

        return objects;
    }

    @GetMapping("/playlist")
    public List<Object> searchPlaylist(@RequestParam String name){
        List<AppUser> users = userService.searchByName(name);
        List<Object> objects = new ArrayList<>();

        for(AppUser a : users)
            objects.add(new UserDTO(a));

        return objects;
    }



}
