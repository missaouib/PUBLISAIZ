package publisaiz.datasources.database.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"login"}))
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class User implements UserDetails {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, max = 30)
    private String name;

    @Size(min = 3, max = 30)
    private String surname;

    @NotNull
    @Size(min = 5, max = 35, message = "password need to have 5 to 35 signs")
    @Column(length = 255)
    transient private String password;

    private String passwordEncrypted;

    @NotNull
    @Size(min = 3, max = 30)
    @Email
    @Column(unique = true)
    private String login;

    @NotNull
    private Boolean active = false;

    private ZonedDateTime created;
    private ZonedDateTime lastLogged;
    private ZonedDateTime activated;
    private ZonedDateTime passwordChanged;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by")
    private Set<Permission> hadCreatedPrermissions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "permission_for")
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "warned")
    private Set<Warning> warnings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, targetEntity = UserProfile.class, mappedBy = "user")
    private Set<UserProfile> userProfile;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Worker.class, mappedBy = "user")
    private Set<Worker> workers;


    public User() {
    }

    public User(String name, String surName, String login, String password, Boolean active) {
        this.name = name;
        this.active = active;
        this.surname = surName;
        this.password = password;
        this.login = login;
    }

    public User(int id, String name, String surName, String password, String login, Boolean active) {
        this.id = id;
        this.active = active;
        this.name = name;
        this.surname = surName;
        this.password = password;
        this.login = login;
    }

    public User(User user) {
        this.activated = user.getActivated();
        this.active = user.getActive();
        this.created = user.getCreated();
        this.id = user.getId();
        this.login = user.getLogin();
        this.name = user.getName();
        this.passwordEncrypted = user.getPasswordEncrypted();
        this.passwordChanged = user.getPasswordChanged();
        this.permissions = user.getPermissions();
        this.roles = user.getRoles();
        this.surname = user.getSurname();
        this.warnings = user.getWarnings();
    }

    public User(String login) {
        this.login = login;
    }

    public Integer getId() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surName) {
        this.surname = surName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Permission> getHadCreatedPrermissions() {
        return hadCreatedPrermissions;
    }

    public void setHadCreatedPrermissions(Permission prermision) {
        this.hadCreatedPrermissions.add(prermision);
    }

    public void setHadCreatedPrermissions(Set<Permission> hadCreatedPrermissions) {
        this.hadCreatedPrermissions = hadCreatedPrermissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        if (this.permissions == null) {
            this.permissions = new HashSet<>();
        }
        this.permissions.add(permission);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public String getPasswordEncrypted() {
        return passwordEncrypted;
    }

    public void setPasswordEncrypted(String passwordEncrypted) {
        this.passwordEncrypted = passwordEncrypted;
    }

    public Set<Warning> getWarnings() {
        return warnings;
    }

    public void setWarnings(Set<Warning> warnings) {
        this.warnings = warnings;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(ZonedDateTime lastLogged) {
        this.lastLogged = lastLogged;
    }

    public ZonedDateTime getActivated() {
        return activated;
    }

    public void setActivated(ZonedDateTime activated) {
        this.activated = activated;
    }

    public ZonedDateTime getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(ZonedDateTime passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addWarning(Warning warning) {
        warnings.add(warning);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Set<UserProfile> userProfile) {
        this.userProfile = userProfile;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }

    public boolean equals(User u) {
        return u.getLogin() == getLogin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", surname='" + surname + '\''
                + ", password='" + password + '\''
                + ", passwordEncrypted='" + passwordEncrypted + '\''
                + ", login='" + login + '\''
                + ", active=" + active
                + '}';
    }

    public User copy() {
        return new User(this);
    }
}