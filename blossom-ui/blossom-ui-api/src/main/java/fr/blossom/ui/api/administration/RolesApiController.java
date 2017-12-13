package fr.blossom.ui.api.administration;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.role.RoleCreateForm;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleService;
import fr.blossom.core.role.RoleUpdateForm;
import fr.blossom.ui.stereotype.BlossomApiController;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomApiController
@RequestMapping("/administration/roles")
public class RolesApiController {

  private final RoleService roleService;
  private final SearchEngineImpl<RoleDTO> searchEngine;

  public RolesApiController(RoleService roleService,
    SearchEngineImpl<RoleDTO> searchEngine) {
    this.roleService = roleService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  public Page<RoleDTO> list(
    @RequestParam(value = "q", required = false) String q, @PageableDefault(size = 25) Pageable pageable) {

    if (Strings.isNullOrEmpty(q)) {
      return this.roleService.getAll(pageable);
    }
    return this.searchEngine.search(q, pageable).getPage();
  }

  @PostMapping
  public RoleDTO create(@NotNull @Valid @RequestBody RoleCreateForm roleCreateForm)
    throws Exception {
    Preconditions.checkArgument(roleCreateForm != null);
    return roleService.create(roleCreateForm);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RoleDTO> get(@PathVariable Long id) {
    Preconditions.checkArgument(id != null);
    RoleDTO role = roleService.getOne(id);
    if (role == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(role, HttpStatus.OK);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<RoleDTO> update(@PathVariable Long id,
    @Valid @RequestBody RoleUpdateForm roleUpdateForm) {
    Preconditions.checkArgument(id != null);
    RoleDTO role = roleService.getOne(id);
    if (role == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(roleService.update(id, roleUpdateForm), HttpStatus.OK);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> delete(@PathVariable Long id,
    @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Optional<Map<Class<? extends AbstractDTO>, Long>> result = this.roleService.delete(role, force);
    if (!result.isPresent() || result.get().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(result.get(), HttpStatus.CONFLICT);
    }
  }
}
