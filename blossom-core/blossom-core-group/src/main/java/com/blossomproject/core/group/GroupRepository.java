package com.blossomproject.core.group;

import com.blossomproject.core.common.repository.CrudRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface GroupRepository extends CrudRepository<Group> {

  Optional<Group> findOneByName(String name);

}
