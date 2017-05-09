package fr.mgargadennec.blossom.core.group;


import fr.mgargadennec.blossom.core.common.search.IndexationEngine;
import fr.mgargadennec.blossom.core.scheduler.IndexationJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class GroupIndexationJob extends IndexationJob {
  @Autowired
  @Qualifier("groupIndexationEngine")
  IndexationEngine groupIndexationEngine;

  @Override
  protected IndexationEngine getIndexationEngine() {
    return groupIndexationEngine;
  }
}
