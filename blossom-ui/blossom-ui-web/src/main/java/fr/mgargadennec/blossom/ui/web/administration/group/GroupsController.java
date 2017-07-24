package fr.mgargadennec.blossom.ui.web.administration.group;

import java.util.Locale;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;

import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.group.GroupCreateForm;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.group.GroupUpdateForm;
import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@BlossomController("/administration/groups")
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
  public ModelAndView getGroupsPage(@RequestParam(value = "q", required = false) String q,
      @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "groups/groups");
  }

  @GetMapping("/_list")
  public ModelAndView getGroupsTable(@RequestParam(value = "q", required = false) String q,
      @PageableDefault(size = 25) Pageable pageable, Model model) {
    return tableView(q, pageable, model, "groups/table");
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
  public ModelAndView getGroupCreatePage(Model model, Locale locale) {
    GroupCreateForm groupCreateForm = new GroupCreateForm();
    groupCreateForm.setLocale(locale);
    return this.createView(groupCreateForm, model);
  }

  @PostMapping("/_create")
  public ModelAndView handleGroupCreateForm(@Valid @ModelAttribute("groupCreateForm") GroupCreateForm groupCreateForm,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return this.createView(groupCreateForm, model);
    }
    try {
      GroupDTO group = this.groupService.create(groupCreateForm);
      return new ModelAndView("redirect:../groups/" + group.getId());
    } catch (Exception e) {
      logger.error("Error on creating group, name " + groupCreateForm.getName() + " already exists ", e);
      return this.createView(groupCreateForm, model);
    }

  }

  private ModelAndView createView(GroupCreateForm groupCreateForm, Model model) {
    model.addAttribute("groupCreateForm", groupCreateForm);
    return new ModelAndView("groups/create", model.asMap());
  }

  @GetMapping("/{id}")
  public ModelAndView getGroup(@PathVariable Long id, Model model, HttpServletRequest request) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Group=%s not found", id));
    }
    model.addAttribute("group", group);
    return new ModelAndView("groups/group", "group", group);
  }

  @PostMapping("/{id}/_delete")
  public String deleteGroup(@PathVariable Long id) {
    this.groupService.delete(this.groupService.getOne(id));
    return "redirect:..";
  }

  @GetMapping("/{id}/_edit")
  public ModelAndView getGroupUpdatePage(@PathVariable Long id, Model model, Locale locale) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Group=%s not found", id));
    }

    return this.editView(group, locale);
  }

  @PostMapping("/{id}/_edit")
  public ModelAndView handleUpdateGroupForm(@PathVariable Long id,
      @Valid @ModelAttribute("groupUpdateForm") GroupUpdateForm groupUpdateForm, BindingResult bindingResult,
      Locale locale) {
    if (bindingResult.hasErrors()) {
      return this.editView(groupUpdateForm, locale);
    }
    try {
      this.groupService.update(id, groupUpdateForm);
      return new ModelAndView("redirect:../" + id);
    } catch (Exception e) {
      return this.editView(groupUpdateForm, locale);
    }

  }

  private ModelAndView editView(GroupUpdateForm groupUpdateForm, Locale locale) {
    groupUpdateForm.setLocale(locale);
    return new ModelAndView("groups/update", "groupUpdateForm", groupUpdateForm);
  }

  private ModelAndView editView(GroupDTO group, Locale locale) {
    GroupUpdateForm groupUpdateForm = new GroupUpdateForm(group.getName(), group.getDescription(), locale);
    return new ModelAndView("groups/update", "groupUpdateForm", groupUpdateForm);
  }

}
