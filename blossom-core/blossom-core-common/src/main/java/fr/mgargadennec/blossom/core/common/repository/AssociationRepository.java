package fr.mgargadennec.blossom.core.common.repository;

import fr.mgargadennec.blossom.core.common.entity.AbstractAssociationEntity;
import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AssociationRepository<A extends AbstractEntity, B extends AbstractEntity, ASSOCIATION extends AbstractAssociationEntity<A, B>> extends JpaRepository<ASSOCIATION, Long>, QueryDslPredicateExecutor<ASSOCIATION> {

    ASSOCIATION findOneByAAndB(A a, B b);

    List<ASSOCIATION> findAllByA(A a);

    List<ASSOCIATION> findAllByB(B b);

}
