package fr.mgargadennec.blossom.autoconfigure.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.common.search.IndexationEngineImpl;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.module.article.*;
import fr.mgargadennec.blossom.module.filemanager.*;
import fr.mgargadennec.blossom.module.filemanager.digest.DigestUtil;
import fr.mgargadennec.blossom.module.filemanager.digest.DigestUtilImpl;
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
    public ArticleService articleService(ArticleDao articleDao, ArticleDTOMapper articleDTOMapper, ApplicationEventPublisher eventPublisher) {
        return new ArticleServiceImpl(articleDao, articleDTOMapper,eventPublisher);
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
    public IndexationEngineImpl<ArticleDTO> articleIndexationEngine(Client client, ArticleService articleService,
                                                              BulkProcessor bulkProcessor, ObjectMapper objectMapper,
                                                              @Value("classpath:/elasticsearch/articles.json") Resource resource) {
        return new IndexationEngineImpl<>(ArticleDTO.class, client, resource, "articles", u -> "article", articleService,
                bulkProcessor, objectMapper);
    }

    @Bean
    public SearchEngineImpl<ArticleDTO> articleSearchEngine(Client client, ObjectMapper objectMapper) {
        return new SearchEngineImpl<>(ArticleDTO.class, client, Lists.newArrayList("name"),
                "articles", objectMapper);
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
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }
}
