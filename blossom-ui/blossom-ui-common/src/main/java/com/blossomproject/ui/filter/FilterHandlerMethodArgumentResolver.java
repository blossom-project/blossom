/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blossomproject.ui.filter;

import com.blossomproject.core.common.search.facet.Facet.FacetType;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class FilterHandlerMethodArgumentResolver implements FilterArgumentResolver {

  private static final String DEFAULT_FILTER_PARAMETER = "filters";
  private static final Splitter doubleSlashSplitter = Splitter.on("//");
  private static final Splitter itemSplitter = Splitter.on("/");
  private static final Splitter semicolonSplitter = Splitter.on(";");
  private final String filterParameter;

  public FilterHandlerMethodArgumentResolver() {
    this(DEFAULT_FILTER_PARAMETER);
  }

  public FilterHandlerMethodArgumentResolver(String filterParameter) {
    this.filterParameter = filterParameter;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return QueryBuilder.class.equals(parameter.getParameterType());
  }

  @Nullable
  @Override
  public QueryBuilder resolveArgument(MethodParameter methodParameter,
    @Nullable ModelAndViewContainer mavContainer,
    NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {

    String filterString = webRequest.getParameter(filterParameter);
    if (Strings.isNullOrEmpty(filterString)) {
      return getDefaultFromAnnotationOrFallback(methodParameter).orElse(null);
    }

    Optional<QueryBuilder> parsed = parseAndApplyBoundaries(filterString);

    return parsed.orElse(null);
  }

  private Optional<QueryBuilder> parseAndApplyBoundaries(String filterString) {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    semicolonSplitter.splitToList(filterString).forEach(
      filter -> {
        List<String> filterData = doubleSlashSplitter.splitToList(filter);
        String filterName = filterData.get(0);
        if (filterData.size() == 3) {
          String filterType = filterData.get(1);
          if (filterType.equals(FacetType.TERMS.name())) {
            if (!Strings.isNullOrEmpty(filterData.get(2))) {
              List<String> filterValues = itemSplitter.splitToList(filterData.get(2));
              boolQueryBuilder.must(QueryBuilders.termsQuery(filterName, filterValues));
            }
          } else if (filterType.equals(FacetType.DATES.name())) {
            List<String> filterValues = itemSplitter.splitToList(filterData.get(2));
            String from = filterValues.get(0);
            String to = filterValues.get(1);
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(filterName);
            if (!Strings.isNullOrEmpty(from)) {
              rangeQueryBuilder.from(Long.parseLong(from));
            }
            if (!Strings.isNullOrEmpty(to)) {
              rangeQueryBuilder.to(Long.parseLong(to));
            }
            if (!Strings.isNullOrEmpty(from) && !Strings.isNullOrEmpty(to)) {
              boolQueryBuilder.must(rangeQueryBuilder);
            }
          }
        }
      }
    );
    if (boolQueryBuilder.hasClauses()) {
      return Optional.of(boolQueryBuilder);
    }
    return Optional.empty();
  }

  private Optional<QueryBuilder> getDefaultFromAnnotationOrFallback(
    MethodParameter methodParameter) {
    FilterDefault defaults = methodParameter.getParameterAnnotation(FilterDefault.class);

    if (defaults != null && defaults.filters() != null && !defaults.filters().isEmpty()) {
      return getFiltersFrom(defaults.filters());
    }

    return Optional.empty();
  }

  private Optional<QueryBuilder> getFiltersFrom(String filterString) {
    return Optional.ofNullable(null);
  }


}
