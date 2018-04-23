package com.blossomproject.autoconfigure.ui;

import static com.blossomproject.autoconfigure.ui.WebContextAutoConfiguration.BLOSSOM_BASE_PATH;
import static com.blossomproject.ui.theme.Theme.BLOSSOM_THEME_NAME;
import static com.blossomproject.ui.theme.ThemeServlet.BLOSSOM_THEME_MAIL_SCSS_SERVLET;
import static com.blossomproject.ui.theme.ThemeServlet.BLOSSOM_THEME_SCSS_SERVLET;
import static org.springframework.web.servlet.support.RequestContext.DEFAULT_THEME_NAME;

import com.blossomproject.autoconfigure.ui.ThemeAutoConfiguration.BlossomThemeProperties;
import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.ui.theme.Theme;
import com.blossomproject.ui.theme.ThemeBuilder;
import com.blossomproject.ui.theme.ThemeBuilderImpl;
import com.blossomproject.ui.theme.ThemeCompiler;
import com.blossomproject.ui.theme.ThemeCompilerImpl;
import com.blossomproject.ui.theme.ThemeServlet;
import com.google.common.collect.Lists;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.theme.SessionThemeResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@Configuration
@EnablePluginRegistries({Theme.class})
@ConditionalOnWebApplication
@EnableConfigurationProperties(BlossomThemeProperties.class)
public class ThemeAutoConfiguration {

  @Autowired
  @Qualifier(value = PluginConstants.PLUGIN_THEME)
  private PluginRegistry<Theme, String> registry;

  @Autowired
  private BlossomThemeProperties blossomThemeProperties;

  @Bean
  @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
  public ThemeBuilder themeBuilder(Set<Locale> availableLocales) {
    return new ThemeBuilderImpl(availableLocales);
  }

  @Bean
  @ConditionalOnProperty(value = "blossom.theme.original.enabled", havingValue = "true", matchIfMissing = true)
  public Theme blossomTheme(ThemeBuilder themeBuilder) {
    return themeBuilder
      .nameAndAliases(BLOSSOM_THEME_NAME, DEFAULT_THEME_NAME)
      .scss().done()
      .bodyClass("md-skin")
      .logo("/blossom/public/img/blossom-flower-bright.svg")
      .build();
  }

  @Bean
  public ThemeCompiler themeCompiler(ResourceLoader resourceLoader) {
    return new ThemeCompilerImpl(registry, resourceLoader);
  }

  @Bean
  public ServletRegistrationBean themeServlet(ThemeResolver themeResolver, ThemeSource themeSource,
    ThemeCompiler themeCompiler) {
    ServletRegistrationBean registration = new ServletRegistrationBean();
    registration.setServlet(new ThemeServlet(themeResolver, themeSource, themeCompiler));
    registration.setUrlMappings(Lists.newArrayList(BLOSSOM_THEME_SCSS_SERVLET, BLOSSOM_THEME_MAIL_SCSS_SERVLET));
    registration.setLoadOnStartup(1);
    return registration;
  }

  @Configuration
  public static class ThemeWebAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    @Qualifier(value = PluginConstants.PLUGIN_THEME)
    private PluginRegistry<Theme, String> registry;

    @Autowired
    private BlossomThemeProperties blossomThemeProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(themeChangeInterceptor())
        .addPathPatterns("/" + BLOSSOM_BASE_PATH + "/**");
    }

    @Bean

    public ThemeSource themeSource() {
      return name -> registry.getPluginFor(name);
    }

    @Bean
    public ThemeResolver themeResolver() {
      SessionThemeResolver themeResolver = new SessionThemeResolver();
      themeResolver.setDefaultThemeName(blossomThemeProperties.getDefaultName());
      return themeResolver;
    }

    @Bean
    public ThemeChangeInterceptor themeChangeInterceptor() {
      ThemeChangeInterceptor tci = new ThemeChangeInterceptor();
      tci.setParamName("theme");
      return tci;
    }
  }

  @ConfigurationProperties(prefix = "blossom.theme")
  public static class BlossomThemeProperties {

    private String defaultName = BLOSSOM_THEME_NAME;

    private boolean originalEnabled = true;

    public String getDefaultName() {
      return defaultName;
    }

    public void setDefaultName(String defaultName) {
      this.defaultName = defaultName;
    }

    public boolean isOriginalEnabled() {
      return originalEnabled;
    }

    public void setOriginalEnabled(boolean originalEnabled) {
      this.originalEnabled = originalEnabled;
    }
  }
}
