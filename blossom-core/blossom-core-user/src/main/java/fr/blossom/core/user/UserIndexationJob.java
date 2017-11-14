package fr.blossom.core.user;


import fr.blossom.core.common.search.IndexationEngine;
import fr.blossom.core.scheduler.IndexationJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class UserIndexationJob extends IndexationJob {
  @Autowired
  @Qualifier("userIndexationEngine")
  IndexationEngine userDTOIndexationEngine;

  @Override
  protected IndexationEngine getIndexationEngine() {
    return userDTOIndexationEngine;
  }
}
