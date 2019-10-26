package publisaiz.functionalities.scraped;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;

class Webscraping {

    private static final Logger logger = LoggerFactory.getLogger(Webscraping.class);
    private static final Scrapers scrapers = new Scrapers();

    public Scraped scrape(Scraped toScrape) throws IOException {
        Document document;
        URL url = new URL(toScrape.getUrl());
        Scraped scraped = new Scraped(toScrape.getUrl());
        if (shouldRefresh(toScrape)) {
            logger.info("no cache - parsing");
            document = Jsoup.parse(url, 10000);
            scraped.setAccessDate(ZonedDateTime.now());
        } else {
            document = Jsoup.parse(toScrape.getContent());
        }
        logger.debug("downloaded document : [{}]", document.text());
        scrapers.scrapeLinks(scraped, document, url);
        scrapers.scrapeEmails(scraped, document);
        scrapers.scrapeContent(scraped, document);
        logger.info("scrapped content: [{}]", scraped.getContent());
        return scraped;
    }

    private boolean shouldRefresh(Scraped scrapedResource) {
        return scrapedResource.getContent() == null
                || scrapedResource.getContent().length() == 0
                || scrapedResource.getAccessDate() == null
                || scrapedResource.getAccessDate().isBefore(ZonedDateTime.now().minusDays(1));
    }


}
