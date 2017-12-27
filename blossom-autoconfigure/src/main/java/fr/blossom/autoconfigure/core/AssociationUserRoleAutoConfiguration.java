package fr.blossom.autoconfigure.core;

import fr.blossom.core.association_user_role.AssociationUserRole;
import fr.blossom.core.association_user_role.AssociationUserRoleDTOMapper;
import fr.blossom.core.association_user_role.AssociationUserRoleDao;
import fr.blossom.core.association_user_role.AssociationUserRoleDaoImpl;
import fr.blossom.core.association_user_role.AssociationUserRoleRepository;
import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.association_user_role.AssociationUserRoleServiceImpl;
import fr.blossom.core.role.RoleDTOMapper;
import fr.blossom.core.user.UserDTOMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnClass(AssociationUserRole.class)
@AutoConfigureAfter({UserAutoConfiguration.class, RoleAutoConfiguration.class})
@EnableJpaRepositories(basePackageClasses = AssociationUserRoleRepository.class)
@EntityScan(basePackageClasses = AssociationUserRole.class)
public class AssociationUserRoleAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(AssociationUserRoleDao.class)
  public AssociationUserRoleDao associationUserRoleDao(AssociationUserRoleRepository repository) {
    return new AssociationUserRoleDaoImpl(repository);
  }

  @Bean
  @ConditionalOnMissingBean(AssociationUserRoleDTOMapper.class)
  public AssociationUserRoleDTOMapper associationUserRoleDTOMapper(UserDTOMapper userDTOMapper,
    RoleDTOMapper roleDTOMapper) {
    return new AssociationUserRoleDTOMapper(userDTOMapper, roleDTOMapper);
  }

  @Bean
  @ConditionalOnMissingBean(AssociationUserRoleService.class)
  public AssociationUserRoleService associationUserRoleService(AssociationUserRoleDao dao,
    AssociationUserRoleDTOMapper mapper, UserDTOMapper aMapper, RoleDTOMapper bMapper,
    ApplicationEventPublisher eventPublisher) {
    return new AssociationUserRoleServiceImpl(dao, mapper, aMapper, bMapper, eventPublisher);
  }

}
