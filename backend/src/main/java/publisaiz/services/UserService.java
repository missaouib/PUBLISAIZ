package publisaiz.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import publisaiz.config.Logged;
import publisaiz.controller.api.dto.UserDTO;
import publisaiz.datasources.database.entities.Role;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ControllerService actionController;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final Logged logged;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       ControllerService actionController, RoleService roleService,
                       PermissionService permissionService, Logged logged) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.actionController = actionController;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.logged = logged;
    }

    public Collection<User> getAllUsers() {
        Collection<User> u = new ArrayList<>();
        userRepository.findAll().forEach(u::add);
        return u;
    }

    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable).stream().map(u -> new UserDTO(u)).collect(Collectors.toSet()));
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(new User());
    }

    public Collection<User> getUserByName(String name) {
        return userRepository.getByName(name);
    }

    public User getUserByLogin(String login) {
        User user = null;
        try {
            user = userRepository.getByLogin(login);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return user;
    }

    public void update(User u) {
        userRepository.save(u);
    }

    public void delete(User u) {
        userRepository.delete(u);
    }

    public User saveWithPasswordEncoding(User u) {
        if (u.getPassword() != null && u.getPassword().length() > 0) {
            u.setPasswordEncrypted(passwordEncoder.encode(u.getPassword()));
        }
        return userRepository.save(u);
    }

    public User save(User u) {
        return userRepository.save(u);
    }

    public Set<Role> setRoles(Set<String> roleNames, User user, Logged logged) {
        return roleNames.stream().map(name -> roleService.findByName(name)).collect(Collectors.toSet());
    }

    @Transactional
    public ResponseEntity<?> whoami(Logged logged) {
        if(logged.getUser()==null || logged.getUser().getLogin()==null)
            return ResponseEntity.notFound().build();
        UserDTO dto = new UserDTO(logged.getUser());
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity update(UserDTO userDTO) {
        return ResponseEntity.ok(new UserDTO(userDTO.toEntity(this, logged)));
    }
}
