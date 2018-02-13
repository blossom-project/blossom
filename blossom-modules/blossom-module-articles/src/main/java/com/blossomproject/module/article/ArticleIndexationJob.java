package com.blossomproject.module.article;

import com.blossomproject.core.common.search.IndexationEngine;
import com.blossomproject.core.scheduler.IndexationJob;
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
