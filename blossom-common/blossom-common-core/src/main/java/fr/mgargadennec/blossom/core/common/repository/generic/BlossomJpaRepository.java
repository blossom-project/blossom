package fr.mgargadennec.blossom.core.common.repository.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BlossomJpaRepository<T> extends JpaRepository<T, Long>, BlossomQueryDslListPredicateExecutor<T> {

}