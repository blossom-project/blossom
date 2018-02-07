package com.blossom_project.ui.api.administration;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.search.SearchEngineImpl;
import com.blossom_project.core.group.GroupCreateForm;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.group.GroupService;
import com.blossom_project.core.group.GroupUpdateForm;
import com.blossom_project.ui.stereotype.BlossomApiController;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/administration/groups")
public class GroupsApiController {

  private final GroupService groupService;
  private final SearchEngineImpl<GroupDTO> searchEngine;

  public GroupsApiController(GroupService groupService,
    SearchEngineImpl<GroupDTO> searchEngine) {
    this.groupService = groupService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('administration:groups:read')")
  public Page<GroupDTO> list(
    @RequestParam(value = "q", required = false) String q, @PageableDefault(size = 25) Pageable pageable) {

    if (Strings.isNullOrEmpty(q)) {
      return this.groupService.getAll(pageable);
    }
    return this.searchEngine.search(q, pageable).getPage();
  }

  @PostMapping
  @PreAuthorize("hasAuthority('administration:groups:create')")
  public ResponseEntity<GroupDTO> create(@NotNull @Valid @RequestBody GroupCreateForm groupCreateForm)
    throws Exception {
    Preconditions.checkArgument(groupCreateForm != null);
    return new ResponseEntity<>(groupService.create(groupCreateForm), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:groups:read')")
  public ResponseEntity<GroupDTO> get(@PathVariable Long id) {
    Preconditions.checkArgument(id != null);
    GroupDTO group = groupService.getOne(id);
    if (group == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(group, HttpStatus.OK);
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:groups:write')")
  public ResponseEntity<GroupDTO> update(@PathVariable Long id,
    @Valid @RequestBody GroupUpdateForm groupUpdateForm) {
    Preconditions.checkArgument(id != null);
    GroupDTO group = groupService.getOne(id);
    if (group == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(groupService.update(id, groupUpdateForm), HttpStatus.OK);
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:groups:delete')")
  public ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> delete(@PathVariable Long id,
    @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Optional<Map<Class<? extends AbstractDTO>, Long>> result = this.groupService.delete(group, force);
    if (!result.isPresent() || result.get().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(result.get(), HttpStatus.CONFLICT);
    }
  }
}
