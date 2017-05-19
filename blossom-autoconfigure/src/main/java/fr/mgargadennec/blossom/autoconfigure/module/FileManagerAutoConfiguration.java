package fr.mgargadennec.blossom.autoconfigure.module;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.module.filemanager.*;
import fr.mgargadennec.blossom.module.filemanager.store.DigestUtil;
import fr.mgargadennec.blossom.module.filemanager.store.DigestUtilImpl;
import fr.mgargadennec.blossom.module.filemanager.store.DiskFileStoreImpl;
import fr.mgargadennec.blossom.module.filemanager.store.FileStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@Configuration
@ConditionalOnClass({File.class})
@AutoConfigureAfter(CommonAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = FileRepository.class)
@EntityScan(basePackageClasses = File.class)
public class FileManagerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(FileStore.class)
  public FileStore fileStore(@Value("${blossom.filemanager.fs.root:}") String rootPath) {
    Path root = null;
    if (!StringUtils.isEmpty(rootPath)) {
      root = Paths.get(rootPath);
      if (!Files.exists(root) || !Files.isDirectory(root) || !Files.isWritable(root)) {
        throw new RuntimeException("Directory " + rootPath + " doesn't exists or is not a directory or is not writable !");
      }
    } else {
      try {
        root = Files.createTempDirectory("blossom");
      } catch (IOException e) {
        throw new RuntimeException("Cannot create a fileStore on disk directory !", e);
      }
    }
    return new DiskFileStoreImpl(root);
  }

  @Bean
  @ConditionalOnMissingBean(DigestUtil.class)
  public DigestUtil digestUtil() {
    return new DigestUtilImpl();
  }

  @Bean
  @ConditionalOnMissingBean(FileService.class)
  public FileService fileService(FileDao fileDao, FileDTOMapper fileDTOMapper, ApplicationEventPublisher eventPublisher, FileStore fileStore, DigestUtil digestUtil) {
    return new FileServiceImpl(fileDao, fileDTOMapper, fileStore, digestUtil, eventPublisher);
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

}
