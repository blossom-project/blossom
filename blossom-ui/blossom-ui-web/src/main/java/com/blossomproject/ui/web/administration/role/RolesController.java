package com.blossomproject.ui.web.administration.role;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.tree.TreeNode;
import com.blossomproject.core.role.RoleCreateForm;
import com.blossomproject.core.role.RoleDTO;
import com.blossomproject.core.role.RolePrivilegeUpdateForm;
import com.blossomproject.core.role.RoleService;
import com.blossomproject.core.role.RoleUpdateForm;
import com.blossomproject.ui.menu.OpenedMenu;
import com.blossomproject.ui.stereotype.BlossomController;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@BlossomController
@RequestMapping("/administration/roles")
@OpenedMenu("roles")
public class RolesController {

  private static final Logger logger = LoggerFactory.getLogger(RolesController.class);

  private final RoleService roleService;
  private final SearchEngineImpl<RoleDTO> searchEngine;
  private final MessageSource messageSource;

  public RolesController(RoleService roleService, SearchEngineImpl<RoleDTO> searchEngine,
    MessageSource messageSource) {
    this.roleService = roleService;
    this.searchEngine = searchEngine;
    this.messageSource = messageSource;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('administration:roles:read')")
  public ModelAndView getRolesPage(@RequestParam(value = "q", required = false) String q,
    @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "blossom/roles/roles");
  }

  private ModelAndView tableView(String q, Pageable pageable, Model model, String viewName) {
    Page<RoleDTO> roles;

    if (Strings.isNullOrEmpty(q)) {
      roles = this.roleService.getAll(pageable);
    } else {
      roles = this.searchEngine.search(q, pageable).getPage();
    }

    model.addAttribute("roles", roles);
    model.addAttribute("q", q);

    return new ModelAndView(viewName, model.asMap());
  }

  @GetMapping("/_create")
  @PreAuthorize("hasAuthority('administration:roles:create')")
  public ModelAndView getRoleCreatePage(Model model, Locale locale) {
    return this.createView(new RoleCreateForm(), model);
  }

  @PostMapping("/_create")
  @PreAuthorize("hasAuthority('administration:roles:create')")
  public ModelAndView handleRoleCreateForm(
    @Valid @ModelAttribute("roleCreateForm") RoleCreateForm roleCreateForm,
    BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return this.createView(roleCreateForm, model);
    }
    try {
      RoleDTO role = this.roleService.create(roleCreateForm);
      return new ModelAndView("redirect:../roles/" + role.getId());
    } catch (Exception e) {
      logger
        .error("Error on creating role, name " + roleCreateForm.getName() + " already exists ", e);
      return this.createView(roleCreateForm, model);
    }
  }

  private ModelAndView createView(RoleCreateForm roleCreateForm, Model model) {
    model.addAttribute("roleCreateForm", roleCreateForm);
    return new ModelAndView("blossom/roles/create", model.asMap());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:roles:read')")
  public ModelAndView getRole(@PathVariable Long id, Model model, HttpServletRequest request) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    model.addAttribute("role", role);
    return new ModelAndView("blossom/roles/role", "role", role);
  }

  @PostMapping("/{id}/_delete")
  @PreAuthorize("hasAuthority('administration:roles:delete')")
  @ResponseBody
  public ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> deleteRole(
    @PathVariable Long id,
    @RequestParam(value = "force", required = false, defaultValue = "false") Boolean force) {
    Optional<Map<Class<? extends AbstractDTO>, Long>> result = this.roleService
      .delete(this.roleService.getOne(id), force);

    if (!result.isPresent() || result.get().isEmpty()) {
      return new ResponseEntity<>(Maps.newHashMap(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(result.get(), HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/{id}/_informations")
  @PreAuthorize("hasAuthority('administration:roles:read')")
  public ModelAndView getRoleInformations(@PathVariable Long id) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    return this.viewRoleInformationView(role);
  }

  @GetMapping("/{id}/_informations/_edit")
  @PreAuthorize("hasAuthority('administration:roles:write')")
  public ModelAndView getRoleInformationsForm(@PathVariable Long id, Model model,
    HttpServletRequest request) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    return this.updateRoleInformationView(new RoleUpdateForm(role), model, Optional.empty());
  }

  @PostMapping("/{id}/_informations/_edit")
  @PreAuthorize("hasAuthority('administration:roles:write')")
  public ModelAndView handleRoleInformationsForm(@PathVariable Long id, Model model,
    @Valid @ModelAttribute("roleUpdateForm") RoleUpdateForm roleUpdateForm,
    BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return this
        .updateRoleInformationView(roleUpdateForm, model, Optional.of(HttpStatus.CONFLICT));
    }

    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    role.setName(roleUpdateForm.getName());
    role.setDescription(roleUpdateForm.getDescription());
    RoleDTO updatedRole = this.roleService.update(id, role);

    return this.viewRoleInformationView(updatedRole);
  }

  private ModelAndView viewRoleInformationView(RoleDTO role) {
    return new ModelAndView("blossom/roles/roleinformations", "role", role);
  }

  private ModelAndView updateRoleInformationView(RoleUpdateForm roleUpdateForm, Model model,
    Optional<HttpStatus> status) {
    ModelAndView modelAndView = new ModelAndView("blossom/roles/roleinformations-edit",
      "roleUpdateForm", roleUpdateForm);
    modelAndView.setStatus(status.orElse(HttpStatus.OK));
    return modelAndView;
  }

  @GetMapping(value = "/privileges/tree", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public TreeNode<Privilege> privilegeTreeNode(Locale locale) {
    List<Privilege> availablePrivileges = this.roleService.getAvailablePrivileges();

    TreeNode<Privilege> rootNode = new TreeNode<>("ALL", messageSource.getMessage("right.all", null, "right.all", locale), null);

    availablePrivileges
      .stream()
      .forEach(p -> {
        TreeNode<Privilege> treeNode = rootNode;
        String[] keyParts = p.privilege().split(":");
        int i = 0;
        String currentKey = null;
        do {
          currentKey = currentKey != null ? currentKey + ":" + keyParts[i] : keyParts[i];
          Optional<TreeNode<Privilege>> child = treeNode.findChildWithId(currentKey);
          if (child.isPresent()) {
            treeNode = child.get();
          } else {
            String labelKey = ("right." + currentKey).replaceAll(":",".");
            TreeNode<Privilege> newNode = new TreeNode<>(currentKey, messageSource.getMessage(labelKey, null, labelKey, locale));
            if(currentKey.equals(p.privilege())) {
              newNode.setData(p);
            }
            treeNode.addChild(newNode);
            treeNode = newNode;
          }
          i++;
        } while (i <= keyParts.length - 1);

      });
    return rootNode;
  }

  @GetMapping("/{id}/_privileges")
  @PreAuthorize("hasAuthority('administration:roles:read')")
  public ModelAndView getRolePrivileges(@PathVariable Long id, Model model) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    return this.viewRolePrivilegeView(role, model);
  }

  @GetMapping("/{id}/_privileges/_edit")
  @PreAuthorize("hasAuthority('administration:roles:write')")
  public ModelAndView getRolePrivilegesForm(@PathVariable Long id, Model model) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    return this.updateRolePrivilegesView(new RolePrivilegeUpdateForm(role), model);
  }

  @PostMapping("/{id}/_privileges/_edit")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('administration:roles:write')")
  public ModelAndView handleRolePrivilegesForm(@PathVariable Long id,
    @Valid @ModelAttribute("rolePrivilegeUpdateForm") RolePrivilegeUpdateForm rolePrivilegeUpdateForm,
    BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return this.updateRolePrivilegesView(rolePrivilegeUpdateForm, model);
    }

    RoleDTO role = this.roleService.getOne(id);
    List<String> availablePrivileges = this.roleService.getAvailablePrivileges().stream().map(Privilege::privilege).collect(Collectors.toList());
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    role.setPrivileges(rolePrivilegeUpdateForm.getPrivileges().stream().filter(availablePrivileges::contains).collect(Collectors.toList()));
    RoleDTO updatedRole = this.roleService.update(id, role);
    return this.viewRolePrivilegeView(updatedRole, model);
  }

  private ModelAndView viewRolePrivilegeView(RoleDTO role, Model model) {
    model.addAttribute("role", role);
    return new ModelAndView("blossom/roles/roleprivileges", model.asMap());
  }

  private ModelAndView updateRolePrivilegesView(RolePrivilegeUpdateForm rolePrivilegeUpdateForm,
    Model model) {
    model.addAttribute("rolePrivilegeUpdateForm", rolePrivilegeUpdateForm);
    ModelAndView modelAndView = new ModelAndView("blossom/roles/roleprivileges-edit",
      model.asMap());
    return modelAndView;
  }
}
