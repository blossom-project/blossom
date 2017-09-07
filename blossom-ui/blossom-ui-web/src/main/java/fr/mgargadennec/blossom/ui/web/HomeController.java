package fr.mgargadennec.blossom.ui.web;

import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@BlossomController
@OpenedMenu("home")
public class HomeController {

    @RequestMapping
    public ModelAndView home() {
        return new ModelAndView("home/home");
    }
}
