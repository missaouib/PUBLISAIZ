package publisaiz.functionalities.scraped;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import publisaiz.config.swagger.ApiPageable;

@RestController
@RequestMapping("api/scraped")
class ScrapedController {

    private final ScrapingService scrapingService;
    private final Logger logger = LoggerFactory.getLogger(ScrapedController.class);

    private ScrapedController(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    @GetMapping
    @ApiPageable
    public Page<Scraped> scrapedAll(Pageable pageable) {
        logger.info("request for [{}]", pageable);
        return scrapingService.findAll(pageable);
    }
}
