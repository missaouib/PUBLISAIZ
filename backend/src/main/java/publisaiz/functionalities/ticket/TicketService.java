package publisaiz.functionalities.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import publisaiz.api.dto.TicketDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.Ticket;
import publisaiz.entities.TicketState;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
class TicketService {

    private final TicketRepository ticketRepository;
    private final Logged logged;

    public TicketService(TicketRepository ticketRepository, UserRepositoryForTicket userRepositoryForTicket, Logged logged) {
        this.ticketRepository = ticketRepository;
        this.logged = logged;
    }

    Page<TicketDTO> getTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable).map(t -> new TicketDTO(t));
    }

    TicketDTO save(TicketDTO ticketDto) {
        return new TicketDTO(ticketRepository.save(Ticketdto2Ticket.ticketDTOtoEntity(ticketDto, this, logged)));
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public Page<TicketDTO> getAll(Pageable pageable) {
        return ticketRepository.findByDeletedFalse(pageable).map(TicketDTO::new);
    }

    public Page<TicketDTO> getCreatedByMe(Pageable pageable) {
        return ticketRepository.findByCreatedbyAndDeletedFalse(logged.getUser(), pageable).map(TicketDTO::new);
    }

    public Page<TicketDTO> getAssignedToMe(Pageable pageable) {
        return ticketRepository.findByAssignedtoAndDeletedFalse(logged.getUser(), pageable);
    }

    public Page<TicketDTO> getByState(TicketState state, Pageable pageable) {
        return ticketRepository.findByStateAndDeletedFalse(state, pageable).map(TicketDTO::new);
    }
}
