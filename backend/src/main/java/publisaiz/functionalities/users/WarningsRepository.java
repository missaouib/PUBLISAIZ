package publisaiz.functionalities.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.entities.User;
import publisaiz.entities.Warning;

@Repository
@Transactional
interface WarningsRepository extends CrudRepository<Warning, Long> {
    Warning findByWarned(User warned);
}
