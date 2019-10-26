package publisaiz.functionalities.scraped;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publisaiz.entities.ScrapedEmail;

import java.util.Set;

@Repository
interface ScrapedEmailRepository extends JpaRepository<ScrapedEmail, Long> {
    Set<ScrapedEmail> findByEmail(String email);

    Set<ScrapedEmail> findByEmail(Set<String> email);
}