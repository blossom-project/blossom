package fr.mgargadennec.blossom.initializr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
@Controller
public class InitializrController {
  private final Initializr initializr;
  private final ProjectGenerator projectGenerator;

  @Autowired
  public InitializrController(Initializr initializr, ProjectGenerator projectGenerator) {
    this.initializr = initializr;
    this.projectGenerator = projectGenerator;
  }


  @GetMapping
  public ModelAndView main(Model model) {
    model.addAttribute("project", new ProjectConfiguration());
    model.addAttribute("initializr", initializr);

    return new ModelAndView("main");
  }

  @PostMapping(produces = "application/zip")
  public void  generate(@ModelAttribute("form") ProjectConfiguration projectConfiguration, HttpServletResponse res) throws Exception {
    System.out.println("test"+ projectConfiguration);
    if(projectConfiguration.getDependencies().size() == 0){
      throw new RuntimeException("");
    }

    res.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+projectConfiguration.getArtifactId()+".zip\"");
    projectGenerator.generateProject(projectConfiguration,res.getOutputStream());
  }
}
