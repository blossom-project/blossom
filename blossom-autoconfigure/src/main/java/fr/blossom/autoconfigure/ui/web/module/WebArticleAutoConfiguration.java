package fr.blossom.autoconfigure.ui.web.module;

import fr.blossom.autoconfigure.ui.common.privileges.ArticlePrivilegesConfiguration;
import fr.blossom.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.module.article.ArticleDTO;
import fr.blossom.module.article.ArticleService;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.content.article.ArticlesController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */

@Configuration
@ConditionalOnClass({ArticleService.class, ArticlesController.class})
@ConditionalOnBean(ArticleService.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(ArticlePrivilegesConfiguration.class)
public class WebArticleAutoConfiguration {

  private final ArticlePrivilegesConfiguration articlePrivilegesConfiguration;

  public WebArticleAutoConfiguration(
    ArticlePrivilegesConfiguration articlePrivilegesConfiguration) {
    this.articlePrivilegesConfiguration = articlePrivilegesConfiguration;
  }

  @Bean
    @Order(3)
    @ConditionalOnMissingBean(name = "contentMenuItem")
    public MenuItem contentMenuItem(MenuItemBuilder builder) {
        return builder
                .key("content")
                .label("menu.content")
                .icon("fa fa-book")
                .link("/blossom/content")
                .leaf(false)
                .build();
    }

    @Bean
    public MenuItem contentArticleMenuItem(MenuItemBuilder builder,
                                           @Qualifier("contentMenuItem") MenuItem contentMenuItem) {
        return builder
                .key("articles")
                .label("menu.content.articles")
                .link("/blossom/content/articles")
                .icon("fa fa-pencil")
                .order(0)
                .privilege(articlePrivilegesConfiguration.articleReadPrivilegePlugin())
                .parent(contentMenuItem)
                .build();
    }

    @Bean
    public ArticlesController articleManagerController(ArticleService articleService,
                                                       SearchEngineImpl<ArticleDTO> searchEngine) {
        return new ArticlesController(articleService, searchEngine);
    }
}
