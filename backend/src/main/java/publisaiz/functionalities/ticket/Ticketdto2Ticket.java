package publisaiz.functionalities.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import publisaiz.api.dto.TicketDTO;
import publisaiz.api.dto.UserDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.Ticket;
import publisaiz.entities.User;

import java.util.Optional;

class Ticketdto2Ticket {

    private static final Logger logger = LoggerFactory.getLogger(Ticketdto2Ticket.class);

    static Ticket ticketDTOtoEntity(TicketDTO ticketDTO, TicketService ticketServie, Logged logged) {
        Optional<Ticket> optional = Optional.empty();
        if (ticketDTO.getId() != null)
            optional = ticketServie.findById(ticketDTO.getId());
        Ticket ticket = optional.orElse(new Ticket());
        logger.info("found: ticket [{}]", ticket);
        bindTicket(ticketDTO, ticketServie, ticket);
        logger.info("binded: ticketDTO [{}] to ticket [{}] by user [{}]", ticketDTO, ticket, logged);
        return ticket;
    }

    static private void bindTicket(TicketDTO ticketDTO, TicketService ticketService, Ticket ticket) {
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDisabledDate(ticketDTO.getDeletedDate());
        ticket.setCreatedDate(ticketDTO.getCreatedDate());
        ticket.setAssignedto(ticketassignedtoToEntity(ticketDTO.getAssignedTo(), ticketService));
    }

    static private User ticketassignedtoToEntity(UserDTO assignedTo, TicketService ticketService) {
        return null;
    }
}