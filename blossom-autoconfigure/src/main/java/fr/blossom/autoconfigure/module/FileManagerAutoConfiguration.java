package fr.blossom.autoconfigure.module;

import fr.blossom.core.common.search.IndexationEngineConfiguration;
import fr.blossom.core.common.search.SearchEngineConfiguration;
import fr.blossom.core.common.search.SummaryDTO;
import fr.blossom.core.common.search.SummaryDTO.SummaryDTOBuilder;
import fr.blossom.module.article.ArticleDTO;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import fr.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.blossom.core.common.search.IndexationEngineImpl;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.module.filemanager.File;
import fr.blossom.module.filemanager.FileContentDao;
import fr.blossom.module.filemanager.FileContentDaoImpl;
import fr.blossom.module.filemanager.FileContentRepository;
import fr.blossom.module.filemanager.FileDTO;
import fr.blossom.module.filemanager.FileDTOMapper;
import fr.blossom.module.filemanager.FileDao;
import fr.blossom.module.filemanager.FileDaoImpl;
import fr.blossom.module.filemanager.FileIndexationJob;
import fr.blossom.module.filemanager.FileRepository;
import fr.blossom.module.filemanager.FileService;
import fr.blossom.module.filemanager.FileServiceImpl;
import fr.blossom.module.filemanager.digest.DigestUtil;
import fr.blossom.module.filemanager.digest.DigestUtilImpl;

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
  public FileService fileService(FileDao fileDao, FileDTOMapper fileDTOMapper, FileContentDao fileContentDao,
      DigestUtil digestUtil, ApplicationEventPublisher eventPublisher) {
    return new FileServiceImpl(fileDao, fileDTOMapper, fileContentDao, digestUtil, eventPublisher);
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
  public IndexationEngineConfiguration<FileDTO> fileIndexationEngineConfiguration(@Value("classpath:/elasticsearch/files.json") Resource resource) {
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
        return u -> SummaryDTOBuilder.create().id(u.getId()).type(this.getTypeFunction().apply(u)).name(u.getName()).description(u.getContentType()).uri("/blossom/content/files/"+u.getId()).build();
      }
    };
  }

  @Bean
  public IndexationEngineImpl<FileDTO> fileIndexationEngine(Client client, FileService fileService,
      BulkProcessor bulkProcessor, ObjectMapper objectMapper, IndexationEngineConfiguration<FileDTO> fileIndexationEngineConfiguration) {
    return new IndexationEngineImpl<>(client,fileService,bulkProcessor,objectMapper,fileIndexationEngineConfiguration);
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
  public SearchEngineImpl<FileDTO> fileSearchEngine(Client client, ObjectMapper objectMapper, SearchEngineConfiguration<FileDTO> fileSearchEngineConfiguration) {
    return new SearchEngineImpl<>(client, objectMapper, fileSearchEngineConfiguration);
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
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }
}
