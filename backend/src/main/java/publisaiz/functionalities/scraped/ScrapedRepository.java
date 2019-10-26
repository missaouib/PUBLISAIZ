package publisaiz.functionalities.scraped;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publisaiz.entities.ScrapedResource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
interface ScrapedRepository extends JpaRepository<ScrapedResource, Long> {
    Optional<ScrapedResource> findByUrl(String c);

    List<ScrapedResource> findByAccessDateNullOrAccessDateBefore(ZonedDateTime minusDays);

    Page<ScrapedResource> findAll(Pageable pageable);
}