package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User> {

  Optional<User> findOneByIdentifier(String identifier);

  Optional<User> findOneByEmail(String email);

}
