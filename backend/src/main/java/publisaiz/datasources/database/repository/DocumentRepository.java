package publisaiz.datasources.database.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import publisaiz.datasources.database.entities.Document;

@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {

}
