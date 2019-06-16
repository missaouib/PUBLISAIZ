package publisaiz.datasources.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.datasources.database.entities.Role;
import publisaiz.datasources.database.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Set<Role> findByUsers(User user);

    Page<Role> findAll(Pageable pageable);

    List<Role> findAll();

    Role findByName(String name);

    Role findById(int id);

    Optional<Role> findByName(Optional<Role> name);
}
