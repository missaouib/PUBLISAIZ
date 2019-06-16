package publisaiz.datasources.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publisaiz.datasources.database.entities.Uploaded;
import publisaiz.datasources.database.entities.User;

@Repository
public interface UploadedRepository extends JpaRepository<Uploaded, Long> {
    Uploaded findByFileNameEndingWith(String fileName);

    Page<Uploaded> findByOwner(User owner, Pageable pageable);
}
