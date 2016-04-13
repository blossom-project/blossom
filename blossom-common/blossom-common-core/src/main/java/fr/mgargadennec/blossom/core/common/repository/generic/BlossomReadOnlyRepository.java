package fr.mgargadennec.blossom.core.common.repository.generic;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface BlossomReadOnlyRepository<T> extends Repository<T, Long> {

  T findOne(Long id);

  boolean exists(Long id);
  
  List<T> findAll();
  
  List<T> findAll(Iterable<Long> ids);
  
  long count();
  
  List<T> findAll(Sort sort);
  
  Page<T> findAll(Pageable pageable);
  
  <S extends T> S save(S entity);
  
  <S extends T> Iterable<S> save(Iterable<S> entities);  

}