package publisaiz.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import publisaiz.config.Logged;
import publisaiz.controller.api.dto.PermissionDTO;
import publisaiz.datasources.database.entities.Permission;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.PermissionRepository;
import publisaiz.datasources.database.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final Logged logged;
    private final UserRepository userRepository;

    public PermissionService(PermissionRepository permissionRepository, Logged logged, UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.logged = logged;
        this.userRepository = userRepository;
    }

    public void saveAll(Set<Permission> permissions) {
        permissionRepository.saveAll(permissions);
    }

    public void save(Permission permission) {
        permissionRepository.save(permission);
    }

    public Set<Permission> findByUser(User user) {
        return permissionRepository.findByPermissionFor(user);
    }

    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    public User getLoggedUser() {
        return logged.getUser();
    }

    public User findUser(String permissionFor) {
        return userRepository.getByLogin(permissionFor);
    }

    public ResponseEntity findAll(Pageable pageable) {
        Page<Permission> all = permissionRepository.findAll(pageable);
        if (all != null)
            return ResponseEntity.ok(PermissionDTO.convert(all));
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity save(PermissionDTO permissionDTO) {
        PermissionDTO re = new PermissionDTO(permissionDTO.toEntity(this));
        if (re != null)
            return ResponseEntity.ok(re);
        return ResponseEntity.badRequest().build();
    }
}
