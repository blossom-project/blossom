package fr.mgargadennec.blossom.ui.web.administration.group;

import com.google.common.base.Strings;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
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
@BlossomController("/administration/groups")
public class GroupsController {

    private static final Logger logger = LoggerFactory.getLogger(GroupsController.class);

    private final GroupService groupService;
    private final SearchEngineImpl<GroupDTO> searchEngine;

    public GroupsController(GroupService groupService, SearchEngineImpl<GroupDTO> searchEngine) {
        this.groupService = groupService;
        this.searchEngine = searchEngine;
    }

    @GetMapping
    public ModelAndView getGroupsPage(@RequestParam(value = "q", required = false) String q, @PageableDefault(size = 25) Pageable pageable, Model model) {
        return tableView(q, pageable, model, "groups/groups");
    }

    @GetMapping("/_list")
    public ModelAndView getGroupsTable(@RequestParam(value = "q", required = false) String q, @PageableDefault(size = 25) Pageable pageable, Model model) {
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
    public ModelAndView getGroupCreatePage() {
        return this.createView(new GroupDTO());
    }

    @PostMapping("/_create")
    public ModelAndView handleGroupCreateForm(@Valid @ModelAttribute("group") GroupDTO group) {
        try {
            group = this.groupService.create(group);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error on creating group, name " + group.getName() + " already exists ", e);
            return this.createView(group);
        }
        return new ModelAndView("redirect:/groups/" + group.getId());
    }

    private ModelAndView createView(GroupDTO group) {
        return new ModelAndView("groups/create", "group", group);
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
        return "redirect:/groups";
    }

    @GetMapping("/{id}/_edit")
    public ModelAndView getGroupUpdatePage(@PathVariable Long id, Model model) {
        GroupDTO group = this.groupService.getOne(id);
        if (group == null) {
            throw new NoSuchElementException(String.format("Group=%s not found", id));
        }

        return this.editView(group);
    }

    @PostMapping("/{id}/_edit")
    public ModelAndView handleUpdateGroupForm(@PathVariable Long id, @Valid @ModelAttribute("group") GroupDTO group) {
        try {
            this.groupService.update(id, group);
        } catch (Exception e) {
            return this.editView(group);
        }
        return new ModelAndView("redirect:/groups/" + id);
    }

    private ModelAndView editView(GroupDTO group) {
        return new ModelAndView("groups/update", "group", group);
    }
}

