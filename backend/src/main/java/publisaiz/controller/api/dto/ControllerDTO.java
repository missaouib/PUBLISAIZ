package publisaiz.controller.api.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import publisaiz.datasources.database.entities.Controller;
import publisaiz.datasources.database.entities.Role;
import publisaiz.datasources.database.entities.User;
import publisaiz.services.ControllerService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerDTO {

    private static Logger logger = LoggerFactory.getLogger(ControllerDTO.class);
    private Integer id;
    @NonNull
    private String controller;
    private String method;
    private String httpMethod;
    private Set<String> roles;
    private Set<String> permissions;
    @NonNull
    private Boolean active;
    private Set<String> addroles;
    private Set<String> addpermissions;
    private String addRole;
    private String deleteRole;
    private String addPermission;
    private String deletePermission;

    public ControllerDTO() {
    }

    public ControllerDTO(Controller controller, Set<Role> rolesToAdd, Set<User> usersToAddPermissions) {
        this(controller);
        this.addpermissions = usersToAddPermissions.stream()
                .map(c -> c.getLogin()).filter(l -> !permissions.contains(l)).collect(Collectors.toSet());
        this.addroles = rolesToAdd.stream()
                .map(r -> r.getName()).filter(r -> !roles.contains(r)).collect(Collectors.toSet());
        logger.debug(toString());
    }

    public ControllerDTO(Controller controller) {
        this.id = controller.getId();
        this.active = controller.isActive();
        this.httpMethod = controller.getHttpMethod();
        this.controller = controller.getController();
        this.method = controller.getMethod();
        this.permissions = controller.getPermissions().stream()
                .filter(p -> p.isActive())
                .map(p -> {
                    if (p.getPermissionFor() != null)
                        return p.getPermissionFor().getLogin();
                    return null;
                }).filter(x -> x != null).collect(Collectors.toSet());
        this.roles = controller.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
        logger.debug(toString());
    }

    public static List<ControllerDTO> convert(Page<Controller> controllers, final Set<Role> rolesToAdd, final Set<User> usersToAddPermissions) {
        List<ControllerDTO> collect = controllers.stream()
                .map(c -> new ControllerDTO(c, rolesToAdd, usersToAddPermissions))
                .peek(c -> logger.debug("controller: ", c))
                .collect(Collectors.toList());
        return collect;
    }

    public static Controller toEntity(ControllerDTO dto, ControllerService controllerService) {
        final Controller controller = controllerService.findById(dto.id);
        if (controller == null)
            throw new IllegalArgumentException("no such controller");
        logger.debug("addPermission");
        controllerService.addPermission(dto, controller);
        logger.debug("delPermission");
        controllerService.delPermission(dto, controller);
        logger.debug("addRole");
        controllerService.addRole(dto, controller);
        logger.debug("deleteRole");
        controllerService.deleteRole(dto, controller);
        return controller;
    }

    public Boolean getActive() {
        return active;
    }

    public Set<String> getAddroles() {
        return addroles;
    }

    public void setAddroles(Set<String> addroles) {
        this.addroles = addroles;
    }

    public String getAddRole() {
        return addRole;
    }

    public void setAddRole(String addRole) {
        this.addRole = addRole;
    }

    public String getDeleteRole() {
        return deleteRole;
    }

    public void setDeleteRole(String deleteRole) {
        this.deleteRole = deleteRole;
    }

    public String getAddPermission() {
        return addPermission;
    }

    public void setAddPermission(String addPermission) {
        this.addPermission = addPermission;
    }

    public Set<String> getAddpermissions() {
        return addpermissions;
    }

    public void setAddpermissions(Set<String> addpermissions) {
        this.addpermissions = addpermissions;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Controller)) return false;
        Controller controller = (Controller) o;
        return Objects.equals(getController(), controller.getController()) &&
                Objects.equals(getMethod(), controller.getMethod()) &&
                Objects.equals(getHttpMethod(), controller.getHttpMethod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getController(), getMethod(), getHttpMethod());
    }

    @Override
    public String toString() {
        return controller + method + " : " + httpMethod;
    }

    public String getDeletePermission() {
        return deletePermission;
    }

    public void setDeletePermission(String deletePermission) {
        this.deletePermission = deletePermission;
    }

}
