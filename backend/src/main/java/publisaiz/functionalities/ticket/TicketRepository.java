package publisaiz.functionalities.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publisaiz.api.dto.TicketDTO;
import publisaiz.entities.Ticket;
import publisaiz.entities.TicketState;
import publisaiz.entities.User;

@Repository
interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByCreatedbyAndDeletedFalse(User user, Pageable pageable);

    Page<Ticket> findByStateAndDeletedFalse(TicketState state, Pageable pageable);

    Page<Ticket> findByStateAndCreatedbyAndDeletedFalse(TicketState state, User user, Pageable pageable);

    Page<Ticket> findByDeletedFalse(Pageable pageable);

    Page<TicketDTO> findByAssignedtoAndDeletedFalse(User user, Pageable pageable);

}