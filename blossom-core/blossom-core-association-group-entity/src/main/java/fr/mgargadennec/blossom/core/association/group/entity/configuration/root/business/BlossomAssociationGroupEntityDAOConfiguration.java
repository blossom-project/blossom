package fr.mgargadennec.blossom.core.association.group.entity.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.group.entity.dao.IBlossomAssociationGroupEntityDAO;
import fr.mgargadennec.blossom.core.association.group.entity.dao.impl.BlossomAssociationGroupEntityDAOImpl;
import fr.mgargadennec.blossom.core.association.group.entity.repository.BlossomAssociationGroupEntityRepository;

@Configuration
public class BlossomAssociationGroupEntityDAOConfiguration {

  @Bean
  public IBlossomAssociationGroupEntityDAO blossomAssociationGroupEntityDao(
      BlossomAssociationGroupEntityRepository boRoleRepository) {
    return new BlossomAssociationGroupEntityDAOImpl(boRoleRepository);
  }

}
