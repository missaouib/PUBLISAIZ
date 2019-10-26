package publisaiz.entities;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private String title;
    @Size(min = 3, max = 30)
    private String description;
    @NotNull
    @OneToOne
    @JoinColumn(name = "created_by")
    private User createdby;
    private TicketState state;
    private Boolean deleted = false;
    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", name = "created")
    private ZonedDateTime createdDate;
    @Column(columnDefinition = "TIMESTAMP", name = "disabled")
    private ZonedDateTime disabledDate;
    @Nullable
    @OneToOne
    @JoinColumn(name = "assigned_to")
    private User assignedto;
    @ManyToMany
    @JoinTable(name = "ticket_user",
            joinColumns = {@JoinColumn(name = "ticket_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> observers = new ArrayList<>();

    @OneToMany(mappedBy = "ticket")
    private List<TicketActivity> activity = new ArrayList<>();

    public Ticket() {
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreatedby() {
        return createdby;
    }

    public void setCreatedby(User createdby) {
        this.createdby = createdby;
    }

    public User getAssignedto() {
        return assignedto;
    }

    public void setAssignedto(User assignedto) {
        this.assignedto = assignedto;
    }

    public List<User> getObservers() {
        return observers;
    }

    public void setObservers(List<User> observers) {
        this.observers = observers;
    }

    public List<TicketActivity> getActivity() {
        return activity;
    }

    public void setActivity(List<TicketActivity> activity) {
        this.activity = activity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketState getState() {
        return state;
    }

    public void setState(TicketState state) {
        this.state = state;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(ZonedDateTime disabledDate) {
        this.disabledDate = disabledDate;
    }
}
