package com.blossomproject.ui.web.system.scheduler;

import com.blossomproject.core.scheduler.job.JobInfo;
import com.blossomproject.core.scheduler.job.ScheduledJobService;
import com.blossomproject.core.scheduler.job.SchedulerInfo;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobKey;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerControllerTest {

    @Mock
    ScheduledJobService scheduledJobService;

    private SchedulerController controller;

    @Before
    public void setUp() {
        controller = new SchedulerController(scheduledJobService);
    }

    @Test
    public void should_display_scheduler_and_groups() throws Exception {
        SchedulerInfo schedulerInfo = mock(SchedulerInfo.class);
        when(scheduledJobService.getSchedulerInfo()).thenReturn(schedulerInfo);

        List<String> groupsInfo = Lists.newArrayList("test", "test2");
        when(scheduledJobService.getGroups()).thenReturn(groupsInfo);

        ModelAndView result = controller.scheduler(new ExtendedModelMap());

        Assert.assertEquals("blossom/system/scheduler/scheduler", result.getViewName());
        Assert.assertEquals(schedulerInfo, result.getModel().get("scheduler"));
        Assert.assertEquals(groupsInfo, result.getModel().get("groups"));

        verify(scheduledJobService, times(1)).getSchedulerInfo();
        verify(scheduledJobService, times(1)).getGroups();
    }

    @Test
    public void should_display_scheduler_and_groups_without_groups() throws Exception {
        SchedulerInfo schedulerInfo = mock(SchedulerInfo.class);
        when(scheduledJobService.getSchedulerInfo()).thenReturn(schedulerInfo);

        when(scheduledJobService.getGroups()).thenReturn(Lists.newArrayList());

        ModelAndView result = controller.scheduler(new ExtendedModelMap());

        Assert.assertEquals("blossom/system/scheduler/scheduler", result.getViewName());
        Assert.assertEquals(schedulerInfo, result.getModel().get("scheduler"));
        Assert.assertTrue(((List) result.getModel().get("groups")).isEmpty());

        verify(scheduledJobService, times(1)).getSchedulerInfo();
        verify(scheduledJobService, times(1)).getGroups();
    }

    @Test
    public void should_display_scheduler_and_jobs_infos() throws Exception {
        SchedulerInfo schedulerInfo = mock(SchedulerInfo.class);
        when(scheduledJobService.getSchedulerInfo()).thenReturn(schedulerInfo);

        List<JobInfo> jobInfos = new ArrayList();
        jobInfos.add(mock(JobInfo.class));
        jobInfos.add(mock(JobInfo.class));
        when(scheduledJobService.getAll(eq("groups"))).thenReturn(jobInfos);

        ModelAndView result = controller.tasks(new ExtendedModelMap(), "groups");

        Assert.assertEquals("blossom/system/scheduler/list", result.getViewName());
        Assert.assertEquals(schedulerInfo, result.getModel().get("scheduler"));
        Assert.assertEquals(jobInfos, result.getModel().get("jobInfos"));

        verify(scheduledJobService, times(1)).getSchedulerInfo();
        verify(scheduledJobService, times(1)).getAll(any());
    }

    @Test
    public void should_display_scheduler_and_jobs_infos_without_jobs_infos() throws Exception {
        SchedulerInfo schedulerInfo = mock(SchedulerInfo.class);
        when(scheduledJobService.getSchedulerInfo()).thenReturn(schedulerInfo);

        List<JobInfo> jobInfos = new ArrayList();
        when(scheduledJobService.getAll(eq("groups"))).thenReturn(jobInfos);

        ModelAndView result = controller.tasks(new ExtendedModelMap(), "groups");

        Assert.assertEquals("blossom/system/scheduler/list", result.getViewName());
        Assert.assertEquals(schedulerInfo, result.getModel().get("scheduler"));
        Assert.assertTrue(((List) result.getModel().get("jobInfos")).isEmpty());

        verify(scheduledJobService, times(1)).getSchedulerInfo();
        verify(scheduledJobService, times(1)).getAll(any());
    }

    @Test
    public void should_display_scheduler_and_job_infos_by_name() throws Exception {
        SchedulerInfo schedulerInfo = mock(SchedulerInfo.class);
        when(scheduledJobService.getSchedulerInfo()).thenReturn(schedulerInfo);

        JobInfo jobInfos = mock(JobInfo.class);
        when(scheduledJobService.getOne(eq(JobKey.jobKey("name", "groups")))).thenReturn(jobInfos);

        ModelAndView result = controller.task(new ExtendedModelMap(), "groups", "name");

        Assert.assertEquals("blossom/system/scheduler/detail", result.getViewName());
        Assert.assertEquals(schedulerInfo, result.getModel().get("scheduler"));
        Assert.assertEquals(result.getModel().get("jobInfo"), jobInfos);

        verify(scheduledJobService, times(1)).getSchedulerInfo();
        verify(scheduledJobService, times(1)).getOne(any());
    }

    @Test
    public void should_display_scheduler_and_job_infos_by_name_without_job_infos() throws Exception {
        SchedulerInfo schedulerInfo = mock(SchedulerInfo.class);
        when(scheduledJobService.getSchedulerInfo()).thenReturn(schedulerInfo);

        when(scheduledJobService.getOne(eq(JobKey.jobKey("name", "groups")))).thenReturn(null);

        ModelAndView result = controller.task(new ExtendedModelMap(), "groups", "name");

        Assert.assertEquals("blossom/system/scheduler/detail", result.getViewName());
        Assert.assertEquals(schedulerInfo, result.getModel().get("scheduler"));
        Assert.assertNull(result.getModel().get("jobInfo"));

        verify(scheduledJobService, times(1)).getSchedulerInfo();
        verify(scheduledJobService, times(1)).getOne(any());
    }

    @Test
    public void should_execute_job() throws Exception {
        controller.executeTask(new ExtendedModelMap(), "group", "name");
        verify(scheduledJobService, times(1)).execute(eq(JobKey.jobKey("name", "group")));
        verifyNoMoreInteractions(scheduledJobService);
    }

    @Test
    public void should_change_state_to_true() throws Exception {
        controller.scheduler(new ExtendedModelMap(), true);
        verify(scheduledJobService, times(1)).changeState(eq(true));
        verifyNoMoreInteractions(scheduledJobService);
    }

    @Test
    public void should_change_state_to_false() throws Exception {
        controller.scheduler(new ExtendedModelMap(), false);
        verify(scheduledJobService, times(1)).changeState(eq(false));
        verifyNoMoreInteractions(scheduledJobService);
    }
}
