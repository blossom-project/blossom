package fr.mgargadennec.blossom.autoconfigure.ui;

import fr.mgargadennec.blossom.ui.stereotype.BlossomApiController;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class WebContextAutoConfiguration {

  @Bean
  public WebMvcRegistrationsAdapter webMvcRegistrationsHandlerMapping() {
    return new WebMvcRegistrationsAdapter() {
      @Override
      public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping() {
          private final static String BLOSSOM_BASE_PATH = "blossom";
          private final static String BLOSSOM_API_BASE_PATH = BLOSSOM_BASE_PATH + "/api";

          @Override
          protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
            Class<?> beanType = method.getDeclaringClass();
            if (AnnotationUtils.findAnnotation(beanType, BlossomController.class) != null) {
              mapping = computeMapping(mapping, BLOSSOM_BASE_PATH);
            } else if (AnnotationUtils.findAnnotation(beanType, BlossomApiController.class) != null) {
              mapping = computeMapping(mapping, BLOSSOM_API_BASE_PATH);
            }

            super.registerHandlerMethod(handler, method, mapping);
          }

          private RequestMappingInfo computeMapping(RequestMappingInfo mapping, String prefix) {
            PatternsRequestCondition apiPattern = new PatternsRequestCondition(prefix).combine(mapping.getPatternsCondition());

            return new RequestMappingInfo(mapping.getName(), apiPattern,
              mapping.getMethodsCondition(), mapping.getParamsCondition(),
              mapping.getHeadersCondition(), mapping.getConsumesCondition(),
              mapping.getProducesCondition(), mapping.getCustomCondition());
          }
        };
      }
    };
  }

}
