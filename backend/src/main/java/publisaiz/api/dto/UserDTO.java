package publisaiz.api.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import publisaiz.entities.User;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

//hadCreatedPrermissions
public class UserDTO {

    private static Logger logger = LoggerFactory.getLogger(UserDTO.class);
    private int id;
    @Size(min = 3, max = 30)
    private String name;
    @Size(min = 3, max = 30)
    private String surname;
    @NotNull
    @Size(min = 5, max = 35, message = "password need to have 5 to 35 signs")
    @Column()
    private String password;
    @NotNull
    @Size(min = 3, max = 30)
    @Email
    private String login;
    @NotNull
    private Boolean active = false;
    private String created;
    private String lastLogged;
    private String activated;
    private String passwordChanged;
    private Set<PermissionDTO> permissions = new HashSet<>();
    private Set<String> roles = new HashSet<>();
    private Set<String> warnings = new HashSet<>();

    public UserDTO(User user) {
        if (user == null)
            return;
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.password = user.getPassword();
        this.login = user.getLogin();
        this.active = user.getActive();
        if (user.getCreated() != null)
            this.created = user.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (user.getLastLogged() != null)
            this.lastLogged = user.getLastLogged().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (user.getActivated() != null)
            this.activated = user.getActivated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (user.getPasswordChanged() != null)
            this.passwordChanged = user.getPasswordChanged().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        this.permissions = user.getPermissions()
                .stream()
                .map(PermissionDTO::new)
                .collect(Collectors.toSet());

        this.roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
        this.warnings = user.getWarnings().stream().filter(w -> w.getConfirmed() == null).map(w -> w.getMessage()).collect(Collectors.toSet());
    }

    private UserDTO() {
    }

    private int getId() {
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

    public Set<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDTO> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getRoles() {
        return roles;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(Set<String> warnings) {
        this.warnings = warnings;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(String lastLogged) {
        this.lastLogged = lastLogged;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public String getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(String passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public boolean equals(User u) {
        return u.getLogin().equals(getLogin());
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
                + ", login='" + login + '\''
                + ", active=" + active
                + '}';
    }
}