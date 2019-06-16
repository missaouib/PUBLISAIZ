package publisaiz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;

@RestController
@RequestMapping("api/error")
@CrossOrigin(origins = {"http://localhost:4200", "http://publisaiz"}, maxAge=3600,
        allowCredentials = "true", methods = {
        POST, GET, PATCH, PUT, DELETE, OPTIONS
})
public class ErrorController {

    @GetMapping
    public ResponseEntity<String> err() {
        return ResponseEntity.unprocessableEntity().body("it is far from ok!");
    }

}
