package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@ApiIgnore
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "role_controller",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "controller_id")}
    )
    private Set<Controller> controllers = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    private boolean active;

    @CreatedDate
    private LocalDateTime created;
    @LastModifiedDate
    private LocalDateTime edited;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String editedBy;
    @Version
    private
    long version;

    public Role() {
        setActive(true);
    }

    public Role(String string) {
        name = string;
    }

    public Role(int id, String name, Set<Controller> controllers, Set<User> users, boolean active) {
        this.id = id;
        this.name = name;
        this.controllers = controllers;
        this.users = users;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Controller> getControllers() {
        return controllers;
    }

    public void setControllers(Set<Controller> controllers) {
        this.controllers = controllers;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }

        Role role = (Role) o;

        return name.equals(role.name);
    }

    public Role id(int id) {
        this.id = id;
        return this;
    }

    public Role name(String name) {
        this.name = name;
        return this;
    }

    public Role controllers(Set<Controller> controllers) {
        this.controllers = controllers;
        return this;
    }

    public Role users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Role active(boolean active) {
        this.active = active;
        return this;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", controllers='" + getControllers() + "'" +
                ", users='" + getUsers() + "'" +
                ", active='" + isActive() + "'" +
                "}";
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    private boolean isActive() {
        return active;
    }

    private void setActive(boolean active) {
        this.active = active;
    }

    public void addController(Controller controller) {
        if (this.controllers == null) {
            this.controllers = new HashSet<>();
        }
        this.controllers.add(controller);
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getEdited() {
        return edited;
    }

    public void setEdited(LocalDateTime edited) {
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

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeController(Controller controller) {
        controllers.remove(controller);
    }
}
