package publisaiz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Component
public class Logged implements AuditorAware<String> {

    private final static Logger logger = LoggerFactory.getLogger(Logged.class);
    private final UserRepository userRepository;

    public Logged(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        User user = null;
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            user = userRepository.getByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user == null) return null;
            user.setLastLogged(ZonedDateTime.now());
            user.setPassword("transparent value");
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        String r = null;
        User u = getUser();
        if (u != null) r = u.getLogin();
        return Optional.ofNullable(r);
    }
}
