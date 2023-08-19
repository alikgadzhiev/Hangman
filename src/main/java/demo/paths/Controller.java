package demo.paths;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/main/{username}")
    public String main(@PathVariable("username") String username, Model model){
        model.addAttribute("username", username);
        return "main";
    }

    @GetMapping("/newPassword/{username}")
    public String newPassword(@PathVariable("username") String username, Model model){
        model.addAttribute("username", username);
        return "newPassword";
    }

    @GetMapping("/room/{username}/{room}")
    public String room(@PathVariable("username") String username,
                       @PathVariable("room") String room,  Model model){
        model.addAttribute("username", username);
        model.addAttribute("room", room);
        return "room";
    }

    @GetMapping("/game/{room}")
    public String game(@PathVariable("room") String room, Model model){
        model.addAttribute("room", room);
        return "game";
    }
}
