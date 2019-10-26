package publisaiz.config.logged;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.entities.User;

import java.time.ZonedDateTime;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Component
@Scope(value = SCOPE_SESSION, proxyMode = TARGET_CLASS)
public class Logged implements AuditorAware<String> {

    private final UserRepositoryForLoggedBean userRepositoryForLoggedBean;
    private final Logger logger = LoggerFactory.getLogger(Logged.class);
    private User user;

    public Logged(UserRepositoryForLoggedBean userRepositoryForLoggedBean) {
        this.userRepositoryForLoggedBean = userRepositoryForLoggedBean;
    }

    public User getUser() {
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            refreshUserInDb();
            if (user == null)
                return null;
            logger.debug("before", user);
            User userFresh = userRepositoryForLoggedBean.getById(user.getId());
            logger.debug("after", user);
            return userFresh;
        }
        return null;
    }

    @Transactional
    private void refreshUserInDb() {
        user = userRepositoryForLoggedBean.getByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null || user.getLastLogged() == null || user.getLastLogged().plus(3, SECONDS).isBefore(ZonedDateTime.now()))
            return;
        user.setLastLogged(ZonedDateTime.now());
        user.setPassword("transparent value");
        logger.debug("before", user);
        user = userRepositoryForLoggedBean.save(user);
        logger.debug("after", user);
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        String r = null;
        if (user != null) r = user.getLogin();
        return Optional.ofNullable(r);
    }
}
