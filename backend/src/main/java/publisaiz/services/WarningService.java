package publisaiz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.entities.Warning;
import publisaiz.datasources.database.repository.WarningsRepository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Service
@Transactional
public class WarningService {

    @Autowired
    WarningsRepository warningsRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Warning getByUser(User user) {
        return warningsRepository.findByWarned(user);
    }

    public void save(Warning warning) {
        warningsRepository.save(warning);
    }

    public void setPresented(Warning warning) {
        warning.setPresented(ZonedDateTime.now());
        warningsRepository.save(warning);
    }
}
