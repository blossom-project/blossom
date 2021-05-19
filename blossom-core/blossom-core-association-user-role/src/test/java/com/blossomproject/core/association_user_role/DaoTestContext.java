package com.blossomproject.core.association_user_role;

import com.blossomproject.core.role.Role;
import com.blossomproject.core.role.RoleDao;
import com.blossomproject.core.role.RoleDaoImpl;
import com.blossomproject.core.role.RoleRepository;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserDao;
import com.blossomproject.core.user.UserDaoImpl;
import com.blossomproject.core.user.UserRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by rlejolivet on 2018-08-08
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, RoleRepository.class, AssociationUserRoleRepository.class})
@EntityScan(basePackageClasses = {User.class, Role.class, AssociationUserRole.class})
@Import({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class DaoTestContext {

    @Bean
    public UserDao userDao(UserRepository userRepository) {
        return new UserDaoImpl(userRepository);
    }

    @Bean
    public RoleDao roleDao(RoleRepository roleRepository) {
        return new RoleDaoImpl(roleRepository);
    }

    @Bean
    public AssociationUserRoleDao associationUserRoleDao(UserRepository userRepository,
            RoleRepository roleRepository,
            AssociationUserRoleRepository repository) {
        return new AssociationUserRoleDaoImpl(userRepository, roleRepository, repository);
    }

}
