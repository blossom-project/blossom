package fr.mgargadennec.blossom.core.common.dao;

import fr.mgargadennec.blossom.core.common.entity.AbstractAssociationEntity;
import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;
import java.util.List;

public interface AssociationDao<A extends AbstractEntity, B extends AbstractEntity, ASSOCIATION extends AbstractAssociationEntity<A, B>> {

  ASSOCIATION associate(A a, B b);

  void dissociate(A a, B b);

  List<ASSOCIATION> getAllA(B b);

  List<ASSOCIATION> getAllB(A a);

  ASSOCIATION getOne(long id);

  ASSOCIATION getOne(A a, B b);
}
