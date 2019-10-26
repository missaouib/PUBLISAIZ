package publisaiz.api.dto;

import publisaiz.entities.Ticket;
import publisaiz.entities.TicketActivity;
import publisaiz.entities.TicketState;
import publisaiz.entities.User;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TicketDTO {

    private List<TicketActivity> activity;
    private String description;
    private List<UserDTO> observers;
    private UserDTO createdBy;
    private TicketState state;
    private ZonedDateTime deletedDate;
    private ZonedDateTime createdDate;
    private UserDTO assignedTo;
    private String title;
    private Long id;

    public TicketDTO(Ticket t) {
        activity = t.getActivity();
        description = t.getDescription();
        observers = t.getObservers().stream().map(UserDTO::new).collect(Collectors.toList());
        title = t.getTitle();
        assignedTo = new UserDTO(t.getAssignedto());
        createdDate = t.getCreatedDate();
        deletedDate = t.getDisabledDate();
        state = t.getState();
        createdBy = new UserDTO(t.getCreatedby());
        id = t.getId();
    }

    public TicketDTO() {

    }

    public List<TicketActivity> getActivity() {
        return activity;
    }

    public void setActivity(List<TicketActivity> activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketState getState() {
        return state;
    }

    public void setState(TicketState state) {
        this.state = state;
    }

    public ZonedDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(ZonedDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserDTO> getObservers() {
        return observers;
    }

    public void setObservers(List<UserDTO> observers) {
        this.observers = observers;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public UserDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    static class TicketActivityDto {

        private Long id;
        private ZonedDateTime date;
        private User user;
        private TicketState ticketStateAfter;
        private TicketState ticketStateBefore;
        private String comment;

        public TicketActivityDto(TicketActivity ticketActivity) {
            comment = ticketActivity.getComment();
            date = ticketActivity.getDate();
            id = ticketActivity.getId();
            ticketStateBefore = ticketActivity.getTicketStateBefore();
            ticketStateAfter = ticketActivity.getTicketStateAfter();
            user = ticketActivity.getUser();
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

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

}
