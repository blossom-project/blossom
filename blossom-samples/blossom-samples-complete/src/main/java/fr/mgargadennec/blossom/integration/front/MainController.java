package fr.mgargadennec.blossom.integration.front;

import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.role.RoleService;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Random;

/**
 * Created by maelg on 05/05/2017.
 */
@Controller
@RequestMapping
public class MainController {
    @Autowired
    private Random random;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private GroupService groupService;

    @GetMapping
    public String home(Model model, HttpServletResponse response) throws InterruptedException {
        long sleep = (long) (random.nextGaussian() * 1000f);
        if (sleep > 0)
            Thread.sleep(sleep);

        if (random.nextDouble() > 0.8d) {
            response.setStatus(HttpStatus.values()[random.nextInt(HttpStatus.values().length)].value());
        }

        model.addAttribute("users", userService.getAll());
        return "home";
    }

    @GetMapping("/users")
    @ResponseBody
    public List<UserDTO> users() {
        return userService.getAll();
    }

    @GetMapping("/roles")
    @ResponseBody
    public List<RoleDTO> roles() {
        return roleService.getAll();
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<GroupDTO> groups() {
        return groupService.getAll();
    }
}
