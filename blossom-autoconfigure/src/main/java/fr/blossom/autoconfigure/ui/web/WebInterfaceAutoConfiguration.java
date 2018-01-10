package fr.blossom.autoconfigure.ui.web;

import fr.blossom.core.common.PluginConstants;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngine;
import fr.blossom.core.common.utils.action_token.ActionTokenService;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.current_user.CurrentUserControllerAdvice;
import fr.blossom.ui.i18n.LocaleControllerAdvice;
import fr.blossom.ui.menu.Menu;
import fr.blossom.ui.menu.MenuControllerAdvice;
import fr.blossom.ui.web.*;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.plugin.core.PluginRegistry;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(HomeController.class)
@PropertySource({"classpath:/languages.properties"})
public class WebInterfaceAutoConfiguration {

    @Bean
    public LoginController loginController() {
        return new LoginController();
    }

    @Bean
    public HomeController homeController() {
        return new HomeController();
    }

    @Bean
    public StatusController statusController(HealthEndpoint healthEndpoint) {
        return new StatusController(healthEndpoint);
    }

    @Bean
    public OmnisearchController searchController(Client client,
                                                 @Qualifier(PluginConstants.PLUGIN_SEARCH_ENGINE) PluginRegistry<SearchEngine, Class<? extends AbstractDTO>> registry) {
        return new OmnisearchController(client, registry);
    }

    @Bean
    public ActivationController activationController(ActionTokenService tokenService,
                                                     UserService userService) {
        return new ActivationController(tokenService, userService);
    }

    @Bean
    public CurrentUserControllerAdvice currentUserControllerAdvice() {
        return new CurrentUserControllerAdvice();
    }

    @Bean
    public ErrorControllerAdvice errorControllerAdvice() {
        return new ErrorControllerAdvice();
    }

    @Bean
    public MenuControllerAdvice menuControllerAdvice(Menu menu) {
        return new MenuControllerAdvice(menu);
    }

    @Bean
    public Set<Locale> availableLocales(@Value("${blossom.languages}") String[] languages) {
        return Stream.of(languages).sequential().map(language -> Locale.forLanguageTag(language))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Bean
    public LocaleControllerAdvice languageControllerAdvice(Set<Locale> availableLocales) {
        return new LocaleControllerAdvice(availableLocales);
    }
}
