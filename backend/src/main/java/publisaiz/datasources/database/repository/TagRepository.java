package publisaiz.datasources.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import publisaiz.datasources.database.entities.Tag;

import java.util.Set;

@Repository
public interface TagRepository extends CrudRepository<Tag, String> {

    Set<Tag> findAllByValue(Set<String> valueSet);

}