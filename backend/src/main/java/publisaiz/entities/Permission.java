package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@ApiIgnore
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    //@NotNull
    private boolean active;
    //@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @Fetch(FetchMode.JOIN)
    private User createdBy;
    //@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_for")
    @Fetch(FetchMode.JOIN)
    private User permissionFor;
    //@NotNull
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @Fetch(FetchMode.JOIN)
    private Set<Controller> controllers = new HashSet<>();
    @Version
    private
    long version;

    public Permission() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Controller> getControllers() {
        return controllers;
    }

    public void setControllers(Set<Controller> controllers) {
        this.controllers = controllers;
    }

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addController(Controller controller) {
        controllers.add(controller);
    }

    public void removeController(Controller controller) {
        this.controllers.remove(controller);
    }

    public void removeControllers(Collection<Controller> controllers) {
        this.controllers.removeAll(controllers);
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getPermissionFor() {
        return permissionFor;
    }

    public void setPermissionFor(User user) {
        this.permissionFor = user;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Permission{"
                + "id=" + id
                + ", fromDate=" + fromDate
                + ", toDate=" + toDate
                + ", active=" + active
                + ", createdBy=" + createdBy
                + ", permissionFor=" + permissionFor
                + ", controllers=" + controllers
                + '}';
    }
}
