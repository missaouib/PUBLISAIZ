package publisaiz.functionalities.permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import publisaiz.api.dto.PermissionDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.Permission;
import publisaiz.entities.User;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
class PermissionService {

    private final PermissionRepository permissionRepository;
    private final Logged logged;
    private final UserRepositoryForPermissions userRepositoryForPermissions;

    public PermissionService(PermissionRepository permissionRepository, Logged logged, UserRepositoryForPermissions userRepositoryForPermissions) {
        this.permissionRepository = permissionRepository;
        this.logged = logged;
        this.userRepositoryForPermissions = userRepositoryForPermissions;
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
        return userRepositoryForPermissions.getByLogin(permissionFor);
    }

    public Page<PermissionDTO> findAll(Pageable pageable) {
        Page<Permission> all = permissionRepository.findAll(pageable);
        return PermissionDTO.convert(all);
    }

    public ResponseEntity<PermissionDTO> save(PermissionDTO permissionDTO) {
        Permission permission = Permissiondto2Permission.permissionDTOtoEntity(permissionDTO, this);
        if (permission != null) {
            PermissionDTO re = new PermissionDTO(permission);
            return ResponseEntity.ok(re);
        }
        return ResponseEntity.badRequest().build();
    }
}
