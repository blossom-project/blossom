package fr.blossom.autoconfigure.core;

import fr.blossom.core.common.utils.liquibase.BlossomSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
public class LiquibaseAutoConfiguration {

    @Configuration
    @EnableConfigurationProperties(LiquibaseProperties.class)
    @Import(LiquibaseJpaDependencyConfiguration.class)
    public static class LiquibaseConfiguration {

        private final LiquibaseProperties properties;

        private final ResourceLoader resourceLoader;

        public LiquibaseConfiguration(LiquibaseProperties properties, ResourceLoader resourceLoader) {
            this.properties = properties;
            this.resourceLoader = resourceLoader;
        }

        @PostConstruct
        public void checkChangelogExists() {
            if (this.properties.isCheckChangeLogLocation()) {
                Resource resource = this.resourceLoader
                        .getResource(this.properties.getChangeLog());
                Assert.state(resource.exists(),
                        "Cannot find changelog location: " + resource
                                + " (please add changelog or check your Liquibase "
                                + "configuration)");
            }
        }

        @Bean
        public SpringLiquibase liquibase(DataSource dataSource) {
            BlossomSpringLiquibase liquibase = new BlossomSpringLiquibase();
            liquibase.setDataSource(dataSource);
            liquibase.setChangeLog(this.properties.getChangeLog());
            liquibase.setContexts(this.properties.getContexts());
            liquibase.setDefaultSchema(this.properties.getDefaultSchema());
            liquibase.setDropFirst(this.properties.isDropFirst());
            liquibase.setShouldRun(this.properties.isEnabled());
            liquibase.setLabels(this.properties.getLabels());
            liquibase.setChangeLogParameters(this.properties.getParameters());
            liquibase.setRollbackFile(this.properties.getRollbackFile());
            return liquibase;
        }

    }

    /**
     * Additional configuration to ensure that {@link EntityManagerFactory} beans
     * depend-on the liquibase bean.
     */
    @Configuration
    @ConditionalOnClass(LocalContainerEntityManagerFactoryBean.class)
    @ConditionalOnBean(AbstractEntityManagerFactoryBean.class)
    protected static class LiquibaseJpaDependencyConfiguration
            extends EntityManagerFactoryDependsOnPostProcessor {

        public LiquibaseJpaDependencyConfiguration() {
            super("liquibase");
        }

    }
}
