/**
 *
 */
package fr.mgargadennec.blossom.core.common.dao.history;

import java.util.List;
import java.util.Map;

import org.hibernate.envers.RevisionType;
import org.hibernate.envers.internal.tools.Triple;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;

public interface IBlossomHistoryDAO {

  Map<RevisionType, List<BlossomAbstractEntity>> getEntityListGroupByRevisionType(Number revision);

  public <A extends BlossomAbstractEntity> List<Triple<A, BlossomRevisionEntity, RevisionType>> getRevisionListOfEntity(
      Class<A> blossomEntityClazz, Long id);

  public <A extends BlossomAbstractEntity> A getEntityAtPreviousRevision(Class<A> blossomEntityClazz, Long id, Number revision);

  public <A extends BlossomAbstractEntity> A getEntityAtNextRevision(Class<A> blossomEntityClazz, Long id, Number revision);

}
