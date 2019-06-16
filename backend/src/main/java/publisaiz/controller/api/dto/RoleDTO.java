package publisaiz.controller.api.dto;

import org.springframework.data.domain.Page;
import publisaiz.datasources.database.entities.Controller;
import publisaiz.datasources.database.entities.Role;
import publisaiz.datasources.database.entities.User;
import publisaiz.services.RoleService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleDTO {
    private Integer id;
    private String name;
    private List<UserDTO> users;
    private List<ControllerDTO> controllers;
    private String created;
    private String edited;
    private String createdBy;
    private String editedBy;
    private String addUser;
    private String delUser;
    private ControllerDTO addController;
    private ControllerDTO deleteController;

    public RoleDTO() {
    }

    public RoleDTO(Role role) {
        this(
                role.getId(),
                role.getName(),
                getLogins(role),
                getControllers(role),
                role.getCreated(),
                role.getEdited(),
                role.getCreatedBy(),
                role.getEditedBy()
        );
    }

    public RoleDTO(Integer id, String name, List<UserDTO> users, List<ControllerDTO> controllers, LocalDateTime created, LocalDateTime edited, String createdBy, String editedBy) {
        this.id = id;
        this.name = name.trim();
        this.users = users;
        this.controllers = controllers;
        if (created != null)
            this.created = created.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (edited != null)
            this.edited = edited.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.createdBy = createdBy;
        this.editedBy = editedBy;
    }

    public static Page<RoleDTO> convert(Page<Role> all) {
        return all.map(RoleDTO::new);
    }

    private static List<ControllerDTO> getControllers(Role role) {
        return role.getControllers().stream().map(ControllerDTO::new).collect(Collectors.toList());
    }

    private static List<UserDTO> getLogins(Role role) {
        return role.getUsers().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public List<ControllerDTO> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerDTO> controllers) {
        this.controllers = controllers;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public String getDelUser() {
        return delUser;
    }

    public void setDelUser(String delUser) {
        this.delUser = delUser;
    }

    public ControllerDTO getAddController() {
        return addController;
    }

    public void setAddController(ControllerDTO addController) {
        this.addController = addController;
    }

    public ControllerDTO getDeleteController() {
        return deleteController;
    }

    public void setDeleteController(ControllerDTO deleteController) {
        this.deleteController = deleteController;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", controllers=" + controllers +
                ", created=" + created +
                ", edited=" + edited +
                ", createdBy=" + createdBy +
                ", editedBy=" + editedBy +
                ", addUser='" + addUser + '\'' +
                ", delUser='" + delUser + '\'' +
                ", addController=" + addController +
                ", deleteController=" + deleteController +
                '}';
    }

    @Transactional
    public Role toEntity(RoleService roleService) {
        Role role = roleService.findByName(getName().trim());
        if (role == null)
            role = new Role();
        if (name != null) role.setName(name.trim());
        Optional<Controller> controller = roleService.findControllerById(getId());
        if (controller.isPresent()) role.addController(controller.get());
        Optional<User> user = roleService.getUserByLogin(getAddUser());
        if (user.isPresent()) role.addUser(user.get());
        return role;
    }
}
