package fr.mgargadennec.blossom.core.common.repository.generic;


import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

public interface BlossomQueryDslListPredicateExecutor<T> extends QueryDslPredicateExecutor<T> {

  List<T> findAll(Predicate predicate);

  List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

}
