package fr.mgargadennec.blossom.ui.web.system.scheduler;

import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController("/system/scheduler")
public class SchedulerController {

  private final Scheduler scheduler;

  @Autowired
  public SchedulerController(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  @GetMapping
  public ModelAndView scheduler(Model model) throws SchedulerException {

    model.addAttribute("jobGroupNames", scheduler.getJobGroupNames());
    model.addAttribute("jobKeysByGroupNames", scheduler.getJobGroupNames().stream().collect(Collectors.toMap(Function.identity(), groupName -> {
      try {
        return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
      } catch (SchedulerException e) {
        return Lists.newArrayList();
      }
    })));

    return new ModelAndView("system/scheduler/scheduler", model.asMap());
  }

}
