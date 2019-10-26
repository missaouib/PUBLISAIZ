package publisaiz.functionalities.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publisaiz.entities.User;
import publisaiz.entities.UserProfile;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Page<UserProfile> findByUserAndEnabledTrueOrderByIdAsc(User user, Pageable pageable);

    Set<UserProfile> findByUserAndEnabledTrueOrderByIdAsc(User user);

}
