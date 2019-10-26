package publisaiz.functionalities.scraped;

import publisaiz.entities.ScrapedEmail;
import publisaiz.entities.ScrapedResource;

import java.util.Set;

class Scrapedresourcedto2Scrapedresource {

    static ScrapedResource scraped2Entity(Scraped scraped, ScrapingService scrapingService) {
        ScrapedResource scrapedResource = scrapingService.findEntityByUrl(scraped.getUrl())
                .orElse(new ScrapedResource(scraped.getUrl()));
        scrapedResource.setContent(scraped.getContent());
        Set<ScrapedEmail> newEmails = scrapingService.getEmails(scraped.getEmails());
        Set<ScrapedEmail> oldEmails = scrapedResource.getEmails();
        newEmails.addAll(oldEmails);
        final Set<ScrapedResource> oldConnections = scrapedResource.getConnections();
        final Set<ScrapedResource> newConnections = scrapingService.findAllEntityOrCreateNew(scraped.getConnections());
        newConnections.addAll(oldConnections);
        scrapedResource.setEmails(oldEmails);
        scrapedResource.setConnections(oldConnections);
        scrapedResource.setAccessDate(scraped.getAccessDate());
        return scrapedResource;
    }
}