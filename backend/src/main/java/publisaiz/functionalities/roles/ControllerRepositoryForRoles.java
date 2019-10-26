package publisaiz.functionalities.roles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import publisaiz.entities.Controller;
import publisaiz.entities.Permission;
import publisaiz.entities.Role;

import java.util.Set;

@Repository
@Transactional
interface ControllerRepositoryForRoles extends PagingAndSortingRepository<Controller, Integer> {

    Set<Controller> findAll();

    Page<Controller> findAll(Pageable pageable);

    Set<Controller> findByRoles(Role role);

    Set<Controller> findByPermissions(Permission permission);

    Controller findByController(String controller);

    Controller findById(int id);

    @Query("select c from Controller c order by c.controller asc")
    Page<Controller> findDistinctOrderByControllerAsc(Pageable pageable);
}
