package com.blossomproject.ui.web;

import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StatusControllerTest {

    @Mock
    HealthEndpoint healthEndpoint;

    private StatusController controller;

    @Before
    public void setUp() {
        controller = new StatusController(healthEndpoint);
    }

    @Test
    public void should_display_status_with_health_up_no_exclude () throws Exception {
        Map<String,String> map = new HashMap();
        map.put("test","testMessage");
        Health.Builder builder = new Health.Builder(Status.UP, map);
        Health health = builder.build();

        StatusController controllerSpy = spy(controller);
        doReturn(health).when(controllerSpy).filteredDetails(any(), any(List.class));

        ResponseEntity<Health> response = controllerSpy.status(Optional.empty());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatusCode()== HttpStatus.OK);
        Assert.assertEquals(health, response.getBody());
    }

    @Test
    public void should_display_status_with_health_error_no_exclude () throws Exception {
        Map<String,String> map = new HashMap();
        map.put("test","testMessage");
        Health.Builder builder = new Health.Builder(Status.DOWN, map);
        Health health = builder.build();

        StatusController controllerSpy = spy(controller);
        doReturn(health).when(controllerSpy).filteredDetails(any(), any(List.class));

        ResponseEntity<Health> response = controllerSpy.status(Optional.empty());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatusCode()== HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(health, response.getBody());
    }

    @Test
    public void should_display_status_with_health_up_with_excludes () throws Exception {
        Map<String,String> map = new HashMap();
        map.put("test","testMessage");
        Health.Builder builder = new Health.Builder(Status.UP, map);
        Health health = builder.build();

        StatusController controllerSpy = spy(controller);
        doReturn(health).when(controllerSpy).filteredDetails(any(), any(List.class));

        ResponseEntity<Health> response = controllerSpy.status(Optional.of(Lists.newArrayList("test1", "test2")));

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatusCode()== HttpStatus.OK);
        Assert.assertEquals(health, response.getBody());
    }

    @Test
    public void should_display_status_with_health_error_without_excludes () throws Exception {
        Map<String,String> map = new HashMap();
        map.put("test","testMessage");
        Health.Builder builder = new Health.Builder(Status.DOWN, map);
        Health health = builder.build();

        StatusController controllerSpy = spy(controller);
        doReturn(health).when(controllerSpy).filteredDetails(any(), any(List.class));

        ResponseEntity<Health> response = controllerSpy.status(Optional.of(Lists.newArrayList("test1", "test2")));

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatusCode()== HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(health, response.getBody());
    }

    @Test
    public void should_filter_details_with_no_excludes () throws Exception {
        Health.Builder builder = new Health.Builder(Status.DOWN);
        Health healthChild = builder.build();
        Health.Builder builder2 = new Health.Builder(Status.DOWN);
        builder2.withDetail("healthChild",healthChild);
        Health health = builder2.build();

        StatusController controllerSpy = spy(controller);
        Health healthResponse = controllerSpy.filteredDetails(health, Lists.newArrayList());
        Assert.assertNotNull(healthResponse);
        Assert.assertEquals(healthResponse, health);

    }

    @Test
    public void should_filter_details_with_excludes () throws Exception {
        Health.Builder builder = new Health.Builder(Status.DOWN);
        Health healthChild = builder.build();
        Health.Builder builder2 = new Health.Builder(Status.DOWN);
        builder2.withDetail("healthChild",healthChild);
        Health health = builder2.build();

        StatusController controllerSpy = spy(controller);
        Health healthResponse = controllerSpy.filteredDetails(health, Lists.newArrayList("healthChild"));
        Assert.assertNotNull(healthResponse);
        Assert.assertTrue(healthResponse.getDetails().isEmpty());

    }


}
