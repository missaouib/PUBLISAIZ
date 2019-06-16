package publisaiz.datasources.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import publisaiz.datasources.database.entities.MailBox;
import publisaiz.datasources.database.entities.User;

import java.util.Set;

@Repository
public interface MailBoxRepository extends CrudRepository<MailBox, Long> {

    Set<MailBox> findByOwner(User owner);

}
