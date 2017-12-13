package fr.blossom.ui.api.administration;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.user.UserCreateForm;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.core.user.UserUpdateForm;
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
@RequestMapping("/administration/users")
public class UsersApiController {

  private final UserService userService;
  private final SearchEngineImpl<UserDTO> searchEngine;

  public UsersApiController(UserService userService,
    SearchEngineImpl<UserDTO> searchEngine) {
    this.userService = userService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  public Page<UserDTO> list(
    @RequestParam(value = "q", required = false) String q,
    @PageableDefault(size = 25) Pageable pageable) {

    if (Strings.isNullOrEmpty(q)) {
      return this.userService.getAll(pageable);
    }
    return this.searchEngine.search(q, pageable).getPage();
  }

  @PostMapping
  public UserDTO create(@NotNull @Valid @RequestBody UserCreateForm userCreateForm)
    throws Exception {
    Preconditions.checkArgument(userCreateForm != null);
    return userService.create(userCreateForm);
  }

  @GetMapping("/{id}")
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
  public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateForm userUpdateForm) {
    Preconditions.checkArgument(id != null);
    UserDTO user = userService.getOne(id);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(userService.update(id, userUpdateForm), HttpStatus.OK);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> delete(@PathVariable Long id,
    @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
    UserDTO user = this.userService.getOne(id);
    if(user == null){
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Optional<Map<Class<? extends AbstractDTO>, Long>> result = this.userService.delete(user, force);
    if (!result.isPresent() || result.get().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(result.get(), HttpStatus.CONFLICT);
    }
  }
}
