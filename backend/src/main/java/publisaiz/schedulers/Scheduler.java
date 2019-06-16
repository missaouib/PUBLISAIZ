package publisaiz.schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    Logger logger = LoggerFactory.getLogger(Scheduler.class);
    int i;

    @Scheduled(cron = "${cron.webcrawler.mojepanstwo:*/90 * * * * *}")
    public void parse() {
        logger.debug("scheduled scanning" + ++i);


    }

}
