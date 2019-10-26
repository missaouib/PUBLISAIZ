package publisaiz.functionalities.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publisaiz.entities.User;
import publisaiz.entities.Warning;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Service
@Transactional
class WarningService {

    @Autowired
    private
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
