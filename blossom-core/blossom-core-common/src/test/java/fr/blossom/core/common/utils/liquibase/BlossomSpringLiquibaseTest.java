package fr.blossom.core.common.utils.liquibase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import fr.blossom.core.common.utils.liquibase.BlossomSpringLiquibase.BlossomSpringResourceOpener;
import java.util.Set;
import liquibase.integration.spring.SpringLiquibase.SpringResourceOpener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@RunWith(MockitoJUnitRunner.class)
public class BlossomSpringLiquibaseTest {

  @Test
  public void should_create_resource_opener() throws Exception {
    BlossomSpringLiquibase blossomLiquibase = new BlossomSpringLiquibase();
    blossomLiquibase.setChangeLog("test");
    blossomLiquibase.setResourceLoader(new DefaultResourceLoader());
    SpringResourceOpener resourceOpener = blossomLiquibase.createResourceOpener();
    assertNotNull(resourceOpener);
    assertTrue(resourceOpener instanceof BlossomSpringResourceOpener);
  }


  @Test
  public void should_find_all_liquibase_in_classpath_star_files() throws Exception {
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    String changelog = "classpath*:/example";
    String relativeTo = "relativeTo";
    boolean includeFiles = true;
    boolean includeDirectories = true;
    boolean recursive = true;

    BlossomSpringLiquibase blossomLiquibase = new BlossomSpringLiquibase();
    blossomLiquibase.setChangeLog(changelog);
    blossomLiquibase.setResourceLoader(resourceLoader);
    SpringResourceOpener resourceOpener = blossomLiquibase.createResourceOpener();

    Set<String> result = resourceOpener.list(null, changelog, includeFiles, includeDirectories, recursive);

    assertNotNull(result);
  }

  @Test
  public void should_find_all_liquibase_files() throws Exception {
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    String changelog = "classpath:/example";
    String relativeTo = "relativeTo";
    boolean includeFiles = true;
    boolean includeDirectories = true;
    boolean recursive = true;

    BlossomSpringLiquibase blossomLiquibase = new BlossomSpringLiquibase();
    blossomLiquibase.setChangeLog(changelog);
    blossomLiquibase.setResourceLoader(resourceLoader);
    SpringResourceOpener resourceOpener = blossomLiquibase.createResourceOpener();

    Set<String> result = resourceOpener.list(null, changelog, includeFiles, includeDirectories, recursive);

    assertNotNull(result);
  }
}
