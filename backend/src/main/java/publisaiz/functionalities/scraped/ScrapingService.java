package publisaiz.functionalities.scraped;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import publisaiz.entities.ScrapedEmail;
import publisaiz.entities.ScrapedResource;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
class ScrapingService {

    private final ScrapedRepository scrapedRepository;
    private final ScrapedEmailRepository scrapedEmailRepository;

    public ScrapingService(ScrapedRepository scrapedRepository, ScrapedEmailRepository scrapedEmailRepository) {
        this.scrapedRepository = scrapedRepository;
        this.scrapedEmailRepository = scrapedEmailRepository;
    }

    public Scraped save(ScrapedResource scrapedResource) {
        return new Scraped(scrapedRepository.save(scrapedResource));
    }

    public Scraped save(Scraped scraped) {
        return new Scraped(scrapedRepository.save(Scrapedresourcedto2Scrapedresource.scraped2Entity(scraped, this)));
    }

    public Optional<Scraped> findByUrl(String url) {
        return scrapedRepository.findByUrl(url).map(s -> new Scraped(s));
    }

    public Set<ScrapedEmail> getEmails(Set<String> email) {
        return scrapedEmailRepository.findByEmail(email);
    }

    public Set<Scraped> findAllOrCreateNew(Set<Scraped> connections) {
        return connections.stream()
                .map(c -> scrapedRepository.findByUrl(c.getUrl())
                        .orElse(new ScrapedResource(c.getUrl())))
                .map(s -> new Scraped(s))
                .collect(Collectors.toSet());
    }

    public List<Scraped> findAll() {
        return scrapedRepository.findAll()
                .stream()
                .map(r -> new Scraped(r))
                .collect(Collectors.toList());
    }

    public Optional<ScrapedResource> findEntityByUrl(String url) {
        return scrapedRepository.findByUrl(url);
    }

    public Set<ScrapedResource> findAllEntityOrCreateNew(Set<Scraped> connections) {
        return connections.stream()
                .map(c -> scrapedRepository.findByUrl(c.getUrl())
                        .orElse(new ScrapedResource(c.getUrl())))
                .collect(Collectors.toSet());
    }

    public List<Scraped> findWhatShouldBeScrapedNow(ZonedDateTime minusDays) {
        return scrapedRepository.findByAccessDateNullOrAccessDateBefore(minusDays)
                .stream().map(Scraped::new).collect(Collectors.toList());
    }

    public Page<Scraped> findAll(Pageable pageable) {
        return scrapedRepository.findAll(pageable).map(Scraped::new);
    }
}
