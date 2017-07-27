package fr.mgargadennec.blossom.ui.web.administration.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Strings;

import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupDTO;
import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupService;
import fr.mgargadennec.blossom.core.association_user_role.AssociationUserRoleDTO;
import fr.mgargadennec.blossom.core.association_user_role.AssociationUserRoleService;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.role.RoleService;
import fr.mgargadennec.blossom.core.user.User;
import fr.mgargadennec.blossom.core.user.UserCreateForm;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@BlossomController("/administration/users")
@OpenedMenu("users")
public class UsersController {

  private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

  private final UserService userService;
  private final AssociationUserGroupService associationUserGroupService;
  private final AssociationUserRoleService associationUserRoleService;
  private final RoleService roleService;
  private final GroupService groupService;
  private final SearchEngineImpl<UserDTO> searchEngine;

  public UsersController(UserService userService, AssociationUserGroupService associationUserGroupService,
      AssociationUserRoleService associationUserRoleService, RoleService roleService, GroupService groupService,
      SearchEngineImpl<UserDTO> searchEngine) {
    this.userService = userService;
    this.associationUserGroupService = associationUserGroupService;
    this.associationUserRoleService = associationUserRoleService;
    this.roleService = roleService;
    this.groupService = groupService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  public ModelAndView getUsersPage(@RequestParam(value = "q", required = false) String q,
      @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "users/users");
  }

  @GetMapping("/_list")
  public ModelAndView getUsersTable(@RequestParam(value = "q", required = false) String q,
      @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "users/table");
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
  public ModelAndView getUserCreatePage(Model model, Locale locale) {
    UserCreateForm userCreateForm = new UserCreateForm();
    userCreateForm.setLocale(locale);
    return this.createView(userCreateForm, model);
  }

  @PostMapping("/_create")
  public ModelAndView handleUserCreateForm(@Valid @ModelAttribute("userCreateForm") UserCreateForm userCreateForm,
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
  public ModelAndView getUser(@PathVariable Long id, Model model, HttpServletRequest request) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    model.addAttribute("user", user);
    return new ModelAndView("users/user", "user", user);
  }

  @GetMapping("/{id}/_informations")
  public ModelAndView getUserInformations(@PathVariable Long id, Model model, HttpServletRequest request) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    model.addAttribute("user", user);
    return new ModelAndView("users/userinformations", "user", user);
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

  @GetMapping("/{id}/_groups")
  public ModelAndView getUserGroupsPage(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    List<AssociationUserGroupDTO> associations = associationUserGroupService.getAllLeft(user);
    List<GroupDTO> associatedGroups = new ArrayList<>();
    for (AssociationUserGroupDTO association : associations) {
      associatedGroups.add(association.getB());
    }
    Page<GroupDTO> pagedGroups = new PageImpl<>(associatedGroups);

    List<GroupDTO> groups = groupService.getAll();
    groups = groups.stream().filter(group -> !associatedGroups.contains(group)).collect(Collectors.toList());
    return this.groupsView(user, pagedGroups, groups, model);
  }

  @GetMapping("/{id}/_roles")
  public ModelAndView getUserRolesPage(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    List<AssociationUserRoleDTO> associations = associationUserRoleService.getAllLeft(user);

    List<RoleDTO> associatedRoles = new ArrayList<>();
    for (AssociationUserRoleDTO association : associations) {
      associatedRoles.add(association.getB());
    }
    Page<RoleDTO> pagedRoles = new PageImpl<>(associatedRoles);

    List<RoleDTO> roles = roleService.getAll();
    roles = roles.stream().filter(role -> !associatedRoles.contains(role)).collect(Collectors.toList());

    return this.rolesView(user, pagedRoles, roles, model);
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

  private ModelAndView groupsView(UserDTO user, Page<GroupDTO> pagedAssociatedGroups, List<GroupDTO> groups, Model model) {
    model.addAttribute("user", user);
    model.addAttribute("groups", groups);
    model.addAttribute("associatedGroups", pagedAssociatedGroups);
    return new ModelAndView("users/usergroups", model.asMap());
  }

  private ModelAndView rolesView(UserDTO user, Page<RoleDTO> pagedRoles, List<RoleDTO> roles, Model model) {
    model.addAttribute("user", user);
    model.addAttribute("associatedRoles", pagedRoles);
    model.addAttribute("roles", roles);
    return new ModelAndView("users/userroles", model.asMap());
  }

  @GetMapping(value = "/{id}/avatar", produces = "image/*")
  @ResponseBody
  public byte[] displayAvatar(@PathVariable Long id) throws IOException {
    return this.userService.loadAvatar(id);
  }

  @PostMapping("/{id}/_change_password")
  public String askForPasswordChange(@PathVariable Long id, RedirectAttributes redirectAttributes) throws Exception {
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
