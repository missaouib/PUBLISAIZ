package publisaiz.datasources.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.entities.Warning;

@Repository
@Transactional
public interface WarningsRepository extends CrudRepository<Warning, Long> {
    Warning findByWarned(User warned);
}
