package publisaiz.functionalities.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import publisaiz.api.dto.UserDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.User;

class Userdto2User {
    private static final Logger logger = LoggerFactory.getLogger(Userdto2User.class);
    private final Logged logged;

    public Userdto2User(Logged logged) {
        this.logged = logged;
    }

    static public User userDTOtoEntity(UserDTO userDTO, UserService userService, Logged logged) {
        logger.info(" toEntity UserDTO [{}]", userDTO);
        User user = null;
        if (userDTO.getLogin() != null)
            user = userService.getUserByLogin(userDTO.getLogin());
        if (user == null)
            user = new User(userDTO.getLogin());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setRoles(userService.setRoles(userDTO.getRoles(), user, logged));
        user.setActive(userDTO.getActive());
        user.setPassword(userDTO.getPassword());

        if (user.getId() == null) {
            user = userService.saveWithPasswordEncoding(user);
        } else {
            user = userService.save(user);
        }

        return user;
    }
}