package publisaiz.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.datasources.database.entities.*;
import publisaiz.services.ControllerService;
import publisaiz.services.PermissionService;
import publisaiz.services.RoleService;
import publisaiz.services.UserService;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Transactional
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    public static final String YOU_ARE_ADMIN_NOW = "you are admin now!";
    public static final String YOU_ARE_USER_NOW = "you are user now!";
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationProviderImpl.class);
    private static final String COULD_NOT_CREATE = "COULD NOT CREATE USER";
    private static final String CONFIRM_EMAIL = "PLEASE CONFIRM YOUR EMAIL!";
    private static final String WRONG = "WRONG PASSWORD";
    public static final String DISABLED = "Your user is manually disabled";
    private final UserService userService;
    private final PermissionService permissionService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ControllerService controllerService;

    public AuthenticationProviderImpl(ControllerService controllerService, UserService userService,
                                      PermissionService permissionService, BCryptPasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.controllerService = controllerService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        User user = null;
        try{
            user = getUser(authentication);
        } catch (Exception e) {
           logger.error("could not create user: [{}] due [{}]", authentication, e.getMessage());
        }
        authenticateUser(authentication.getCredentials().toString(), user);
        Set<Authority> authorities = new HashSet<>();
        setRoles(user, authorities);
        setPermissions(user, authorities);
        authorities.forEach(authority -> logger.info("authority [{}] ", authority.getAuthority()));
        return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials().toString(), authorities);
    }

    private User getUser(Authentication authentication) {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.getUserByLogin(login);
        if (user == null && login.length()> 1 && password.length()> 1) {
            logger.debug("login [{}] password [{}]", login, password);
            user = new User();
            user.setLogin(login);
            user.setPassword(password);
            if (userService.getAllUsers().isEmpty())
                return saveAsAdmin(user);
            else
                return saveAsUser(user);
        }
        return user;
    }

    private User saveAsUser(User user) {
        Role role = new Role();
        role.setName("user");
        user.addRole(role);
        user.setActivated(ZonedDateTime.now());
        user.setCreated(ZonedDateTime.now());
        user.setActive(true);
        user.setActivated(ZonedDateTime.now());
        Warning warning = new Warning(YOU_ARE_USER_NOW);
        user.addWarning(warning);
        user = userService.saveWithPasswordEncoding(user);
        role.setControllers(controllerService.findByRoles(role));
        return userService.saveWithPasswordEncoding(user);
    }

    private User saveAsAdmin(User user) {
        Role role = new Role();
        role.setName("admin");
        role.setControllers(controllerService.findAll());
        user.addRole(role);
        user.setActive(true);
        user.setActivated(ZonedDateTime.now());
        Warning warning = new Warning(YOU_ARE_ADMIN_NOW);
        user.addWarning(warning);
        return userService.saveWithPasswordEncoding(user);
    }

    private void authenticateUser(String password, User user) {
        if (user == null) {
            logger.info(COULD_NOT_CREATE);
            throw new BadCredentialsException(COULD_NOT_CREATE);
        } else if (!user.isEnabled()) {
            logger.info(DISABLED);
            throw new BadCredentialsException(DISABLED);
        } else if (user.getActivated() == null || user.getActivated().isAfter(ZonedDateTime.now())) {
            logger.info(CONFIRM_EMAIL);
            throw new BadCredentialsException(CONFIRM_EMAIL);
        } else if (!passwordEncoder.matches(password, user.getPasswordEncrypted())) {
            logger.info(WRONG);
            throw new BadCredentialsException(WRONG);
        }
    }

    private void setPermissions(User user, Set<Authority> authorities) {
        logger.info("finding permissions by user [{}]", user);
            permissionService
                .findByUser(user).stream()
                .filter(p -> p.isActive())
                .forEach(addPermissionsControllersToAuthorities(authorities));
    }

    private Consumer<Permission> addPermissionsControllersToAuthorities(Set<Authority> authorities) {
        logger.info("addPermissionsControllersToAuthorities [{}]", authorities);
        return permission ->
                permission.getControllers().forEach(addControllersToAuthorities(authorities));
    }

    private Consumer<Controller> addControllersToAuthorities(Set<Authority> authorities) {
        logger.info("addControllersToAuthorities [{}]", authorities);
        return action -> {
            logger.info("addControllersToAuthorities action:[{}]", action);
            @NotNull String stringified = SecurityConfig.stringifyController(action.getController(), action.getMethod(), action.getHttpMethod());
            authorities.add(new Authority(stringified));
            logger.info("added authority: [{}]", stringified);
        };
    }

    private void setRoles(User user, Set<Authority> authorities) {
        logger.info("setRoles [{}]", user);
        roleService.findByUser(user).forEach(addRoleControllersToAuthorities(authorities));
    }

    private Consumer<Role> addRoleControllersToAuthorities(Set<Authority> authorities) {
        logger.info("addRoleControllersToAuthorities [{}]", authorities);
        return role -> role.getControllers().forEach(addControllersToAuthorities(authorities));
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Transactional
    public static class Authority implements GrantedAuthority {
        public static final long serialVersionUID = 1L;
        private String a;

        Authority(String a) {
            this.a = a;
        }

        @Override
        public String getAuthority() {
            return a;
        }
    }
}