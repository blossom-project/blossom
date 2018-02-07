package com.blossom_project.core.common;

import com.querydsl.core.types.Predicate;
import org.springframework.context.annotation.Configuration;

@Configuration
public interface PredicateProvider {

  Predicate getPredicate();

}
