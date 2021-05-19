package com.blossomproject.core.common.search;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import org.assertj.core.util.Lists;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.CollectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class SearchEngineImplTest {

  private SearchEngineImpl searchEngine;

  @Before
  public void setUp(){
    searchEngine = BDDMockito.spy(new SearchEngineImpl(null,null,null));
  }

  @Test
  public void sort_builders_should_have_score_sort_builder_at_last_place(){
    // ARRANGE
    Pageable pageable = PageRequest.of(0,50, Sort.by(Lists.newArrayList(Order.by("name"), Order.by("number"))));

    // ACT
    List<SortBuilder> sortBuilders = searchEngine.getSortBuilders(pageable);

    // ASSERT
    Assert.assertFalse(CollectionUtils.isEmpty(sortBuilders));
    Assert.assertEquals(3,sortBuilders.size());
    Assert.assertTrue(sortBuilders.get(sortBuilders.size() - 1) instanceof ScoreSortBuilder);

  }

  @Test
  public void sort_should_have_field_sort_based_on_dto_prefix() throws NoSuchFieldException, IllegalAccessException {
    // ARRANGE
    Pageable pageable = PageRequest.of(0,50, Sort.by(Lists.newArrayList(Order.by("name").with(Direction.ASC))));

    // ACT
    List<SortBuilder> sortBuilders = searchEngine.getSortBuilders(pageable);

    // ASSERT
    Assert.assertFalse(CollectionUtils.isEmpty(sortBuilders));
    Assert.assertTrue(sortBuilders.get(0) instanceof FieldSortBuilder);

    FieldSortBuilder nameSortBuilder = (FieldSortBuilder) sortBuilders.get(0);
    Field field = FieldSortBuilder.class.getDeclaredField("fieldName");
    field.setAccessible(true);
    Assert.assertEquals("dto.name",field.get(nameSortBuilder));


  }
}
