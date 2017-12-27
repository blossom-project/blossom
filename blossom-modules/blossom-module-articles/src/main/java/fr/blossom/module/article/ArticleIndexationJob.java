package fr.blossom.module.article;

import fr.blossom.core.common.search.IndexationEngine;
import fr.blossom.core.scheduler.IndexationJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ArticleIndexationJob extends IndexationJob {
  @Autowired
  @Qualifier("articleIndexationEngine")
  IndexationEngine fileIndexationEngine;

  @Override
  protected IndexationEngine getIndexationEngine() {
    return fileIndexationEngine;
  }
}
