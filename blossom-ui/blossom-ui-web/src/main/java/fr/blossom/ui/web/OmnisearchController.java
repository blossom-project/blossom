package fr.blossom.ui.web;

import com.google.common.collect.Lists;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngine;
import fr.blossom.core.common.search.SearchResult;
import fr.blossom.ui.stereotype.BlossomController;
import java.util.List;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@BlossomController
public class OmnisearchController {

  private final Client client;
  private final PluginRegistry<SearchEngine, Class<? extends AbstractDTO>> registry;

  public OmnisearchController(Client client,
    PluginRegistry<SearchEngine, Class<? extends AbstractDTO>> registry) {
    this.client = client;
    this.registry = registry;
  }

  @ResponseBody
  @GetMapping("/_search")
  public List<SearchResult<?>> omniSearch(@RequestParam(value = "q") String query) {
    Pageable pageable = new PageRequest(0, 5);
    MultiSearchRequestBuilder request = client.prepareMultiSearch();
    List<SearchEngine> plugins = registry.getPlugins();
    plugins.forEach(engine -> request.add(engine.prepareSearch(query, pageable)));
    MultiSearchResponse response = request.get(TimeValue.timeValueSeconds(15));

    // You will get all individual responses from MultiSearchResponse#getResponses()
    int index = 0;
    List<SearchResult<?>> results = Lists.newArrayList();
    for (MultiSearchResponse.Item item : response.getResponses()) {
      SearchResponse unitResponse = item.getResponse();
      SearchResult<?> result = plugins.get(index).parseResults(unitResponse, pageable);
      results.add(result);
      index++;
    }

    return results;
  }
}
