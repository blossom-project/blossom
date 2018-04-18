package com.blossomproject.autoconfigure.ui;

import com.blossomproject.ui.filter.FilterHandlerMethodArgumentResolver;
import com.blossomproject.ui.i18n.RestrictedSessionLocaleResolver;
import com.blossomproject.ui.stereotype.BlossomApiController;
import com.blossomproject.ui.stereotype.BlossomController;
import com.google.common.collect.Iterables;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore({WebMvcAutoConfiguration.class, ErrorMvcAutoConfiguration.class})
public class WebContextAutoConfiguration implements WebMvcConfigurer {

  public final static String BLOSSOM_BASE_PATH = "blossom";
  public final static String BLOSSOM_API_BASE_PATH = BLOSSOM_BASE_PATH + "/api";

  @Autowired
  private MessageSource messageSource;

  @Bean
  public LocaleResolver localeResolver(Set<Locale> availableLocales) {
    RestrictedSessionLocaleResolver resolver = new RestrictedSessionLocaleResolver(
      availableLocales);
    resolver.setDefaultLocale(Iterables.getFirst(availableLocales, Locale.ENGLISH));
    return resolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(filterHandlerMethodArgumentResolver());
  }

  @Bean
  public FilterHandlerMethodArgumentResolver filterHandlerMethodArgumentResolver(){
    return new FilterHandlerMethodArgumentResolver();
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("lang");
    return lci;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor())
      .addPathPatterns("/" + BLOSSOM_BASE_PATH + "/**", "/" + BLOSSOM_API_BASE_PATH + "/**");
  }

  @Override
  public Validator getValidator() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setValidationMessageSource(messageSource);
    return validator;
  }

  @Bean
  public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
    return new WebMvcRegistrations() {
      @Override
      public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping() {

          @Override
          protected void registerHandlerMethod(Object handler, Method method,
            RequestMappingInfo mapping) {
            Class<?> beanType = method.getDeclaringClass();
            if (AnnotationUtils.findAnnotation(beanType, BlossomController.class) != null) {
              mapping = computeMapping(mapping, BLOSSOM_BASE_PATH);
            } else if (AnnotationUtils.findAnnotation(beanType, BlossomApiController.class)
              != null) {
              mapping = computeMapping(mapping, BLOSSOM_API_BASE_PATH);
            }

            super.registerHandlerMethod(handler, method, mapping);
          }

          private RequestMappingInfo computeMapping(RequestMappingInfo mapping, String prefix) {
            PatternsRequestCondition apiPattern = new PatternsRequestCondition(prefix)
              .combine(mapping.getPatternsCondition());

            return new RequestMappingInfo(mapping.getName(), apiPattern,
              mapping.getMethodsCondition(),
              mapping.getParamsCondition(), mapping.getHeadersCondition(),
              mapping.getConsumesCondition(),
              mapping.getProducesCondition(), mapping.getCustomCondition());
          }
        };
      }
    };
  }
}
