package publisaiz.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import publisaiz.controller.api.dto.PermissionDTO;
import publisaiz.services.PermissionService;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;


@RestController
@RequestMapping("api/permissions")
@CrossOrigin(origins = {"http://localhost:4200", "http://publisaiz"}, maxAge=3600,
        allowCredentials = "true", methods = {
        POST, GET, PATCH, PUT, DELETE, OPTIONS
})
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity get(Pageable pageable) {
        return permissionService.findAll(pageable);
    }
    @CrossOrigin(
            origins = {"http://localhost:4200", "http://publisaiz"},
            maxAge=3600,
            allowCredentials = "true")
    @PostMapping
    public ResponseEntity post(@RequestBody PermissionDTO permissionDTO) {
        return permissionService.save(permissionDTO);
    }

}
