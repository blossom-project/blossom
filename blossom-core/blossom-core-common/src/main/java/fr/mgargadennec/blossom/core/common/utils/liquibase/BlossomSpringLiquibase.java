package fr.mgargadennec.blossom.core.common.utils.liquibase;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.util.file.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class BlossomSpringLiquibase extends SpringLiquibase {

  private static final Logger logger = LoggerFactory.getLogger(BlossomSpringLiquibase.class);

  @Override
  protected SpringResourceOpener createResourceOpener() {
    return new BlossomSpringResourceOpener(getChangeLog());
  }


  public class BlossomSpringResourceOpener extends SpringResourceOpener {

    private String parentFile;

    public BlossomSpringResourceOpener(String parentFile) {
      super(parentFile);
      this.parentFile = parentFile;
    }

    @Override
    public Set<String> list(String relativeTo, String path, boolean includeFiles,
      boolean includeDirectories, boolean recursive) throws IOException {
      Set<String> returnSet = new HashSet<String>();
      if(path.startsWith("classpath*:")) {
        String tempFile = FilenameUtils.concat(FilenameUtils.getFullPath(relativeTo), path);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<String> resources = Lists
          .newArrayList(resolver.getResources(tempFile + "*.xml")).stream()
          .sorted(Comparator.comparing(Resource::getFilename))
          .map(resource -> "classpath:db/changelog/blossom/" + resource.getFilename())
          .collect(Collectors.toList());

        for (String res : resources) {
          returnSet.add(res);
        }

        return returnSet;
      }else{
        return super.list(relativeTo,path,includeFiles,includeDirectories,recursive);
      }
    }
  }
}
