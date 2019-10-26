package publisaiz.functionalities.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import publisaiz.api.dto.UserDTO;
import publisaiz.api.dto.UserProfileDTO;
import publisaiz.config.swagger.ApiPageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("api/users")
class UsersController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiPageable
    public Page<UserDTO> users(Pageable pageable) {
        logger.info("users [{}]", pageable);
        return userService.getAllUsers(pageable);
    }

    @PostMapping
    public ResponseEntity<?> update(@RequestBody @Valid UserDTO userDTO, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors());
        logger.info("update userDTO [{}]", userDTO);
        return userService.update(userDTO);
    }

    @GetMapping("whoami")
    public ResponseEntity<?> whoami() {
        return userService.whoami();
    }

    @GetMapping("logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("authors")
    @ApiPageable
    public ResponseEntity<?> getAuthors(Pageable pageable) {
        return userService.getAuthors(pageable);
    }

    @GetMapping("my_profile")
    @ApiPageable
    public Page<UserProfileDTO> getMyProfile(Pageable pageable) {
        return userService.getMyProfile(pageable);
    }

    @PostMapping("my_profile")
    public ResponseEntity<UserProfileDTO> setMyProfile(@RequestBody UserProfileDTO profile) {
        return userService.setMyProfile(profile);
    }

    @DeleteMapping("delete_profile/{id}")
    public boolean deleteMyProfile(@PathVariable Long id) {
        return userService.deleteMyProfile(id);
    }
}
