package publisaiz.functionalities.permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import publisaiz.api.dto.PermissionDTO;
import publisaiz.config.swagger.ApiPageable;


@RestController
@RequestMapping("api/permissions")
class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @ApiPageable
    public Page<PermissionDTO> get(Pageable pageable) {
        return permissionService.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<PermissionDTO> post(@RequestBody PermissionDTO permissionDTO) {
        return permissionService.save(permissionDTO);
    }

}
