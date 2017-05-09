package fr.mgargadennec.blossom.core.user;


import fr.mgargadennec.blossom.core.common.search.IndexationEngine;
import fr.mgargadennec.blossom.core.scheduler.IndexationJob;
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
