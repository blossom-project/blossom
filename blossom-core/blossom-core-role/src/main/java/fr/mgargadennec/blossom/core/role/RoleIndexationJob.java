package fr.mgargadennec.blossom.core.role;


import fr.mgargadennec.blossom.core.common.search.IndexationEngine;
import fr.mgargadennec.blossom.core.scheduler.IndexationJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class RoleIndexationJob extends IndexationJob {
  @Autowired
  @Qualifier("roleIndexationEngine")
  IndexationEngine roleIndexationEngine;

  @Override
  protected IndexationEngine getIndexationEngine() {
    return roleIndexationEngine;
  }
}
