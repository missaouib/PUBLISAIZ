package publisaiz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publisaiz.datasources.database.entities.MailBox;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.MailBoxRepository;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class MailBoxService {

    private MailBoxRepository mailBoxRepository;

    @Autowired
    public MailBoxService(MailBoxRepository mailBoxRepository) {
        this.mailBoxRepository = mailBoxRepository;
    }

    public MailBox findById(Long id) {
        return mailBoxRepository.findById(id).orElse(new MailBox());
    }

    public Set<MailBox> findByUser(User user) {
        return mailBoxRepository.findByOwner(user);
    }

}