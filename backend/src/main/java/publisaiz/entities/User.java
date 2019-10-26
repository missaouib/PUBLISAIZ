package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"login"}))
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@ApiIgnore
public class User implements UserDetails {

    @Version
    private
    long version;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Article.class, mappedBy = "author")
    private
    Set<Article> articles = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, targetEntity = UserProfile.class, mappedBy = "user", cascade = CascadeType.ALL)
    private
    Set<UserProfile> profiles = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Size(min = 3, max = 30)
    private String name;
    @Size(min = 3, max = 30)
    private String surname;
    @NotNull
    @Size(min = 5, max = 35, message = "password need to have 5 to 35 signs")
    @Column()
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
    @OneToMany(fetch = FetchType.LAZY, targetEntity = Worker.class, mappedBy = "user")
    private Set<Worker> workers = new HashSet<>();


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

    private void setId(Integer id) {
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
        generateNameAndSurnameFromLogin(login);
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

    private void setHadCreatedPrermissions(Set<Permission> hadCreatedPrermissions) {
        this.hadCreatedPrermissions = hadCreatedPrermissions;
    }

    public void addHadCreatedPrermissions(Permission prermision) {
        this.hadCreatedPrermissions.add(prermision);
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    private void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        if (this.permissions == null) {
            this.permissions = new HashSet<>();
        }
        this.permissions.add(permission);
    }

    public Set<Article> getArticles() {
        return articles;
    }

    private void setArticles(Set<Article> articles) {
        this.articles = articles;
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

    private void setWarnings(Set<Warning> warnings) {
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

    private void setPasswordChanged(ZonedDateTime passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addWarning(Warning warning) {
        warnings.add(warning);
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    private void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }

    public boolean equals(User u) {
        return u.getLogin().equals(getLogin());
    }

    public Set<UserProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<UserProfile> profiles) {
        this.profiles = profiles;
    }

    public long getVersion() {
        return version;
    }

    private void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "User{" + '\n'
                + "id=" + id
                + ", name='" + name + '\''
                + ", surname='" + surname + '\''
                + ", password='" + password + '\''
                + ", passwordEncrypted='" + passwordEncrypted + '\''
                + ", login='" + login + '\''
                + ", active=" + active
                + ", version=" + version
                + '}';
    }

    public User copy() {
        return new User(this);
    }


    private void generateNameAndSurnameFromLogin(String login) {
        if (login.contains("@") && name == null && surname == null) {
            List<String> coll = new ArrayList<>();
            String[] parts = login.split("@");
            Arrays.asList(parts).forEach(a -> {
                if (a.contains(".")) {
                    Arrays.asList(a.split("\\.")).forEach(b -> coll.add(b));
                } else if (a.contains("_")) {
                    Arrays.asList(a.split("_")).forEach(b -> coll.add(b));
                } else {
                    coll.add(a);
                }
            });
            Iterator<String> it = coll.iterator();
            if (it.hasNext()) {
                String n = it.next();
                if (n.length() > 2 && n.length() < 30) name = n;
            }
            if (it.hasNext()) {
                String s = it.next();
                if (s.length() > 2 && s.length() < 30) surname = s;
            }
        }
    }


    public static final class UserBuilder {
        long version;
        Set<Article> articles = new HashSet<>();
        Set<UserProfile> profiles = new HashSet<>();
        private Integer id;
        private String name;
        private String surname;
        transient private String password;
        private String passwordEncrypted;
        private String login;
        private Boolean active = false;
        private ZonedDateTime created;
        private ZonedDateTime lastLogged;
        private ZonedDateTime activated;
        private ZonedDateTime passwordChanged;
        private Set<Permission> hadCreatedPrermissions = new HashSet<>();
        private Set<Permission> permissions = new HashSet<>();
        private Set<Role> roles = new HashSet<>();
        private Set<Warning> warnings = new HashSet<>();
        private Set<Worker> workers = new HashSet<>();

        private UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withVersion(long version) {
            this.version = version;
            return this;
        }

        public UserBuilder withArticles(Set<Article> articles) {
            this.articles = articles;
            return this;
        }

        public UserBuilder withProfiles(Set<UserProfile> profiles) {
            this.profiles = profiles;
            return this;
        }

        public UserBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public UserBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withPasswordEncrypted(String passwordEncrypted) {
            this.passwordEncrypted = passwordEncrypted;
            return this;
        }

        public UserBuilder withLogin(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder withActive(Boolean active) {
            this.active = active;
            return this;
        }

        public UserBuilder withCreated(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public UserBuilder withLastLogged(ZonedDateTime lastLogged) {
            this.lastLogged = lastLogged;
            return this;
        }

        public UserBuilder withActivated(ZonedDateTime activated) {
            this.activated = activated;
            return this;
        }

        public UserBuilder withPasswordChanged(ZonedDateTime passwordChanged) {
            this.passwordChanged = passwordChanged;
            return this;
        }

        public UserBuilder withHadCreatedPrermissions(Set<Permission> hadCreatedPrermissions) {
            this.hadCreatedPrermissions = hadCreatedPrermissions;
            return this;
        }

        public UserBuilder withPermissions(Set<Permission> permissions) {
            this.permissions = permissions;
            return this;
        }

        public UserBuilder withRoles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public UserBuilder withWarnings(Set<Warning> warnings) {
            this.warnings = warnings;
            return this;
        }

        public UserBuilder withWorkers(Set<Worker> workers) {
            this.workers = workers;
            return this;
        }

        public User build(User user) {
            user.setVersion(version);
            user.setArticles(articles);
            user.setProfiles(profiles);
            user.setId(id);
            user.setName(name);
            user.setSurname(surname);
            user.setPassword(password);
            user.setPasswordEncrypted(passwordEncrypted);
            user.setLogin(login);
            user.setActive(active);
            user.setCreated(created);
            user.setLastLogged(lastLogged);
            user.setActivated(activated);
            user.setPasswordChanged(passwordChanged);
            user.setHadCreatedPrermissions(hadCreatedPrermissions);
            user.setPermissions(permissions);
            user.setRoles(roles);
            user.setWarnings(warnings);
            user.setWorkers(workers);
            return user;
        }

        public User build() {
            User user = new User();
            user.setVersion(version);
            user.setArticles(articles);
            user.setProfiles(profiles);
            user.setId(id);
            user.setName(name);
            user.setSurname(surname);
            user.setPassword(password);
            user.setPasswordEncrypted(passwordEncrypted);
            user.setLogin(login);
            user.setActive(active);
            user.setCreated(created);
            user.setLastLogged(lastLogged);
            user.setActivated(activated);
            user.setPasswordChanged(passwordChanged);
            user.setHadCreatedPrermissions(hadCreatedPrermissions);
            user.setPermissions(permissions);
            user.setRoles(roles);
            user.setWarnings(warnings);
            user.setWorkers(workers);
            return user;
        }
    }
}