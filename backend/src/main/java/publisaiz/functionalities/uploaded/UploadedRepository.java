package publisaiz.functionalities.uploaded;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publisaiz.entities.Uploaded;
import publisaiz.entities.User;

import java.util.Optional;

@Repository
interface UploadedRepository extends JpaRepository<Uploaded, Long> {
    Uploaded findByFileNameEndingWithAndHideFalse(String fileName);

    Page<Uploaded> findByOwnerAndHideFalse(User owner, Pageable pageable);

    Optional<Uploaded> findByIdAndHideFalse(Long id);
}
