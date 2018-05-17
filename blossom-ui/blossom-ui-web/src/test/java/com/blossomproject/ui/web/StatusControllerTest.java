package com.blossomproject.ui.web;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class StatusControllerTest {

  @Mock
  HealthEndpoint healthEndpoint;

  @InjectMocks
  @Spy
  private StatusController controller;

  @Test
  public void should_display_all_status_with_health_up() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("test", "testMessage");
    Health.Builder builder = new Health.Builder(Status.UP, map);
    Health health = builder.build();

    doReturn(health).when(controller).filteredDetails(any(), any(List.class));

    ResponseEntity<Health> response = controller.status(Optional.empty(), Optional.empty());

    assertNotNull(response);
    assertTrue(response.getStatusCode() == HttpStatus.OK);
    assertEquals(health, response.getBody());
  }

  @Test
  public void should_display_all_status_with_health_down() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("test", "testMessage");
    Health.Builder builder = new Health.Builder(Status.DOWN, map);
    Health health = builder.build();

    doReturn(health).when(controller).filteredDetails(any(), any(List.class));

    ResponseEntity<Health> response = controller.status(Optional.empty(), Optional.empty());

    assertNotNull(response);
    assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    assertEquals(health, response.getBody());
  }

  @Test
  public void should_display_status_up_with_excludes() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("test", "testMessage");
    Health.Builder builder = new Health.Builder(Status.UP, map);
    Health health = builder.build();

    doReturn(health).when(controller).filteredDetails(any(), any(List.class));

    ResponseEntity<Health> response = controller.status(Optional.of(Lists.newArrayList("test1", "test2")), Optional.empty());

    assertNotNull(response);
    assertTrue(response.getStatusCode() == HttpStatus.OK);
    assertEquals(health, response.getBody());
  }

  @Test
  public void should_display_status_down_without_excludes() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("test", "testMessage");
    Health.Builder builder = new Health.Builder(Status.DOWN, map);
    Health health = builder.build();

    doReturn(health).when(controller).filteredDetails(any(), any(List.class));

    ResponseEntity<Health> response = controller.status(Optional.of(Lists.newArrayList("test1", "test2")), Optional.empty());

    assertNotNull(response);
    assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    assertEquals(health, response.getBody());
  }

  @Test
  public void should_filter_details_without_excludes() throws Exception {
    Health.Builder builder = new Health.Builder(Status.DOWN);
    Health healthChild = builder.build();
    Health.Builder builder2 = new Health.Builder(Status.DOWN);
    builder2.withDetail("healthChild", healthChild);
    Health health = builder2.build();

    Health healthResponse = controller.filteredDetails(health, Lists.newArrayList());
    assertNotNull(healthResponse);
    assertEquals(health, healthResponse);

  }

  @Test
  public void should_filter_details_with_excludes() throws Exception {
    Health.Builder builder = new Health.Builder(Status.DOWN);
    Health healthChild = builder.build();
    Health.Builder builder2 = new Health.Builder(Status.DOWN);
    builder2.withDetail("healthChild", healthChild);
    Health health = builder2.build();

    Health healthResponse = controller.filteredDetails(health, Lists.newArrayList("healthChild"));
    assertNotNull(healthResponse);
    assertTrue(healthResponse.getDetails().isEmpty());

  }

  private Health buildTestHealth() {
    HealthAggregator aggregator = new OrderedHealthAggregator();

    Health healthLeafDown = Health.down().build();
    Health healthLeafUp = Health.up().build();

    Map<String, Health> downSubRootChildren = new HashMap<>();
    downSubRootChildren.put("healthLeafDown", healthLeafDown);
    downSubRootChildren.put("healthLeafUp", healthLeafUp);
    Health downSubRoot = aggregator.aggregate(downSubRootChildren);

    Map<String, Health> upSubRootChildren = new HashMap<>();
    upSubRootChildren.put("healthLeafUp", healthLeafUp);
    upSubRootChildren.put("healthLeafStillUp", healthLeafUp);
    Health upSubRoot = aggregator.aggregate(upSubRootChildren);

    Map<String, Health> level2SubRootMap = new HashMap<>();
    level2SubRootMap.put("downSubRoot", downSubRoot);
    level2SubRootMap.put("upSubRoot", upSubRoot);
    Health level2SubRoot = aggregator.aggregate(level2SubRootMap);

    Map<String, Health> returnMap = new HashMap<>();
    returnMap.put("level2SubRoot", level2SubRoot);
    returnMap.put("upSubRoot", upSubRoot);
    return aggregator.aggregate(returnMap);
  }

  @Test
  public void should_display_status_down_without_includes() {
    Health health = buildTestHealth();
    doReturn(health).when(healthEndpoint).health();
    ResponseEntity<Health> response = controller.status(Optional.empty(), Optional.empty());

    assertNotNull(response);
    assertSame(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    assertEquals(health, response.getBody());
  }

  @Test
  public void should_display_status_up_with_includes() {
    Health health = buildTestHealth();
    doReturn(health).when(healthEndpoint).health();
    ResponseEntity<Health> response = controller.status(Optional.empty(), Optional.of(Lists.newArrayList("upSubRoot")));

    assertNotNull(response);
    assertSame(response.getStatusCode(), HttpStatus.OK);
    assertTrue(response.getBody().getDetails().keySet().contains("upSubRoot"));
    assertFalse(response.getBody().getDetails().keySet().contains("level2SubRoot"));
  }

  @Test
  public void should_display_status_up_with_includes_and_exlcludes() {
    Health health = buildTestHealth();
    doReturn(health).when(healthEndpoint).health();
    ResponseEntity<Health> response = controller.status(Optional.of(Lists.newArrayList("healthLeafDown")), Optional.of(Lists.newArrayList("level2SubRoot.downSubRoot")));

    assertNotNull(response);
    assertSame(response.getStatusCode(), HttpStatus.OK);
    assertFalse(response.getBody().getDetails().keySet().contains("upSubRoot"));
    assertTrue(response.getBody().getDetails().keySet().contains("level2SubRoot"));
    assertFalse(((Health)response.getBody().getDetails().get("level2SubRoot")).getDetails().keySet().contains("upSubRoot"));
  }

  @Test
  public void should_display_status_up_with_includes_leaf() {
    Health health = buildTestHealth();
    doReturn(health).when(healthEndpoint).health();
    ResponseEntity<Health> response = controller.status(Optional.empty(), Optional.of(Lists.newArrayList("level2SubRoot.downSubRoot.healthLeafUp")));

    assertNotNull(response);
    assertSame(response.getStatusCode(), HttpStatus.OK);
    assertFalse(response.getBody().getDetails().keySet().contains("upSubRoot"));
    assertTrue(response.getBody().getDetails().keySet().contains("level2SubRoot"));
    assertTrue(((Health)response.getBody().getDetails().get("level2SubRoot")).getDetails().keySet().contains("downSubRoot"));
  }

  @Test
  public void should_display_root_if_leaf_health() {
    Health health = Health.up().build();
    doReturn(health).when(healthEndpoint).health();

    ResponseEntity<Health> response = controller.status(Optional.empty(), Optional.of(Lists.newArrayList("")));

    assertNotNull(response);
    assertSame(response.getStatusCode(), HttpStatus.OK);
    assertTrue(response.getBody().getDetails().isEmpty());

  }


}
