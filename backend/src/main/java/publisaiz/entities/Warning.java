package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@ApiIgnore
public class Warning {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private ZonedDateTime created;
    private ZonedDateTime presented;
    private ZonedDateTime confirmed;
    private Boolean hide;
    @NotNull
    private String message;
    // private WarningCathegory cathegory;
    @ManyToOne
    @JoinColumn(name = "warned_id")
    private User warned;

    public Warning() {
    }

    public Warning(String message, User user) {
        setMessage(message);
        setWarned(user);
        //setCathegory(cathegory);
    }

    public Warning(String message) {
        setMessage(message);
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getPresented() {
        return presented;
    }

    public void setPresented(ZonedDateTime presented) {
        this.presented = presented;
    }

    public ZonedDateTime getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(ZonedDateTime confirmed) {
        this.confirmed = confirmed;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public User getWarned() {
        return warned;
    }

    private void setWarned(User warned) {
        this.warned = warned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warning that = (Warning) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    private void created() {
        if (created == null)
            created = ZonedDateTime.now();
    }
}
