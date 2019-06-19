package publisaiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import publisaiz.controller.api.dto.ControllerDTO;
import publisaiz.services.ControllerService;

@RestController
@RequestMapping("api/controllers")
@CrossOrigin(origins = {"http://localhost:4200", "http://publisaiz"}, maxAge=3600,
        allowCredentials = "true" )
public class ControllersController {

    private final ControllerService controllerService;
    private final Logger logger = LoggerFactory.getLogger(ControllerService.class);

    public ControllersController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping
    public ResponseEntity<?> all(@PageableDefault(  sort = {"controller", "id"},
                                                    direction = Sort.Direction.ASC,
            page = 0, size = 5)
                                                                Pageable pageable) {
        logger.info("get, [{}]", pageable);
        return controllerService.findAll(pageable);
    }
    
    @PostMapping
    public ResponseEntity<?> edit(@RequestBody ControllerDTO dto) {
        logger.info("post, [{}]", dto);
        return controllerService.save(dto);
    }
}
