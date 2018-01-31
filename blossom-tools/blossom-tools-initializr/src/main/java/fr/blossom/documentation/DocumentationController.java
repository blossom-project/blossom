package fr.blossom.documentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/documentation")
public class DocumentationController
{

  @GetMapping
  public ModelAndView main(Model model) {
    return new ModelAndView("documentation");
  }

}
