package fr.mgargadennec.blossom.samples;

import fr.mgargadennec.blossom.core.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by maelg on 05/05/2017.
 */
@Controller
@RequestMapping
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("users", userService.getAll());
        return "home";
    }
}
