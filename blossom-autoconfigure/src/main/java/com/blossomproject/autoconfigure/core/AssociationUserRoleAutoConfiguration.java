package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.association_user_role.*;
import com.blossomproject.core.role.RoleDTOMapper;
import com.blossomproject.core.role.RoleRepository;
import com.blossomproject.core.user.UserDTOMapper;
import com.blossomproject.core.user.UserRepository;
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
    public AssociationUserRoleDao associationUserRoleDao(UserRepository userRepository,
            RoleRepository roleRepository,
            AssociationUserRoleRepository repository) {
        return new AssociationUserRoleDaoImpl(userRepository, roleRepository, repository);
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
