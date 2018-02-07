package com.blossom_project.module.article;

import com.blossom_project.core.common.search.IndexationEngine;
import com.blossom_project.core.scheduler.IndexationJob;
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
