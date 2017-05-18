package fr.mgargadennec.blossom.ui.web;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;

@BlossomController
public class HomeController {

    @RequestMapping
    public ModelAndView home() {
        return new ModelAndView("home/home");
    }
}
