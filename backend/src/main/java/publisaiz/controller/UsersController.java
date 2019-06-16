package publisaiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import publisaiz.config.Logged;
import publisaiz.controller.api.dto.UserDTO;
import publisaiz.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = {"http://localhost:4200", "http://publisaiz"}, maxAge=3600,
        allowCredentials = "true", methods = {
        POST, GET, PATCH, PUT, DELETE, OPTIONS
})
public class UsersController {

    private final UserService userService;
    private final Logged logged;
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    public UsersController(Logged logged, UserService userService) {
        this.userService = userService;
        this.logged = logged;
    }

    @GetMapping
    public ResponseEntity<?> users(Pageable pageable) {
        logger.info("users [{}]", pageable);
        return userService.getAllUsers(pageable);
    }
    @CrossOrigin(
            origins = {"http://localhost:4200", "http://publisaiz"},
            maxAge=3600,
            allowCredentials = "true")
    @PostMapping
    public ResponseEntity update(@RequestBody @Valid UserDTO userDTO, BindingResult result) {
        if(result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors());
        logger.info("update userDTO [{}]", userDTO);
        return userService.update(userDTO);
    }

    @GetMapping("whoami")
    public ResponseEntity<?> whoami() {
        return userService.whoami(logged);
    }

    @GetMapping("logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }
}
