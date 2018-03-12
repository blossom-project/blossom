package com.blossomproject.ui.web;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngine;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OmnisearchControllerTest {

    @Mock
    private Client client;

    @Mock
    private PluginRegistry<SearchEngine, Class<? extends AbstractDTO>> registry;

    private OmnisearchController controller;

    @Before
    public void setUp() {
        controller = new OmnisearchController(client, registry);

    }

    @Test
    public void should_display_empty_paged_result_without_plugin() throws Exception {
        OmnisearchController spyController = spy(controller);
        when(controller.filteredPlugins())
                .thenReturn(new ArrayList<SearchEngine>());
        ModelAndView response = spyController.omniSearch("test", mock(Pageable.class), new ExtendedModelMap());
        verify(spyController, times(1)).filteredPlugins();
        Assert.assertNotNull(response);
        assertTrue(response.getViewName().equals("omnisearch/omnisearch"));
        assertTrue(response.getModel().isEmpty());
    }

    @Test
    public void should_filter_and_return_empty_plugins_with_no_plugins() throws Exception {
        when(registry.getPlugins()).thenReturn(new ArrayList<SearchEngine>());
        List<SearchEngine> searchEngines = controller.filteredPlugins();
        verify(registry, times(1)).getPlugins();

        Assert.assertNotNull(searchEngines);
        assertTrue(searchEngines.isEmpty());
    }

    @Test
    public void should_filter_and_return_empty_plugins_with_no_plugins_includeInOmnisearch() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        when(searchEngine.includeInOmnisearch()).thenReturn(false);

        SearchEngine searchEngine2 = mock(SearchEngine.class);
        when(searchEngine2.includeInOmnisearch()).thenReturn(false);

        when(registry.getPlugins()).thenReturn(Lists.newArrayList(searchEngine, searchEngine2));
        List<SearchEngine> searchEngines = controller.filteredPlugins();
        verify(registry, times(1)).getPlugins();

        Assert.assertNotNull(searchEngines);
        assertTrue(searchEngines.isEmpty());
    }

    @Test
    public void should_filter_and_return_one_plugin_with_one_plugin_includeInOmnisearch() throws Exception {
        SearchEngine searchEngineNoOmniSearch = mock(SearchEngine.class);
        when(searchEngineNoOmniSearch.includeInOmnisearch()).thenReturn(false);

        SearchEngine searchEngineOmniSearch = mock(SearchEngine.class);
        when(searchEngineOmniSearch.includeInOmnisearch()).thenReturn(true);

        when(registry.getPlugins()).thenReturn(Lists.newArrayList(searchEngineNoOmniSearch, searchEngineOmniSearch));
        List<SearchEngine> searchEngines = controller.filteredPlugins();
        verify(registry, times(1)).getPlugins();

        Assert.assertNotNull(searchEngines);
        assertTrue(searchEngines.size() == 1);
    }

    @Test
    public void should_filter_and_return_plugins_with_plugins_includeInOmnisearch() throws Exception {
        SearchEngine searchEngineNoOmniSearch = mock(SearchEngine.class);
        when(searchEngineNoOmniSearch.includeInOmnisearch()).thenReturn(false);
        List<SearchEngine> searchEnginesMock = new ArrayList<>();
        searchEnginesMock.add(searchEngineNoOmniSearch);

        IntStream.range(0, 100).forEach(i -> {
            SearchEngine searchEngineOmniSearch = mock(SearchEngine.class);
            when(searchEngineOmniSearch.includeInOmnisearch()).thenReturn(true);
            searchEnginesMock.add(searchEngineOmniSearch);
        });

        when(registry.getPlugins()).thenReturn(searchEnginesMock);
        List<SearchEngine> searchEngines = controller.filteredPlugins();
        verify(registry, times(1)).getPlugins();

        Assert.assertNotNull(searchEngines);
        assertTrue(searchEngines.size() == 100);

        searchEnginesMock.remove(searchEngineNoOmniSearch);
        assertEquals(searchEnginesMock, searchEngines);

    }

    @Test
    public void should_filter_and_return_plugins_with_plugins_includeInOmnisearch_and_without() throws Exception {
        SearchEngine searchEngineNoOmniSearch = mock(SearchEngine.class);
        when(searchEngineNoOmniSearch.includeInOmnisearch()).thenReturn(false);
        List<SearchEngine> searchEnginesMock = new ArrayList<>();
        searchEnginesMock.add(searchEngineNoOmniSearch);

        IntStream.range(0, 100).forEach(i -> {
            SearchEngine searchEngineOmniSearch = mock(SearchEngine.class);
            when(searchEngineOmniSearch.includeInOmnisearch()).thenReturn(true);
            searchEnginesMock.add(searchEngineOmniSearch);
        });

        IntStream.range(0, 30).forEach(i -> {
            SearchEngine searchEngineOmniSearch = mock(SearchEngine.class);
            when(searchEngineOmniSearch.includeInOmnisearch()).thenReturn(false);
            searchEnginesMock.add(searchEngineOmniSearch);
        });

        when(registry.getPlugins()).thenReturn(searchEnginesMock);
        List<SearchEngine> searchEngines = controller.filteredPlugins();
        verify(registry, times(1)).getPlugins();

        Assert.assertNotNull(searchEngines);
        assertTrue(searchEngines.size() == 100);

        searchEnginesMock.remove(searchEngineNoOmniSearch);
        assertEquals(searchEnginesMock.stream().filter(searchEngine -> searchEngine.includeInOmnisearch()).collect(Collectors.toList()), searchEngines);
    }
}
