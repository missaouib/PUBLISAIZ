package publisaiz.functionalities.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import publisaiz.api.dto.ControllerDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.Controller;
import publisaiz.entities.Permission;
import publisaiz.entities.Role;
import publisaiz.entities.User;
import publisaiz.functionalities.roles.RoleRepository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
class ControllerService {

    private final ControllerRepository repository;
    private final RoleRepository roleRepository;
    private final UserRepositoryForControllers userRepositoryForControllers;
    private final Logged logged;
    private final PermissionRepositoryForControllers permissionRepositoryForControllers;
    private final Logger logger = LoggerFactory.getLogger(ControllerService.class);

    public ControllerService(ControllerRepository repository,
                             RoleRepository roleRepository,
                             UserRepositoryForControllers userRepositoryForControllers,
                             Logged logged,
                             PermissionRepositoryForControllers permissionRepositoryForControllers) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.userRepositoryForControllers = userRepositoryForControllers;
        this.logged = logged;
        this.permissionRepositoryForControllers = permissionRepositoryForControllers;
    }

    public Page<ControllerDTO> findAll(Pageable pageable) {
        Set<Role> roles = Set.copyOf(roleRepository.findAll());
        Set<User> users = new HashSet<>(userRepositoryForControllers.findAll());
        return ControllerDTO.convert(repository.findAll(pageable), roles, users);
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

    public ResponseEntity<ControllerDTO> save(ControllerDTO dto) {
        logger.info("save [{}]", dto);
        Controller c = ControllerDto2Controller.controllerDTOtoEntity(dto, this);
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
                permissionRepositoryForControllers.save(p);
            }
        }
    }

    public void addPermission(ControllerDTO dto, Controller controller) {
        logger.debug("adding permission as active for [{]]", dto.getDeletePermission());
        User user = userRepositoryForControllers.getByLogin(dto.getAddPermission());
        if (user != null && dto.getAddPermission() != null && dto.getAddPermission().trim().length() > 0) {
            Permission permission = new Permission();
            permission.setPermissionFor(user);
            permission.addController(controller);
            permission.setCreatedBy(logged.getUser());
            permission.setActive(true);
            permission.setFromDate(ZonedDateTime.now());
            permissionRepositoryForControllers.save(permission);
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
