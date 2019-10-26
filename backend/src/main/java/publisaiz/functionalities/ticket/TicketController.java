package publisaiz.functionalities.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import publisaiz.api.dto.TicketDTO;
import publisaiz.config.swagger.ApiPageable;
import publisaiz.entities.TicketState;

@RestController
@RequestMapping({"api/tickets"})
class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    @ApiPageable
    public Page<TicketDTO> tickets(Pageable pageable) {
        return ticketService.getAll(pageable);
    }

    @GetMapping("created_by_me")
    @ApiPageable
    public Page<TicketDTO> getCreatedByMe(Pageable pageable) {
        return ticketService.getCreatedByMe(pageable);
    }

    @GetMapping("assigned_to_me")
    @ApiPageable
    public Page<TicketDTO> getAssignedToMe(Pageable pageable) {
        return ticketService.getAssignedToMe(pageable);
    }

    @GetMapping("state/{state}")
    @ApiPageable
    public Page<TicketDTO> getByState(TicketState state, Pageable pageable) {
        return ticketService.getByState(state, pageable);
    }

}
