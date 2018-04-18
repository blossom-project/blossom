package com.blossomproject.core.common.search;

import com.blossomproject.core.common.search.facet.Facet;
import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Created by Maël Gargadennnec on 23/05/2017.
 */
public class SearchResult<DTO> {
  private final long duration;
  private final Page<DTO> page;
  private final List<Facet> facets;

  public SearchResult(long duration,Page<DTO> page) {
    this(duration, page, Lists.newArrayList());
  }

  public SearchResult(long duration, Page<DTO> page, List<Facet> facets) {
    this.duration=duration;
    this.page = page;
    this.facets = facets;
  }

  public long getDuration() {
    return duration;
  }

  public Page<DTO> getPage() {
    return page;
  }

  public List<Facet> getFacets() {
    return facets;
  }
}
