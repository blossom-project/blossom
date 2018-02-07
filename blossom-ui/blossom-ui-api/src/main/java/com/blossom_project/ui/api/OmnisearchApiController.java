package com.blossom_project.ui.api;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.search.SearchEngine;
import com.blossom_project.core.common.search.SearchResult;
import com.blossom_project.core.common.search.SummaryDTO;
import com.blossom_project.ui.stereotype.BlossomApiController;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@BlossomApiController
@RequestMapping("/search")
public class OmnisearchApiController {

  private final Client client;
  private final PluginRegistry<SearchEngine, Class<? extends AbstractDTO>> registry;

  public OmnisearchApiController(Client client,
    PluginRegistry<SearchEngine, Class<? extends AbstractDTO>> registry) {
    this.client = client;
    this.registry = registry;
  }

  @GetMapping
  public Map<String, Object> omniSearch(
    @RequestParam(value = "q", defaultValue = "", required = false) String query,
    @PageableDefault(size = 20) Pageable pageable) {
    List<SearchEngine> plugins = filteredPlugins();
    if (plugins.isEmpty()) {
      Map<String, Object> model = Maps.newHashMap();
      model.put("q", query);
      model.put("total", 0);
      model.put("duration", 0L);
      model.put("results", Maps.newHashMap());
      return model;
    }

    MultiSearchRequestBuilder request = client.prepareMultiSearch();
    plugins.forEach(engine -> request.add(engine.prepareSearch(query, pageable)));
    MultiSearchResponse response = request.get(TimeValue.timeValueSeconds(15));

    int index = 0;
    Map<String, SearchResult<SummaryDTO>> results = Maps.newHashMap();

    List<MultiSearchResponse.Item> items = Lists.newArrayList(response.getResponses());
    for (MultiSearchResponse.Item item : items) {
      SearchResponse unitResponse = item.getResponse();
      SearchEngine searchEngine = plugins.get(index);
      SearchResult<SummaryDTO> result = searchEngine.parseSummaryResults(unitResponse, pageable);
      results.put(searchEngine.getName(), result);
      index++;
    }

    Map<String, Object> model = Maps.newHashMap();
    model.put("q", query);
    model.put("total",
      results.values().stream().mapToLong(r -> r.getPage().getTotalElements()).sum());
    model.put("duration",
      results.values().stream().mapToLong(SearchResult::getDuration).max().getAsLong());
    model.put("results", results.entrySet().stream()
      .filter(e -> e.getValue().getPage().getTotalElements() != 0)
      .sorted(Comparator.comparing(
        (Entry<String, SearchResult<SummaryDTO>> e) -> e.getValue().getPage().getTotalElements())
        .reversed())
      .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (u, v) -> {
        throw new IllegalStateException(String.format("Duplicate key %s", u));
      }, LinkedHashMap::new)));

    return model;
  }

  @VisibleForTesting
  List<SearchEngine> filteredPlugins() {
    return registry.getPlugins().stream().filter(SearchEngine::includeInOmnisearch)
      .collect(Collectors.toList());
  }
}
