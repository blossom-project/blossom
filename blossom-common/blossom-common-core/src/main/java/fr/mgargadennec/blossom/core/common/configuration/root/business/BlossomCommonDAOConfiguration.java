package fr.mgargadennec.blossom.core.common.configuration.root.business;

import org.hibernate.envers.AuditReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.dao.history.impl.BlossomHistoryDAOImpl;

@Configuration
public class BlossomCommonDAOConfiguration {

	@Bean
	public IBlossomHistoryDAO blossomHistoryDao(AuditReader auditReader) {
		return new BlossomHistoryDAOImpl(auditReader);
	}

}
