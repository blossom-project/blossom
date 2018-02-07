package com.blossom_project.ui.api.administration;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.search.SearchEngineImpl;
import com.blossom_project.core.user.UserCreateForm;
import com.blossom_project.core.user.UserDTO;
import com.blossom_project.core.user.UserService;
import com.blossom_project.core.user.UserUpdateForm;
import com.blossom_project.ui.stereotype.BlossomApiController;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomApiController
@RequestMapping("/administration/users")
public class UsersApiController {

  private final UserService userService;
  private final SearchEngineImpl<UserDTO> searchEngine;
  private final Tika tika;

  public UsersApiController(UserService userService,
    SearchEngineImpl<UserDTO> searchEngine, Tika tika) {
    this.userService = userService;
    this.searchEngine = searchEngine;
    this.tika = tika;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('administration:users:read')")
  public Page<UserDTO> list(
    @RequestParam(value = "q", required = false) String q,
    @PageableDefault(size = 25) Pageable pageable) {

    if (Strings.isNullOrEmpty(q)) {
      return this.userService.getAll(pageable);
    }
    return this.searchEngine.search(q, pageable).getPage();
  }

  @PostMapping
  @PreAuthorize("hasAuthority('administration:users:create')")
  public ResponseEntity<UserDTO> create(@NotNull @Valid @RequestBody UserCreateForm userCreateForm)
    throws Exception {
    Preconditions.checkArgument(userCreateForm != null);
    return new ResponseEntity<>(userService.create(userCreateForm), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:users:read')")
  public ResponseEntity<UserDTO> get(@PathVariable Long id) {
    Preconditions.checkArgument(id != null);
    UserDTO user = userService.getOne(id);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(user, HttpStatus.OK);
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:users:write')")
  public ResponseEntity<UserDTO> update(@PathVariable Long id,
    @Valid @RequestBody UserUpdateForm userUpdateForm) {
    Preconditions.checkArgument(id != null);
    UserDTO user = userService.getOne(id);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(userService.update(id, userUpdateForm), HttpStatus.OK);
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:users:delete')")
  public ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> delete(@PathVariable Long id,
    @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Optional<Map<Class<? extends AbstractDTO>, Long>> result = this.userService.delete(user, force);
    if (!result.isPresent() || result.get().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(result.get(), HttpStatus.CONFLICT);
    }
  }

  @GetMapping(value = "/{id}/avatar")
  @ResponseBody
  public ResponseEntity<InputStreamResource> displayAvatar(@PathVariable Long id)
    throws IOException {
    InputStream avatar = this.userService.loadAvatar(id);
    return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType(this.tika.detect(avatar)))
      .body(new InputStreamResource(avatar));
  }

  @PostMapping("/{id}/_avatar/_edit")
  @PreAuthorize("hasAuthority('administration:users:write')")
  public void updateAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile file)
    throws IOException {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    this.userService.updateAvatar(id, file.getBytes());
  }
}
