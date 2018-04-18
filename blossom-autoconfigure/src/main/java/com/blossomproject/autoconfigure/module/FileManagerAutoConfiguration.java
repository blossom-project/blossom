package com.blossomproject.autoconfigure.module;

import com.blossomproject.core.common.search.SearchEngine;
import com.blossomproject.core.common.search.facet.AggregationConverter;
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
import com.blossomproject.module.filemanager.File;
import com.blossomproject.module.filemanager.FileContentDao;
import com.blossomproject.module.filemanager.FileContentDaoImpl;
import com.blossomproject.module.filemanager.FileContentRepository;
import com.blossomproject.module.filemanager.FileDTO;
import com.blossomproject.module.filemanager.FileDTOMapper;
import com.blossomproject.module.filemanager.FileDao;
import com.blossomproject.module.filemanager.FileDaoImpl;
import com.blossomproject.module.filemanager.FileIndexationJob;
import com.blossomproject.module.filemanager.FileRepository;
import com.blossomproject.module.filemanager.FileService;
import com.blossomproject.module.filemanager.FileServiceImpl;
import com.blossomproject.module.filemanager.digest.DigestUtil;
import com.blossomproject.module.filemanager.digest.DigestUtilImpl;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@Configuration
@ConditionalOnClass({File.class})
@AutoConfigureAfter(CommonAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = FileRepository.class)
@PropertySource("classpath:/filemanager.properties")
@EntityScan(basePackageClasses = File.class)
public class FileManagerAutoConfiguration {

  @Qualifier(PluginConstants.PLUGIN_ASSOCIATION_SERVICE)
  @Autowired
  private PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationServicePlugins;

  @Bean
  @ConditionalOnMissingBean(FileContentDao.class)
  public FileContentDao fileContentDao(FileContentRepository fileContentRepository) {
    return new FileContentDaoImpl(fileContentRepository);
  }

  @Bean
  @ConditionalOnMissingBean(DigestUtil.class)
  public DigestUtil digestUtil() {
    return new DigestUtilImpl();
  }

  @Bean
  @ConditionalOnMissingBean(FileService.class)
  public FileService fileService(FileDao fileDao, FileDTOMapper fileDTOMapper,
    FileContentDao fileContentDao,
    DigestUtil digestUtil, ApplicationEventPublisher eventPublisher) {
    return new FileServiceImpl(fileDao, fileDTOMapper, fileContentDao, digestUtil, eventPublisher,
      associationServicePlugins);
  }

  @Bean
  @ConditionalOnMissingBean(FileDao.class)
  public FileDao fileDao(FileRepository fileRepository) {
    return new FileDaoImpl(fileRepository);
  }

  @Bean
  @ConditionalOnMissingBean(FileDTOMapper.class)
  public FileDTOMapper fileDTOMapper() {
    return new FileDTOMapper();
  }

  @Bean
  public IndexationEngineConfiguration<FileDTO> fileIndexationEngineConfiguration(
    @Value("classpath:/elasticsearch/files.json") Resource resource) {
    return new IndexationEngineConfiguration<FileDTO>() {
      @Override
      public Class<FileDTO> getSupportedClass() {
        return FileDTO.class;
      }

      @Override
      public Resource getSource() {
        return resource;
      }

      @Override
      public String getAlias() {
        return "files";
      }

      @Override
      public Function<FileDTO, String> getTypeFunction() {
        return u -> "file";
      }

      @Override
      public Function<FileDTO, SummaryDTO> getSummaryFunction() {
        return u -> SummaryDTOBuilder.create().id(u.getId()).type(this.getTypeFunction().apply(u))
          .name(u.getName()).description(u.getContentType())
          .uri("/blossom/content/files/" + u.getId()).build();
      }
    };
  }

  @Bean
  public IndexationEngineImpl<FileDTO> fileIndexationEngine(Client client,
    FileService fileService,
    BulkProcessor bulkProcessor, ObjectMapper objectMapper,
    IndexationEngineConfiguration<FileDTO> fileIndexationEngineConfiguration) {
    return new IndexationEngineImpl<>(client, fileService, bulkProcessor, objectMapper,
      fileIndexationEngineConfiguration);
  }

  @Bean
  public SearchEngineConfiguration<FileDTO> fileSearchEngineConfiguration() {
    return new SearchEngineConfiguration<FileDTO>() {
      @Override
      public String getName() {
        return "menu.content.filemanager";
      }

      @Override
      public Class<FileDTO> getSupportedClass() {
        return FileDTO.class;
      }

      @Override
      public String[] getFields() {
        return new String[]{"dto.name", "dto.contentType", "dto.extension"};
      }

      @Override
      public String getAlias() {
        return "files";
      }

      @Override
      public boolean includeInOmnisearch() {
        return false;
      }
    };
  }


  @Bean
  public SearchEngineImpl<FileDTO> fileSearchEngine(Client client, ObjectMapper objectMapper,
    SearchEngineConfiguration<FileDTO> fileSearchEngineConfiguration,@Qualifier(PluginConstants.PLUGIN_SEARCH_ENGINE_AGGREGATION_CONVERTERS)
    PluginRegistry<AggregationConverter, SearchEngine> aggregationConverters) {
    return new SearchEngineImpl<>(client, objectMapper, aggregationConverters, fileSearchEngineConfiguration);
  }

  @Bean
  @Qualifier("fileIndexationFullJob")
  public JobDetailFactoryBean fileIndexationFullJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(FileIndexationJob.class);
    factoryBean.setGroup("Indexation");
    factoryBean.setName("Files Indexation Job");
    factoryBean.setDescription("Files full indexation Job");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  @Qualifier("fileScheduledIndexationTrigger")
  public SimpleTriggerFactoryBean fileScheduledIndexationTrigger(
    @Qualifier("fileIndexationFullJob") JobDetail fileIndexationFullJob) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("File re-indexation");
    factoryBean.setDescription("Periodic re-indexation of all files of the application");
    factoryBean.setJobDetail(fileIndexationFullJob);
    factoryBean.setStartDelay((long) 30 * 1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(
      SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }

}
