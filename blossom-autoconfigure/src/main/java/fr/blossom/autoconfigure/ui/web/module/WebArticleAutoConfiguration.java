package fr.blossom.autoconfigure.ui.web.module;

import fr.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.module.article.ArticleDTO;
import fr.blossom.module.article.ArticleService;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.content.article.ArticlesController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */

@Configuration
@ConditionalOnBean(ArticleService.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebArticleAutoConfiguration {

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
      .privilege(articleReadPrivilegePlugin())
      .parent(contentMenuItem)
      .build();
  }

  @Bean
  public ArticlesController articleManagerController(ArticleService articleService,
    SearchEngineImpl<ArticleDTO> searchEngine) {
    return new ArticlesController(articleService, searchEngine);
  }

  @Bean
  public Privilege articleReadPrivilegePlugin() {
    return new SimplePrivilege("content", "articles", "read");
  }

  @Bean
  public Privilege articleManagerWritePrivilegePlugin() {
    return new SimplePrivilege("content", "articles", "write");
  }

  @Bean
  public Privilege articleManagerCreatePrivilegePlugin() {
    return new SimplePrivilege("content", "articles", "create");
  }

  @Bean
  public Privilege articleManagerDeletePrivilegePlugin() {
    return new SimplePrivilege("content", "articles", "delete");
  }
}
