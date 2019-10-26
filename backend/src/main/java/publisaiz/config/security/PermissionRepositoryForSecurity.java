package publisaiz.config.security;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.entities.Permission;
import publisaiz.entities.User;

import java.util.Set;

@Repository()
@Transactional
interface PermissionRepositoryForSecurity extends PagingAndSortingRepository<Permission, Long> {

    Set<Permission> findByPermissionFor(User u);

    Set<Permission> findAll();

    Page<Permission> findAll(Pageable pageable);
}
