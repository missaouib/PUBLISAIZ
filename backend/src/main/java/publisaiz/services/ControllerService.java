package publisaiz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import publisaiz.config.Logged;
import publisaiz.controller.api.dto.ControllerDTO;
import publisaiz.datasources.database.entities.Controller;
import publisaiz.datasources.database.entities.Permission;
import publisaiz.datasources.database.entities.Role;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.ControllerRepository;
import publisaiz.datasources.database.repository.PermissionRepository;
import publisaiz.datasources.database.repository.RoleRepository;
import publisaiz.datasources.database.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ControllerService {

    private final ControllerRepository repository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final Logged logged;
    private final PermissionRepository permissionRepository;
    Logger logger = LoggerFactory.getLogger(ControllerService.class);

    public ControllerService(ControllerRepository repository,
                             RoleRepository roleRepository,
                             UserRepository userRepository,
                             Logged logged,
                             PermissionRepository permissionRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.logged = logged;
        this.permissionRepository = permissionRepository;
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        Set<Role> roles = Set.copyOf(roleRepository.findAll());
        Set<User> users = userRepository.findAll();
        return ResponseEntity.ok(ControllerDTO.convert(repository.findAll(pageable), roles, users));
    }

    public Set<Controller> findAll() {
        return new HashSet<>(repository.findAll());
    }

    public Set<Controller> findByRoles(Role role) {
        return repository.findByRoles(role);
    }

    public Set<Controller> findByPermissions(Permission permission) {
        return repository.findByPermissions(permission);
    }

    public void update(Controller controller) {
        repository.save(controller);
    }

    public Controller findByController(String controller) {
        return repository.findByController(controller);
    }

    public void saveAll(Set<Controller> controllers) {
        repository.saveAll(controllers);
    }

    public void add(Controller controller) {
        repository.save(controller);
    }

    public Controller findById(int id) {
        return repository.findById(id);
    }

    public void save(Controller controller) {
        repository.save(controller);
    }

    public ResponseEntity<?> save(ControllerDTO dto) {
        logger.info("save [{}]", dto);
        Controller c = ControllerDTO.toEntity(dto, this);
        return ResponseEntity.ok(new ControllerDTO(c));
    }

    public void delPermission(ControllerDTO dto, Controller controller) {
        logger.info("setting permission as inactive for [{]]", dto.getDeletePermission());
        if (dto.getDeletePermission() != null && dto.getDeletePermission().trim().length() > 0) {
            Set<Permission> permissions = controller.getPermissions();
            Optional<Permission> permission = permissions.stream()
                    .filter(p -> p.getPermissionFor().getLogin().equals(dto.getDeletePermission()))
                    .findFirst();
            logger.info("found [{]] from permissions [{}]", permission, permissions);
            if (permission.isPresent()) {
                Permission p = permission.get();
                p.setActive(false);
                logger.info("setting permission [{]] as inactive", p);
                permissionRepository.save(p);
            }
        }
    }

    public void addPermission(ControllerDTO dto, Controller controller) {
        logger.debug("adding permission as active for [{]]", dto.getDeletePermission());
        User user = userRepository.getByLogin(dto.getAddPermission());
        if (user != null && dto.getAddPermission() != null && dto.getAddPermission().trim().length() > 0) {
            Permission permission = new Permission();
            permission.setPermissionFor(user);
            permission.addController(controller);
            permission.setCreatedBy(logged.getUser());
            permission.setActive(true);
            permission.setFromDate(ZonedDateTime.now());
            permissionRepository.save(permission);
            Set<Permission> permissions = controller.getPermissions();
            permissions.add(permission);
        }
    }

    public void addRole(ControllerDTO dto, Controller controller) {
        if (dto.getAddRole() != null && dto.getAddRole().trim().length() > 0) {
            logger.debug("..................... dto: [{}]", dto);
            Role role = roleRepository.findByName(dto.getAddRole());
            if (role == null)
                role = new Role(dto.getAddRole());
            controller.addRole(role);
            controller = repository.save(controller);
            role.addController(controller);
            roleRepository.save(role);
        }
    }

    public void deleteRole(ControllerDTO dto, Controller controller) {
        if (dto.getDeleteRole() != null && dto.getDeleteRole().trim().length() > 0) {
            Role role = roleRepository.findByName(dto.getDeleteRole());
            if (role == null)
                return;
            Set<Role> roles = controller.getRoles();
            roles.remove(role);
            controller.setRoles(roles);
            repository.save(controller);
            role.removeController(controller);
            roleRepository.save(role);
        }
    }
}
