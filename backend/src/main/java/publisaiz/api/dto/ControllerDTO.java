package publisaiz.api.dto;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import publisaiz.entities.Controller;
import publisaiz.entities.Role;
import publisaiz.entities.User;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerDTO {

    private static final Logger logger = LoggerFactory.getLogger(ControllerDTO.class);
    private Integer id;
    private String controller;
    private String method;
    private String httpMethod;
    private Boolean active;
    private String addRole;
    private String deleteRole;
    private String addPermission;
    private String deletePermission;
    private Set<String> roles;
    private Set<String> permissions;
    private Set<String> addroles;
    private Set<String> addpermissions;

    private ControllerDTO() {
    }

    private ControllerDTO(Controller controller, Set<Role> rolesToAdd, Set<User> usersToAddPermissions) {
        this(controller);
        this.addpermissions = usersToAddPermissions.stream()
                .map(c -> c.getLogin()).filter(l -> !permissions.contains(l)).collect(Collectors.toSet());
        this.addroles = rolesToAdd.stream()
                .map(r -> r.getName()).filter(r -> !roles.contains(r)).collect(Collectors.toSet());
        logger.debug("ControllerDTO: rolesToAdd and usersToAddPermissions constructor [{}]", this);
        logger.debug(toString());
    }

    public ControllerDTO(Controller controller) {
        this.id = controller.getId();
        this.active = controller.isActive();
        this.httpMethod = controller.getHttpMethod();
        this.controller = controller.getController();
        this.method = controller.getMethod();
        this.permissions = getPermissions(controller);
        this.roles = getRoles(controller);
        logger.debug("ControllerDTO default constructor [{}]", this);
    }

    public static Page<ControllerDTO> convert(Page<Controller> controllers, final Set<Role> rolesToAdd, final Set<User> usersToAddPermissions) {
        var collect = controllers.map(c -> new ControllerDTO(c, rolesToAdd, usersToAddPermissions));
        return collect;
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

    @NotNull
    private Set<String> getPermissions(Controller controller) {
        return controller.getPermissions().stream()
                .filter(p -> p.isActive())
                .map(p -> {
                    if (p.getPermissionFor() != null)
                        return p.getPermissionFor().getLogin();
                    return null;
                }).filter(x -> x != null).collect(Collectors.toSet());
    }

    @NotNull
    private Set<String> getRoles(Controller controller) {
        return controller.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
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
