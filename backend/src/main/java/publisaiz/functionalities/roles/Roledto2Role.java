package publisaiz.functionalities.roles;

import publisaiz.api.dto.RoleDTO;
import publisaiz.entities.Controller;
import publisaiz.entities.Role;
import publisaiz.entities.User;

import java.util.Optional;

class Roledto2Role {
    public Roledto2Role() {
    }

    static Role roleDTOtoEntity(RoleDTO roleDTO, RoleService roleService) {
        Role role = null;
        if (roleDTO.getName() != null && roleDTO.getName().length() > 0)
            role = roleService.findByName(roleDTO.getName().trim());
        if (role == null)
            role = new Role();
        if (roleDTO.getName() != null) role.setName(roleDTO.getName().trim());
        Optional<Controller> controller = roleService.findControllerById(roleDTO.getId());
        if (controller.isPresent()) role.addController(controller.get());
        Optional<User> user = roleService.getUserByLogin(roleDTO.getAddUser());
        if (user.isPresent()) role.addUser(user.get());
        return role;
    }
}