package publisaiz.entities;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class TicketActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private ZonedDateTime date;
    @Size(min = 3, max = 10000)
    private String comment;
    @ManyToOne(targetEntity = User.class, optional = false, cascade = CascadeType.MERGE)
    private User user;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    @Fetch(FetchMode.JOIN)
    private Ticket ticket;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TicketState ticketStateBefore;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TicketState ticketStateAfter;

    public TicketActivity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public TicketState getTicketStateAfter() {
        return ticketStateAfter;
    }

    public void setTicketStateAfter(TicketState ticketStateAfter) {
        this.ticketStateAfter = ticketStateAfter;
    }

    public TicketState getTicketStateBefore() {
        return ticketStateBefore;
    }

    public void setTicketStateBefore(TicketState ticketStateBefore) {
        this.ticketStateBefore = ticketStateBefore;
    }
}
