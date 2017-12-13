package fr.blossom.ui.web.administration.user;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.user.User;
import fr.blossom.core.user.UserCreateForm;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.core.user.UserUpdateForm;
import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@BlossomController
@RequestMapping("/administration/users")
@OpenedMenu("users")
public class UsersController {

  private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

  private final UserService userService;
  private final SearchEngineImpl<UserDTO> searchEngine;

  public UsersController(UserService userService, SearchEngineImpl<UserDTO> searchEngine) {
    this.userService = userService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('administration:users:read')")
  public ModelAndView getUsersPage(@RequestParam(value = "q", required = false) String q,
    @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "users/users");
  }

  private ModelAndView tableView(String q, Pageable pageable, Model model, String viewName) {
    Page<UserDTO> users;

    if (Strings.isNullOrEmpty(q)) {
      users = this.userService.getAll(pageable);
    } else {
      users = this.searchEngine.search(q, pageable).getPage();
    }

    model.addAttribute("users", users);
    model.addAttribute("q", q);

    return new ModelAndView(viewName, model.asMap());
  }

  @GetMapping("/_create")
  @PreAuthorize("hasAuthority('administration:users:create')")
  public ModelAndView getUserCreatePage(Model model, Locale locale) {
    UserCreateForm userCreateForm = new UserCreateForm();
    userCreateForm.setLocale(locale);
    return this.createView(userCreateForm, model);
  }

  @PostMapping("/_create")
  @PreAuthorize("hasAuthority('administration:users:create')")
  public ModelAndView handleUserCreateForm(
    @Valid @ModelAttribute("userCreateForm") UserCreateForm userCreateForm,
    BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return this.createView(userCreateForm, model);
    }
    try {
      UserDTO user = this.userService.create(userCreateForm);
      return new ModelAndView("redirect:../users/" + user.getId());
    } catch (Exception e) {
      return this.createView(userCreateForm, model);
    }
  }

  private ModelAndView createView(UserCreateForm userCreateForm, Model model) {
    model.addAttribute("userCreateForm", userCreateForm);
    model.addAttribute("civilities", User.Civility.values());
    return new ModelAndView("users/create", model.asMap());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:users:read')")
  public ModelAndView getUser(@PathVariable Long id, Model model, HttpServletRequest request) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    model.addAttribute("user", user);
    return new ModelAndView("users/user", "user", user);
  }

  @GetMapping("/{id}/_informations")
  @PreAuthorize("hasAuthority('administration:users:read')")
  public ModelAndView getUserInformations(@PathVariable Long id) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    return this.viewUserInformationView(user);
  }

  @GetMapping("/{id}/_informations/_edit")
  @PreAuthorize("hasAuthority('administration:users:write')")
  public ModelAndView getUserInformationsForm(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    return this.updateUserInformationView(new UserUpdateForm(user), user, model);
  }

  @PostMapping("/{id}/_informations/_edit")
  @PreAuthorize("hasAuthority('administration:users:write')")
  public ModelAndView handleUserInformationsUpdateForm(@PathVariable Long id,
    @Valid @ModelAttribute("userUpdateForm") UserUpdateForm userUpdateForm,
    BindingResult bindingResult, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }

    if (bindingResult.hasErrors()) {
      return this.updateUserInformationView(userUpdateForm, user, model);
    }

    UserDTO updatedUser = this.userService.update(id, userUpdateForm);
    return this.viewUserInformationView(updatedUser);
  }

  private ModelAndView viewUserInformationView(UserDTO user) {
    return new ModelAndView("users/userinformations", "user", user);
  }

  private ModelAndView updateUserInformationView(UserUpdateForm userUpdateForm, UserDTO user,
    Model model) {
    model.addAttribute("userUpdateForm", userUpdateForm);
    model.addAttribute("user", user);
    model.addAttribute("civilities", User.Civility.values());
    return new ModelAndView("users/userinformations-edit", model.asMap());
  }

  @GetMapping(value = "/{id}/avatar", produces = "image/*")
  @ResponseBody
  public byte[] displayAvatar(@PathVariable Long id) throws IOException {
    return this.userService.loadAvatar(id);
  }

  @GetMapping("/{id}/_avatar/_edit")
  @PreAuthorize("hasAuthority('administration:users:write')")
  public ModelAndView getUserAvatarForm(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    return new ModelAndView("users/useravatar-edit-modal", "user", user);
  }

  @PostMapping("/{id}/_avatar/_edit")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('administration:users:write')")
  public void handleUserAvatarUpdateForm(@PathVariable Long id,
    @RequestParam("avatar") MultipartFile file)
    throws IOException {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    this.userService.updateAvatar(id, file.getBytes());
  }

  @PostMapping("/{id}/_delete")
  @PreAuthorize("hasAuthority('administration:users:delete')")
  @ResponseBody
  public ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> deleteUser(
    @PathVariable Long id,
    @RequestParam(value = "force", required = false, defaultValue = "false") Boolean force) {
    Optional<Map<Class<? extends AbstractDTO>, Long>> result = this.userService.delete(this.userService.getOne(id), force);

    if(!result.isPresent() || result.get().isEmpty()){
      return new ResponseEntity<>(Maps.newHashMap(), HttpStatus.OK);
    }else{
      return new ResponseEntity<>(result.get(), HttpStatus.CONFLICT);
    }
  }
}
