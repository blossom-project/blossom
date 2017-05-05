package fr.mgargadennec.blossom.core.common.search;


import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.plugin.core.Plugin;

public interface SearchEngine extends Plugin<Class<? extends AbstractDTO>> {

  Page<?> search(String q, Pageable pageable);

  Page<?> search(String q, Pageable pageable, Iterable<QueryBuilder> filters);

}
