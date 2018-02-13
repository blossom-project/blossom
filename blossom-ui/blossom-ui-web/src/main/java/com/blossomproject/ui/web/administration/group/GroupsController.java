package com.blossomproject.ui.web.administration.group;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.group.GroupCreateForm;
import com.blossomproject.core.group.GroupDTO;
import com.blossomproject.core.group.GroupService;
import com.blossomproject.core.group.GroupUpdateForm;
import com.blossomproject.ui.menu.OpenedMenu;
import com.blossomproject.ui.stereotype.BlossomController;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@BlossomController
@RequestMapping("/administration/groups")
@OpenedMenu("groups")
public class GroupsController {

  private static final Logger logger = LoggerFactory.getLogger(GroupsController.class);

  private final GroupService groupService;
  private final SearchEngineImpl<GroupDTO> searchEngine;

  public GroupsController(GroupService groupService, SearchEngineImpl<GroupDTO> searchEngine) {
    this.groupService = groupService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('administration:groups:read')")
  public ModelAndView getGroupsPage(@RequestParam(value = "q", required = false) String q,
    @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "blossom/groups/groups");
  }

  private ModelAndView tableView(String q, Pageable pageable, Model model, String viewName) {
    Page<GroupDTO> groups;

    if (Strings.isNullOrEmpty(q)) {
      groups = this.groupService.getAll(pageable);
    } else {
      groups = this.searchEngine.search(q, pageable).getPage();
    }

    model.addAttribute("groups", groups);
    model.addAttribute("q", q);

    return new ModelAndView(viewName, model.asMap());
  }

  @GetMapping("/_create")
  @PreAuthorize("hasAuthority('administration:groups:create')")
  public ModelAndView getGroupCreatePage(Model model, Locale locale) {
    GroupCreateForm groupCreateForm = new GroupCreateForm();
    groupCreateForm.setLocale(locale);
    return this.createView(groupCreateForm, model);
  }

  @PostMapping("/_create")
  @PreAuthorize("hasAuthority('administration:groups:create')")
  public ModelAndView handleGroupCreateForm(
    @Valid @ModelAttribute("groupCreateForm") GroupCreateForm groupCreateForm,
    BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return this.createView(groupCreateForm, model);
    }
    try {
      GroupDTO group = this.groupService.create(groupCreateForm);
      return new ModelAndView("redirect:../groups/" + group.getId());
    } catch (Exception e) {
      logger
        .error("Error on creating group, name " + groupCreateForm.getName() + " already exists ",
          e);
      return this.createView(groupCreateForm, model);
    }

  }

  private ModelAndView createView(GroupCreateForm groupCreateForm, Model model) {
    model.addAttribute("groupCreateForm", groupCreateForm);
    return new ModelAndView("blossom/groups/create", model.asMap());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:groups:read')")
  public ModelAndView getGroup(@PathVariable Long id, Model model, HttpServletRequest request) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Group=%s not found", id));
    }
    model.addAttribute("group", group);
    return new ModelAndView("blossom/groups/group", "group", group);
  }


  @PostMapping("/{id}/_delete")
  @PreAuthorize("hasAuthority('administration:groups:delete')")
  public @ResponseBody
  ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> deleteGroup(
    @PathVariable Long id,
    @RequestParam(value = "force", required = false, defaultValue = "false") Boolean force) {
    Optional<Map<Class<? extends AbstractDTO>, Long>> result = this.groupService
      .delete(this.groupService.getOne(id), force);

    if (!result.isPresent() || result.get().isEmpty()) {
      return new ResponseEntity<>(Maps.newHashMap(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(result.get(), HttpStatus.CONFLICT);
    }
  }


  @GetMapping("/{id}/_informations")
  @PreAuthorize("hasAuthority('administration:groups:read')")
  public ModelAndView getRoleInformations(@PathVariable Long id, HttpServletRequest request) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Group=%s not found", id));
    }
    return this.viewGroupInformationView(group);
  }

  @GetMapping("/{id}/_informations/_edit")
  @PreAuthorize("hasAuthority('administration:groups:write')")
  public ModelAndView getGroupInformationsForm(@PathVariable Long id, Model model) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Group=%s not found", id));
    }
    return this.updateGroupInformationView(new GroupUpdateForm(group), model, Optional.empty());
  }

  @PostMapping("/{id}/_informations/_edit")
  @PreAuthorize("hasAuthority('administration:groups:write')")
  public ModelAndView handleGroupInformationsForm(@PathVariable Long id, Model model,
    @Valid @ModelAttribute("groupUpdateForm") GroupUpdateForm groupUpdateForm,
    BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return this.updateGroupInformationView(groupUpdateForm, model, Optional.of(HttpStatus.CONFLICT));
    }

    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    group.setName(groupUpdateForm.getName());
    group.setDescription(groupUpdateForm.getDescription());
    GroupDTO updatedGroup = this.groupService.update(id, group);

    return this.viewGroupInformationView(updatedGroup);
  }

  private ModelAndView viewGroupInformationView(GroupDTO group) {
    return new ModelAndView("blossom/groups/groupinformations", "group", group);
  }

  private ModelAndView updateGroupInformationView(GroupUpdateForm groupUpdateForm, Model model, Optional<HttpStatus> status) {
    ModelAndView modelAndView= new ModelAndView("blossom/groups/groupinformations-edit", "groupUpdateForm", groupUpdateForm);
    modelAndView.setStatus(status.orElse(HttpStatus.OK));
    return modelAndView;
  }

}
