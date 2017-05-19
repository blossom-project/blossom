package fr.mgargadennec.blossom.ui.web.administration.role;

import com.google.common.base.Strings;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.role.RoleService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.NoSuchElementException;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@BlossomController("/administration/roles")
public class RolesController {

  private static final Logger logger = LoggerFactory.getLogger(RolesController.class);

  private final RoleService roleService;
  private final SearchEngineImpl<RoleDTO> searchEngine;

  public RolesController(RoleService roleService, SearchEngineImpl<RoleDTO> searchEngine) {
    this.roleService = roleService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  public ModelAndView getRolesPage(@RequestParam(value = "q", required = false) String q, @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "roles/roles");
  }

  @GetMapping("/_list")
  public ModelAndView getRolesTable(@RequestParam(value = "q", required = false) String q, @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "roles/table");
  }

  private ModelAndView tableView(String q, Pageable pageable, Model model, String viewName) {
    Page<RoleDTO> roles;

    if (Strings.isNullOrEmpty(q)) {
      roles = this.roleService.getAll(pageable);
    } else {
      roles = this.searchEngine.search(q, pageable);
    }

    model.addAttribute("roles", roles);
    model.addAttribute("q", q);

    return new ModelAndView(viewName, model.asMap());
  }

  @GetMapping("/_create")
  public ModelAndView getRoleCreatePage() {
    return this.createView(new RoleDTO());
  }

  @PostMapping("/_create")
  public ModelAndView handleRoleCreateForm(@Valid @ModelAttribute("role") RoleDTO role) {
    try {
      role = this.roleService.create(role);
    } catch (DataIntegrityViolationException e) {
      logger.error("Error on creating role, name " + role.getName() + " already exists ", e);
      return this.createView(role);
    }
    return new ModelAndView("redirect:/roles/" + role.getId());
  }

  private ModelAndView createView(RoleDTO role) {
    return new ModelAndView("roles/create", "role", role);
  }


  @GetMapping("/{id}")
  public ModelAndView getRole(@PathVariable Long id, Model model, HttpServletRequest request) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    model.addAttribute("role", role);
    return new ModelAndView("roles/role", "role", role);
  }


  @PostMapping("/{id}/_delete")
  public String deleteRole(@PathVariable Long id) {
    this.roleService.delete(this.roleService.getOne(id));
    return "redirect:/roles";
  }

  @GetMapping("/{id}/_edit")
  public ModelAndView getRoleUpdatePage(@PathVariable Long id, Model model) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }

    return this.editView(role);
  }

  @PostMapping("/{id}/_edit")
  public ModelAndView handleUpdateRoleForm(@PathVariable Long id, @Valid @ModelAttribute("role") RoleDTO role) {
    try {
      this.roleService.update(id, role);
    } catch (Exception e) {
      return this.editView(role);
    }
    return new ModelAndView("redirect:/roles/" + id);
  }

  private ModelAndView editView(RoleDTO role) {
    return new ModelAndView("roles/update", "role", role);
  }
}

