package fr.mgargadennec.blossom.ui.web.administration.user;

import com.google.common.base.Strings;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.NoSuchElementException;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@BlossomController("/administration/users")
public class UsersController {

  private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

  private final UserService userService;
  private final SearchEngineImpl<UserDTO> searchEngine;

  public UsersController(UserService userService, SearchEngineImpl<UserDTO> searchEngine) {
    this.userService = userService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  public ModelAndView getUsersPage(@RequestParam(value = "q", required = false) String q, @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "users/users");
  }

  @GetMapping("/_list")
  public ModelAndView getUsersTable(@RequestParam(value = "q", required = false) String q, @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "users/table");
  }

  private ModelAndView tableView(String q, Pageable pageable, Model model, String viewName) {
    Page<UserDTO> users;

    if (Strings.isNullOrEmpty(q)) {
      users = this.userService.getAll(pageable);
    } else {
      users = this.searchEngine.search(q, pageable);
    }

    model.addAttribute("users", users);
    model.addAttribute("q", q);

    return new ModelAndView(viewName, model.asMap());
  }

  @GetMapping("/_create")
  public ModelAndView getUserCreatePage() {
    return this.createView(new UserDTO());
  }

  @PostMapping("/_create")
  public ModelAndView handleUserCreateForm(@Valid @ModelAttribute("user") UserDTO user) {
    try {
      user = this.userService.create(user);
    } catch (DataIntegrityViolationException e) {
      logger.error("Error on creating user, login " + user.getIdentifier() + " already exists ", e);
      return this.createView(user);
    }
    return new ModelAndView("redirect:/users/" + user.getId());
  }

  private ModelAndView createView(UserDTO user) {
    return new ModelAndView("users/create", "user", user);
  }


  @GetMapping("/{id}")
  public ModelAndView getUser(@PathVariable Long id, Model model, HttpServletRequest request) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    model.addAttribute("user", user);
    return new ModelAndView("users/user", "user", user);
  }


  @PostMapping("/{id}/_delete")
  public String deleteUser(@PathVariable Long id) {
    this.userService.delete(this.userService.getOne(id));
    return "redirect:/users";
  }

  @GetMapping("/{id}/_edit")
  public ModelAndView getUserUpdatePage(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }

    return this.editView(user);
  }

  @PostMapping("/{id}/_edit")
  public ModelAndView handleUpdateUserForm(@PathVariable Long id, @Valid @ModelAttribute("user") UserDTO user) {
    try {
      this.userService.update(id, user);
    } catch (Exception e) {
      return this.editView(user);
    }
    return new ModelAndView("redirect:/users/" + id);
  }

  private ModelAndView editView(UserDTO user) {
    return new ModelAndView("users/update", "user", user);
  }


  @PostMapping("/{id}/_change_password")
  public String askForPasswordChange(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    this.userService.askPasswordChange(id);
    redirectAttributes.addAttribute("reseted", true);
    return "redirect:/users/" + id;
  }

  @PostMapping("/{id}/_enable")
  public String enableUser(@PathVariable Long id) {
    this.userService.updateActivation(id, true);
    return "redirect:/users/" + id;
  }

  @PostMapping("/{id}/_disable")
  public String disableUser(@PathVariable Long id) {
    this.userService.updateActivation(id, false);
    return "redirect:/users/" + id;
  }

}

