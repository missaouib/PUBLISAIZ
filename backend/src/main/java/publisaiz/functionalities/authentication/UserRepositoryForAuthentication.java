package publisaiz.functionalities.authentication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.entities.User;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
interface UserRepositoryForAuthentication extends JpaRepository<User, Integer> {

    Page<User> getByName(String username, Pageable pageable);

    List<User> findAll();

    Collection<User> getBySurname(String surname);

    User getByLoginAndPasswordEncrypted(String login, String password);

    User getByLogin(String login);

    User getById(int user_id);

    @Query(value = "select distinct u from User u, Article a where a.author = u.id",
            countQuery = "select count(distinct u) from User u, Article a where a.author = u.id")
    Page<User> getAuthors(Pageable pageable);

}
