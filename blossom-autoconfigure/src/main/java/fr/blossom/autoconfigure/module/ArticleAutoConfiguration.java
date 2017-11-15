package fr.blossom.autoconfigure.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.blossom.core.common.search.IndexationEngineConfiguration;
import fr.blossom.core.common.search.IndexationEngineImpl;
import fr.blossom.core.common.search.SearchEngineConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.common.search.SummaryDTO;
import fr.blossom.core.common.search.SummaryDTO.SummaryDTOBuilder;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.module.article.Article;
import fr.blossom.module.article.ArticleDTO;
import fr.blossom.module.article.ArticleDTOMapper;
import fr.blossom.module.article.ArticleDao;
import fr.blossom.module.article.ArticleDaoImpl;
import fr.blossom.module.article.ArticleIndexationJob;
import fr.blossom.module.article.ArticleRepository;
import fr.blossom.module.article.ArticleService;
import fr.blossom.module.article.ArticleServiceImpl;
import java.util.function.Function;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
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
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@Configuration
@ConditionalOnClass({Article.class})
@AutoConfigureAfter(CommonAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = ArticleRepository.class)
@EntityScan(basePackageClasses = Article.class)
public class ArticleAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(ArticleService.class)
  public ArticleService articleService(ArticleDao articleDao, ArticleDTOMapper articleDTOMapper,
    ApplicationEventPublisher eventPublisher) {
    return new ArticleServiceImpl(articleDao, articleDTOMapper, eventPublisher);
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
        return u -> SummaryDTOBuilder.create().id(u.getId()).type(this.getTypeFunction().apply(u)).name(u.getName()).uri("/blossom/content/articles/"+u.getId()).build();
      }
    };
  }

  @Bean
  public IndexationEngineImpl<ArticleDTO> articleIndexationEngine(Client client,
    ArticleService articleService,
    BulkProcessor bulkProcessor, ObjectMapper objectMapper,
    IndexationEngineConfiguration<ArticleDTO> articleIndexationEngineConfiguration) {
    return new IndexationEngineImpl<>(client, articleService, bulkProcessor, objectMapper, articleIndexationEngineConfiguration);
  }

  @Bean
  public SearchEngineConfiguration<ArticleDTO> articleSearchEngineConfiguration() {
    return new SearchEngineConfiguration<ArticleDTO>() {
      @Override
      public String getName() {
        return "articles.label";
      }

      @Override
      public Class<ArticleDTO> getSupportedClass() {
        return ArticleDTO.class;
      }

      @Override
      public String[] getFields() {
        return new String[]{"dto.name"};
      }

      @Override
      public String getAlias() {
        return "articles";
      }
    };
  }

  @Bean
  public SearchEngineImpl<ArticleDTO> articleSearchEngine(Client client, ObjectMapper objectMapper, SearchEngineConfiguration<ArticleDTO> articleSearchEngineConfiguration) {
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
    factoryBean.setStartDelay(0);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(
      SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }
}
