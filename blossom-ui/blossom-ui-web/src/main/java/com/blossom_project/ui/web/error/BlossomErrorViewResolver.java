package com.blossom_project.ui.web.error;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;


public class BlossomErrorViewResolver implements ErrorViewResolver, Ordered {

  private static final Map<Series, String> SERIES_VIEWS;

  static {
    Map<Series, String> views = new HashMap<>();
    views.put(Series.CLIENT_ERROR, "4xx");
    views.put(Series.SERVER_ERROR, "5xx");
    SERIES_VIEWS = Collections.unmodifiableMap(views);
  }

  private ApplicationContext applicationContext;
  private final ResourceProperties resourceProperties;
  private final TemplateAvailabilityProviders templateAvailabilityProviders;
  private int order = Ordered.LOWEST_PRECEDENCE;

  public BlossomErrorViewResolver(ApplicationContext applicationContext,
    ResourceProperties resourceProperties) {
    Assert.notNull(applicationContext, "ApplicationContext must not be null");
    Assert.notNull(resourceProperties, "ResourceProperties must not be null");
    this.applicationContext = applicationContext;
    this.resourceProperties = resourceProperties;
    this.templateAvailabilityProviders = new TemplateAvailabilityProviders(
      applicationContext);
  }

  BlossomErrorViewResolver(ApplicationContext applicationContext,
    ResourceProperties resourceProperties,
    TemplateAvailabilityProviders templateAvailabilityProviders) {
    Assert.notNull(applicationContext, "ApplicationContext must not be null");
    Assert.notNull(resourceProperties, "ResourceProperties must not be null");
    this.applicationContext = applicationContext;
    this.resourceProperties = resourceProperties;
    this.templateAvailabilityProviders = templateAvailabilityProviders;
  }

  @Override
  public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status,
    Map<String, Object> model) {
    ModelAndView modelAndView = resolve(String.valueOf(status), model);
    if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
      modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
    }
    return modelAndView;
  }

  private ModelAndView resolve(String viewName, Map<String, Object> model) {
    String prefix = "";
    if (((String) model.getOrDefault("path", "")).startsWith("/blossom")) {
      prefix = "blossom/";
    }
    String errorViewName = "blossom/error/" + prefix + viewName;
    TemplateAvailabilityProvider provider = this.templateAvailabilityProviders
      .getProvider(errorViewName, this.applicationContext);
    if (provider != null) {
      return new ModelAndView(errorViewName, model);
    }
    errorViewName = "error/" + prefix + "default";
    provider = this.templateAvailabilityProviders.getProvider(errorViewName, this.applicationContext);
    if (provider != null) {
      return new ModelAndView(errorViewName, model);
    }
    return resolveResource(errorViewName, model);
  }

  private ModelAndView resolveResource(String viewName, Map<String, Object> model) {
    for (String location : this.resourceProperties.getStaticLocations()) {
      try {
        Resource resource = this.applicationContext.getResource(location);
        resource = resource.createRelative(viewName + ".html");
        if (resource.exists()) {
          return new ModelAndView(new BlossomErrorViewResolver.HtmlResourceView(resource), model);
        }
      } catch (Exception ex) {
      }
    }
    return null;
  }

  @Override
  public int getOrder() {
    return this.order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  private static class HtmlResourceView implements View {

    private Resource resource;

    HtmlResourceView(Resource resource) {
      this.resource = resource;
    }

    @Override
    public String getContentType() {
      return MediaType.TEXT_HTML_VALUE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      response.setContentType(getContentType());
      FileCopyUtils.copy(this.resource.getInputStream(),
        response.getOutputStream());
    }

  }

}
