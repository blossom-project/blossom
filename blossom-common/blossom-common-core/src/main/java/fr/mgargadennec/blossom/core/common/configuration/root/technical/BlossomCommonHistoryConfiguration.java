package fr.mgargadennec.blossom.core.common.configuration.root.technical;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.impl.BlossomEntityDiffBuilderImpl;

@Configuration
public class BlossomCommonHistoryConfiguration {

	@Bean
	public AuditReader auditReader(EntityManagerFactory blossomEntityManagerFactory) {
		EntityManager entityManager = blossomEntityManagerFactory.createEntityManager();
		return AuditReaderFactory.get(entityManager);
	}

	@Bean
	public Javers blossomJaver() {
	 return JaversBuilder.javers().build();
	}

	@Bean
	public IBlossomEntityDiffBuilder blossomEntityDiffBuilder(Javers javers) {
		return new BlossomEntityDiffBuilderImpl(javers);
	}
}
