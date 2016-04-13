/**
 *
 */
package fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl;

import javax.persistence.PostPersist;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.BlossomAutowireHelper;

public class BlossomRevisionListener implements RevisionListener {
	private BlossomScheduledHistoryExtractor blossomScheduledHistoryExtractor;

	public void newRevision(Object revisionEntity) {
		BlossomRevisionEntity entity = (BlossomRevisionEntity) revisionEntity;
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		entity.setUser(user);
	}

	@PostPersist
	public void newRevisionPostPersist(final BlossomRevisionEntity revision) {
		BlossomAutowireHelper.autowire(this, this.blossomScheduledHistoryExtractor);
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				blossomScheduledHistoryExtractor.indexOne(revision);
			}
		});
	}

}
