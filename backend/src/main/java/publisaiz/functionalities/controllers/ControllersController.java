package publisaiz.functionalities.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import publisaiz.api.dto.ControllerDTO;
import publisaiz.config.swagger.ApiPageable;

@RestController
@RequestMapping("api/controllers")
class ControllersController {

    private final ControllerService controllerService;
    private final Logger logger = LoggerFactory.getLogger(ControllerService.class);

    public ControllersController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping
    @ApiPageable
    public Page<ControllerDTO> all(@PageableDefault(sort = {"controller", "id"},
            direction = Sort.Direction.ASC,
            size = 5) Pageable pageable) {
        logger.info("get, [{}]", pageable);
        Page<ControllerDTO> controllersPage = controllerService.findAll(pageable);
        return controllersPage;
    }

    @PostMapping
    public ResponseEntity<ControllerDTO> edit(@RequestBody ControllerDTO dto) {
        logger.info("post, [{}]", dto);
        return controllerService.save(dto);
    }
}
