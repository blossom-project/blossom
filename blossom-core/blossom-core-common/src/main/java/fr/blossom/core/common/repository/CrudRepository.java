package fr.blossom.core.common.repository;

import com.querydsl.core.types.Predicate;
import fr.blossom.core.common.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface CrudRepository<T extends AbstractEntity> extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {

    @Override
    List<T> findAll(Predicate predicate);

}
