package com.blossomproject.core.role;

import com.blossomproject.core.common.repository.CrudRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role> {

  Optional<Role> findOneByName(String name);

}
