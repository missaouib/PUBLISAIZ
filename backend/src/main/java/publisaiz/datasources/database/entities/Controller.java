package publisaiz.datasources.database.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"controller", "method", "httpMethod"}))
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Controller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String controller;
    private String method;
    private String httpMethod;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "controllers")
    private Set<Role> roles;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Permission> permissions;
    @NonNull
    private boolean active;

    public Controller() {
    }

    public Controller(Controller controller) {
        id = controller.id;
        active = controller.isActive();
        httpMethod = controller.getHttpMethod();
        this.controller = controller.getController();
        this.method = controller.getMethod();
        permissions = controller.getPermissions();
        roles = controller.getRoles();
    }

    public int getId() {
        return id;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean isActive() {
        return active;
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

    public Controller copy() {
        return new Controller(this);
    }

    public void addRole(Role role) {
        roles.add(role);
    }
}
