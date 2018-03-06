package com.blossomproject.autoconfigure.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.blossomproject.autoconfigure.core.CommonAutoConfiguration;
import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.IndexationEngineConfiguration;
import com.blossomproject.core.common.search.IndexationEngineImpl;
import com.blossomproject.core.common.search.SearchEngineConfiguration;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SummaryDTO;
import com.blossomproject.core.common.search.SummaryDTO.SummaryDTOBuilder;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import com.blossomproject.module.article.Article;
import com.blossomproject.module.article.ArticleDTO;
import com.blossomproject.module.article.ArticleDTOMapper;
import com.blossomproject.module.article.ArticleDao;
import com.blossomproject.module.article.ArticleDaoImpl;
import com.blossomproject.module.article.ArticleIndexationJob;
import com.blossomproject.module.article.ArticleRepository;
import com.blossomproject.module.article.ArticleService;
import com.blossomproject.module.article.ArticleServiceImpl;
import java.util.function.Function;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@Configuration
@ConditionalOnClass(Article.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = ArticleRepository.class)
@EntityScan(basePackageClasses = Article.class)
public class ArticleAutoConfiguration {

  @Qualifier(PluginConstants.PLUGIN_ASSOCIATION_SERVICE)
  @Autowired
  private PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationServicePlugins;


  @Bean
  @ConditionalOnMissingBean(ArticleService.class)
  public ArticleService articleService(ArticleDao articleDao, ArticleDTOMapper articleDTOMapper,
    ApplicationEventPublisher eventPublisher) {
    return new ArticleServiceImpl(articleDao, articleDTOMapper, eventPublisher,
      associationServicePlugins);
  }

  @Bean
  @ConditionalOnMissingBean(ArticleDao.class)
  public ArticleDao articleDao(ArticleRepository articleRepository) {
    return new ArticleDaoImpl(articleRepository);
  }

  @Bean
  @ConditionalOnMissingBean(ArticleDTOMapper.class)
  public ArticleDTOMapper articleDTOMapper() {
    return new ArticleDTOMapper();
  }

  @Bean
  public IndexationEngineConfiguration<ArticleDTO> articleIndexationEngineConfiguration(
    @Value("classpath:/elasticsearch/articles.json") Resource resource) {
    return new IndexationEngineConfiguration<ArticleDTO>() {
      @Override
      public Class<ArticleDTO> getSupportedClass() {
        return ArticleDTO.class;
      }

      @Override
      public Resource getSource() {
        return resource;
      }

      @Override
      public String getAlias() {
        return "articles";
      }

      @Override
      public Function<ArticleDTO, String> getTypeFunction() {
        return u -> "article";
      }

      @Override
      public Function<ArticleDTO, SummaryDTO> getSummaryFunction() {
        return u -> SummaryDTOBuilder.create().id(u.getId()).type(this.getTypeFunction().apply(u))
          .name(u.getName()).uri("/blossom/content/articles/" + u.getId()).build();
      }
    };
  }

  @Bean
  public IndexationEngineImpl<ArticleDTO> articleIndexationEngine(Client client,
    ArticleService articleService,
    BulkProcessor bulkProcessor, ObjectMapper objectMapper,
    IndexationEngineConfiguration<ArticleDTO> articleIndexationEngineConfiguration) {
    return new IndexationEngineImpl<>(client, articleService, bulkProcessor, objectMapper,
      articleIndexationEngineConfiguration);
  }

  @Bean
  public SearchEngineConfiguration<ArticleDTO> articleSearchEngineConfiguration() {
    return new SearchEngineConfiguration<ArticleDTO>() {
      @Override
      public String getName() {
        return "menu.content.articles";
      }

      @Override
      public Class<ArticleDTO> getSupportedClass() {
        return ArticleDTO.class;
      }

      @Override
      public String[] getFields() {
        return new String[]{"dto.name","dto.description","dto.content","dto.viewable"};
      }

      @Override
      public String getAlias() {
        return "articles";
      }
    };
  }

  @Bean
  public SearchEngineImpl<ArticleDTO> articleSearchEngine(Client client,
    ObjectMapper objectMapper,
    SearchEngineConfiguration<ArticleDTO> articleSearchEngineConfiguration) {
    return new SearchEngineImpl<>(client, objectMapper, articleSearchEngineConfiguration);
  }

  @Bean
  @Qualifier("articleIndexationFullJob")
  public JobDetailFactoryBean articleIndexationFullJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(ArticleIndexationJob.class);
    factoryBean.setGroup("Indexation");
    factoryBean.setName("Articles Indexation Job");
    factoryBean.setDescription("Articles full indexation Job");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  @Qualifier("articleScheduledIndexationTrigger")
  public SimpleTriggerFactoryBean articleScheduledIndexationTrigger(
    @Qualifier("articleIndexationFullJob") JobDetail articleIndexationFullJob) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("Article re-indexation");
    factoryBean.setDescription("Periodic re-indexation of all articles of the application");
    factoryBean.setJobDetail(articleIndexationFullJob);
    factoryBean.setStartDelay((long) 30 * 1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(
      SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }

}
