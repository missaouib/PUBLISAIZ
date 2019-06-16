package publisaiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import publisaiz.config.Logged;
import publisaiz.datasources.database.entities.Uploaded;
import publisaiz.exceptions.FileStorageException;
import publisaiz.services.FilesProcessor;
import publisaiz.services.FilesProcessor.UploadFileResponse;
import publisaiz.services.UploadedService;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping({"api/files", "files"})
@CrossOrigin(origins = {"http://localhost:4200", "http://publisaiz"}, maxAge=3600,
        allowCredentials = "true", methods = {
        POST, GET, PATCH, PUT, DELETE, OPTIONS
})
public class UploadedController {

    private final static Logger logger = LoggerFactory.getLogger(UploadedController.class);
    private final UploadedService uploadedService;
    private final Logged logged;
    private final FilesProcessor filesProcessor;
    private final Environment environment;

    public UploadedController(Environment environment, UploadedService uploadedService, Logged logged, FilesProcessor filesProcessor1) {
        this.uploadedService = uploadedService;
        this.logged = logged;
        this.filesProcessor = filesProcessor1;
        this.environment = environment;
    }

    @DeleteMapping
    public void delete(@RequestParam(value = "id") Long id) {
        Uploaded uploaded = null;
        if (id != null && id > 0) {
            Optional<Uploaded> byId = uploadedService.findById(id);
            if (byId.isEmpty())
                throw new IllegalArgumentException("no such file");
            uploaded = byId.get();
        }
        if (uploaded != null && logged.getUser() != null) {
            uploadedService.delete(uploaded, logged.getUser());
        }
    }

    @GetMapping(value = "one/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getOne(@PathVariable(value = "id") Long id) throws IOException {
        logger.info("get by id: [{}]", id);
        if (id != null)
            return filesProcessor.formResponse(uploadedService.findById(id)).readAllBytes();
        else
            throw new IllegalStateException("unknown problem");
    }

    @GetMapping("all")
    public ResponseEntity<?> findByOwner(Pageable pageable) throws IOException {
        logger.info("getAll: [{}]", pageable);
        return uploadedService.findByOwner(pageable);
    }
    @CrossOrigin(
            origins = {"http://localhost:4200", "http://publisaiz"},
            maxAge=3600,
            allowCredentials = "true")
    @PostMapping
    public UploadFileResponse post(@RequestBody MultipartFile file,
                                   @RequestParam(value = "id",
                                           required = false) Long id)
            throws FileStorageException {
        Long fileId = uploadedService.saveFile(file, logged.getUser(), id);
        String fileDownloadUri =
                environment.getProperty("publisaiz.api")
                        .concat("files")
                        .concat("/one/")
                        .concat(fileId.toString());
        return new UploadFileResponse(fileId, fileDownloadUri, file.getContentType(), file.getSize());
    }

}