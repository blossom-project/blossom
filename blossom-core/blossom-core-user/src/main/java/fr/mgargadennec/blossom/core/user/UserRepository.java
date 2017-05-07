package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User> {
}
