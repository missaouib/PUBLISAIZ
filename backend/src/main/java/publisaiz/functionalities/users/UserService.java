package publisaiz.functionalities.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import publisaiz.api.dto.UserDTO;
import publisaiz.api.dto.UserProfileDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.Role;
import publisaiz.entities.Uploaded;
import publisaiz.entities.User;
import publisaiz.entities.UserProfile;
import publisaiz.functionalities.roles.RoleService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final Logged logged;
    private final UserProfileRepository userProfileRepository;
    private final Environment environment;
    private final UploadedRepositoryForUsers uploadedRepositoryForUsers;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       RoleService roleService,
                       Logged logged,
                       UploadedRepositoryForUsers uploadedRepositoryForUsers,
                       Environment environment, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.logged = logged;
        this.uploadedRepositoryForUsers = uploadedRepositoryForUsers;
        this.environment = environment;
        this.userProfileRepository = userProfileRepository;
    }

    public Collection<User> getAllUsers() {
        Collection<User> u = new ArrayList<>();
        userRepository.findAll().forEach(u::add);
        return u;
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(u -> new UserDTO(u));
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(new User());
    }

    public Page<User> getUserByName(String name, Pageable pageable) {
        return userRepository.getByName(name, pageable);
    }

    public User getUserByLogin(String login) {
        User user = null;
        try {
            user = userRepository.getByLogin(login);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return user;
    }

    public void update(User u) {
        userRepository.save(u);
    }

    public void delete(User u) {
        userRepository.delete(u);
    }

    public User saveWithPasswordEncoding(User u) {
        if (u.getPassword() != null && u.getPassword().length() > 0) {
            u.setPasswordEncrypted(passwordEncoder.encode(u.getPassword()));
        }
        return userRepository.save(u);
    }

    public User save(User u) {
        return userRepository.save(u);
    }

    public Set<Role> setRoles(Set<String> roleNames, User user, Logged logged) {
        return roleNames.stream()
                .map(name -> roleService.findByName(name))
                .collect(Collectors.toSet());
    }

    public ResponseEntity<?> whoami() {
        if (logged == null || logged.getUser() == null)
            return ResponseEntity.notFound().build();
        UserDTO dto = new UserDTO(logged.getUser());
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity update(UserDTO userDTO) {
        return ResponseEntity
                .ok(new UserDTO(Userdto2User.userDTOtoEntity(userDTO, this, logged)));
    }

    public ResponseEntity<?> getAuthors(Pageable pageable) {
        return ResponseEntity
                .ok(userRepository.getAuthors(pageable).map(UserDTO::new));
    }

    public Page<UserProfileDTO> getMyProfile(Pageable pageable) {
        Page<UserProfileDTO> res = userProfileRepository.findByUserAndEnabledTrueOrderByIdAsc(logged.getUser(), pageable)
                .map(p -> new UserProfileDTO(p, environment));
        return res;
    }

    public ResponseEntity<UserProfileDTO> setMyProfile(UserProfileDTO profileDTO) {
        User user = logged.getUser();
        Set<UserProfile> profiles = userProfileRepository.findByUserAndEnabledTrueOrderByIdAsc(user);
        profiles.forEach(p -> p.setEnabled(false));
        UserProfile profile = Userprofiedto2Userprofile.userProfileDTO2UserProfile(profileDTO, this);
        profile.setUser(user);
        profile.setEnabled(true);
        profiles.add(profile);
        user.setProfiles(profiles);
        user.setPassword("irrelevant");
        userRepository.save(user);
        return ResponseEntity.ok(new UserProfileDTO(profile, environment));
    }


    public Boolean deleteMyProfile(Long id) {
        Optional<UserProfile> prof = userProfileRepository.findById(id);
        prof.ifPresent(p -> setEnabledToFalse(p));
        return true;
    }

    private void setEnabledToFalse(UserProfile p) {
        if (p.getUser().getLogin().equals(logged.getUser().getLogin()))
            p.setEnabled(false);
        userProfileRepository.save(p);
    }

    public Optional<UserProfile> findProfileById(Long id) {
        return userProfileRepository.findById(id);
    }

    public Optional<Uploaded> findUploadedByFileURL(String fileURL) {
        String idString = fileURL.substring(fileURL.lastIndexOf("/") + 1);
        if (idString.isEmpty()) return Optional.empty();
        return uploadedRepositoryForUsers.findById(Long.parseLong(idString));
    }

}
