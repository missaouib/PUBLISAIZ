package publisaiz.functionalities.scraped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@Lazy(false)
@Profile("prod")
class WebscrapingScheduler {

    private static final Logger logger = LoggerFactory.getLogger(WebscrapingScheduler.class);
    private final ScrapingService scrapingService;
    private final Webscraping webscraping = new Webscraping();

    private WebscrapingScheduler(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    @Scheduled(fixedDelay = 10000L)
    public void parse() {
        List<Scraped> all = scrapingService.findWhatShouldBeScrapedNow(ZonedDateTime.now().minusDays(1));
        logger.trace("found [{}] scraped resources in db.", all.size());
        all.stream()
                .filter(r -> shouldBeScrapedNow(r))
                .limit(10)
                .peek(r -> logger.trace("parsing now [{}]", r.getUrl()))
                .forEach(this::process);
        if (all.isEmpty()) {
            Scraped initial = new Scraped("https://news.google.com/?hl=en-US&gl=US&ceid=US:en");
            process(initial);
        }
        logger.trace("job done", scrapingService.findAll().size());
    }

    private boolean shouldBeScrapedNow(Scraped r) {
        return r.getAccessDate() == null || r.getAccessDate().isBefore(ZonedDateTime.now().minusDays(1));
    }

    private void process(Scraped initial) {
        if (initial != null && initial.getUrl() != null && initial.getUrl().length() > 5) {
            Scraped scraped = scrape(webscraping, initial);
            if (scraped != null) {
                scraped.getConnections().forEach(r -> {
                    Scraped saved = saveIfHasAnyContent(r);
                    if (saved != null)
                        scraped.addConnection(saved);
                });
                saveIfHasAnyContent(scraped);
            }
        }
    }

    private Scraped saveIfHasAnyContent(Scraped scraped) {
        if (hasContent(scraped))
            return scrapingService.save(scraped);
        else
            logger.trace("skipped saving: [{}]", scraped);
        return null;
    }

    private boolean hasContent(Scraped scraped) {
        return scraped != null
                && scraped.getUrl() != null
                && scraped.getContent() != null
                && scraped.getContent().length() > 0;
    }

    private Scraped scrape(Webscraping webscraping, Scraped r) {
        try {
            return webscraping.scrape(r);
        } catch (IOException e) {
            logger.trace("when scraped [{}] found error [{}]", r, e.getMessage());
        }
        return null;
    }

}
