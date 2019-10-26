package publisaiz.functionalities.roles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import publisaiz.api.dto.RoleDTO;
import publisaiz.config.swagger.ApiPageable;

@RestController
@RequestMapping("api/roles")
class RolesController {

    private final static Logger logger = LoggerFactory.getLogger(RoleService.class);
    private final RoleService roleService;

    public RolesController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    @ApiPageable
    public Page<RoleDTO> getAll(Pageable pageable) {
        return roleService.findAll(pageable);
    }

    @PostMapping
    public RoleDTO save(@RequestBody RoleDTO role) {
        return roleService.save(role);
    }

    @DeleteMapping("role:{roleId}/user:{login}")
    public ResponseEntity<RoleDTO> removeUser(@PathVariable("roleId") Integer roleId, @PathVariable("login") Integer userId) {
        logger.info("(@PathParam(\"roleId\") Integer [{}], @PathParam(\"login\") Integer [{}])", roleId, userId);
        RoleDTO roleDTO = roleService.removeUser(roleId, userId);
        if (roleDTO == null) return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok(roleDTO);
    }

    @DeleteMapping("role:{roleId}/controller:{controllerId}")
    public ResponseEntity<RoleDTO> removeController(@PathVariable("roleId") Integer roleId, @PathVariable("controllerId") Integer controllerId) {
        logger.info("(@PathParam(\"roleId\") Integer [{}], @PathParam(\"controllerId\") Integer [{}])", roleId, controllerId);
        RoleDTO roleDTO = roleService.removeController(roleId, controllerId);
        if (roleDTO == null) return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok(roleDTO);
    }

}
