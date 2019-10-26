package publisaiz.functionalities.roles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import publisaiz.api.dto.RoleDTO;
import publisaiz.entities.Controller;
import publisaiz.entities.Role;
import publisaiz.entities.User;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository repository;
    private final ControllerRepositoryForRoles controllerRepositoryForRoles;
    private final UserRepositoryForRoles userRepositoryForRoles;
    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService(RoleRepository repository, ControllerRepositoryForRoles controllerService, UserRepositoryForRoles userRepositoryForRoles) {
        this.repository = repository;
        this.controllerRepositoryForRoles = controllerService;
        this.userRepositoryForRoles = userRepositoryForRoles;
    }

    public Set<Role> findByUser(User user) {
        return repository.findByUsers(user);
    }

    public Page<RoleDTO> findAll(Pageable pageable) {
        return RoleDTO.convert(repository.findAll(pageable));
    }

    public Role findByName(String name) {
        return repository.findByName(name);
    }

    private Role findById(int id) {
        return repository.findById(id);
    }

    public Role save(Role role) {
        return repository.save(role);
    }

    private void delete(Role role) {
        repository.delete(role);
    }


    public RoleDTO removeUser(Integer roleId, Integer userId) {
        Role role = findById(roleId);
        if (userId != null && roleId != null) {
            Optional<User> userOptional = userRepositoryForRoles.findById(userId);
            logger.debug("found user [{}] and role [{}]", userOptional.isPresent(), role);
            if (role != null && userOptional.isPresent()) {
                User user = userOptional.get();
                logger.debug("deleting relation of user [{}] and role [{}]", user, role);
                user.getRoles().remove(role);
                user = userRepositoryForRoles.save(user);
                role.getUsers().remove(user);
                return new RoleDTO(save(role));
            }
        }
        return new RoleDTO(role);
    }

    public RoleDTO removeController(Integer roleId, Integer controllerId) {
        if (roleId != null && controllerId != null) {
            Role role = findById(roleId);
            Optional<Controller> optController = controllerRepositoryForRoles.findById(controllerId);
            if (role != null && optController.isPresent()) {
                Controller controller = optController.get();
                role.getControllers().remove(controller);
                controller.getRoles().remove(role);
                controllerRepositoryForRoles.save(controller);
                return new RoleDTO(save(role));
            }
        }
        return null;
    }


    private void deleteRoleIfPossible(Optional<Role> roleToDelete) {
        roleToDelete.ifPresent(this::delete);
    }

    private void oldRole(String editRoleName, Optional<Role> editRole) {
        Role role = new Role();
        if (editRole.isPresent()) {
            role = findById(editRole.get().getId());
        }
        // RENAME ROLE
        if (editRoleName != null && editRoleName.length() > 2) {
            role.setName(editRoleName);
            save(role);
        }
    }

    private void newRole(String addNewRole) {
        Role role = findByName(addNewRole);
        if (role == null) {
            role.setName(addNewRole);
        }
        save(role);
    }

    private void deleteActionFromRole(Optional<Controller> delAction, String editRoleName) {
        Role role = findByName(editRoleName);
        role.setControllers(
                role.getControllers().stream()
                        .filter(a -> a.getId() != delAction.get().getId())
                        .collect(Collectors.toCollection(HashSet::new)));
        save(role);
    }

    private void addActionToRole(Optional<Controller> addActionToRole, Optional<Role> editRoleName) {
        if (editRoleName.isPresent()) {
            Role role = findByName(editRoleName.get().getName());
            if (role.getName() != null && addActionToRole.isPresent()) {
                role.addController(controllerRepositoryForRoles.findById(addActionToRole.get().getId()));
                save(role);
            }
        }
    }

    private Optional<Role> findByName(Optional<Role> editRoleName) {
        return repository.findByName(editRoleName);
    }

    public RoleDTO save(RoleDTO roleDTO) {
        return new RoleDTO(repository.save(Roledto2Role.roleDTOtoEntity(roleDTO, this)));
    }

    public Optional<Controller> findControllerById(Integer id) {
        if (id == null) return Optional.empty();
        return controllerRepositoryForRoles.findById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        if (login == null || login.length() == 0) return Optional.empty();
        return Optional.ofNullable(userRepositoryForRoles.getByLogin(login));
    }
}
