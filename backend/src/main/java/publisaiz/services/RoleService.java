package publisaiz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import publisaiz.controller.api.dto.RoleDTO;
import publisaiz.datasources.database.entities.Controller;
import publisaiz.datasources.database.entities.Role;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.ControllerRepository;
import publisaiz.datasources.database.repository.RoleRepository;
import publisaiz.datasources.database.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository repository;
    private final ControllerRepository controllerRepository;
    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService(RoleRepository repository, ControllerRepository controllerService, UserRepository userRepository) {
        this.repository = repository;
        this.controllerRepository = controllerService;
        this.userRepository = userRepository;
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

    public Role findById(int id) {
        return repository.findById(id);
    }

    public Role save(Role role) {
        return repository.save(role);
    }

    public void delete(Role role) {
        repository.delete(role);
    }


    public RoleDTO removeUser(Integer roleId, Integer userId) {
        Role role = findById(roleId);
        if (userId != null && roleId != null) {
            Optional<User> userOptional = userRepository.findById(userId);
            logger.debug("found user [{}] and role [{}]", userOptional.isPresent(), role);
            if (role != null && userOptional.isPresent()) {
                User user = userOptional.get();
                logger.debug("deleting relation of user [{}] and role [{}]", user, role);
                user.getRoles().remove(role);
                user = userRepository.save(user);
                role.getUsers().remove(user);
                return new RoleDTO(save(role));
            }
        }
        return new RoleDTO(role);
    }

    public RoleDTO removeController(Integer roleId, Integer controllerId) {
        if (roleId != null && controllerId != null) {
            Role role = findById(roleId);
            Optional<Controller> optController = controllerRepository.findById(controllerId);
            if (role != null && optController.isPresent()) {
                Controller controller = optController.get();
                role.getControllers().remove(controller);
                controller.getRoles().remove(role);
                controllerRepository.save(controller);
                return new RoleDTO(save(role));
            }
        }
        return null;
    }


    private void deleteRoleIfPossible(Optional<Role> roleToDelete) {
        if (roleToDelete.isPresent()) {
            delete(roleToDelete.get());
        }
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
                role.addController(controllerRepository.findById(addActionToRole.get().getId()));
                save(role);
            }
        }
    }

    private Optional<Role> findByName(Optional<Role> editRoleName) {
        return repository.findByName(editRoleName);
    }

    public RoleDTO save(RoleDTO role) {
        return new RoleDTO(repository.save(role.toEntity(this)));
    }

    public Optional<Controller> findControllerById(Integer id) {
        if (id == null) return Optional.empty();
        return controllerRepository.findById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        if (login == null || login.length() == 0) return Optional.empty();
        return Optional.ofNullable(userRepository.getByLogin(login));
    }
}
