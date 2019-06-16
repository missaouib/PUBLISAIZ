package publisaiz.datasources.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.datasources.database.entities.User;

import java.util.Collection;
import java.util.Set;

@Repository
@Transactional

public interface UserRepository extends CrudRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {

    Collection<User> getByName(String username);

    Set<User> findAll();

    Collection<User> getBySurname(String surname);

    User getByLoginAndPasswordEncrypted(String login, String password);

    User getByLogin(String login);

    User getById(int user_id);
}
