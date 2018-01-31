package fr.blossom.initializr;

import javax.servlet.http.HttpServletResponse;

import fr.blossom.initializr.enums.PACKAGING_MODE;
import fr.blossom.initializr.enums.SOURCE_LANGUAGE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
@Controller
@RequestMapping("/initializr")
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
    model.addAttribute("project", new ProjectConfiguration("blossom-starter-basic"));
    model.addAttribute("initializr", initializr);
    model.addAttribute("packagingModes", PACKAGING_MODE.values());
    model.addAttribute("sourceLanguages", SOURCE_LANGUAGE.values());

    return new ModelAndView("initializr");
  }

  @PostMapping(produces = "application/zip", value="/initializr")
  public void generate(@ModelAttribute("form") ProjectConfiguration projectConfiguration,
    HttpServletResponse res)
    throws Exception {
    if (projectConfiguration.getDependencies().size() == 0) {
      throw new RuntimeException("");
    }
    res.addHeader(HttpHeaders.CONTENT_DISPOSITION,
      "attachment; filename=\"" + projectConfiguration.getArtifactId() + ".zip\"");
    projectGenerator.generateProject(projectConfiguration, res.getOutputStream());
  }
}
