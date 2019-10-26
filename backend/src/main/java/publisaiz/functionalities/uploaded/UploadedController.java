package publisaiz.functionalities.uploaded;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import publisaiz.api.dto.UploadedDTO;
import publisaiz.config.logged.Logged;
import publisaiz.config.swagger.ApiPageable;
import publisaiz.entities.Uploaded;
import publisaiz.functionalities.uploaded.FilesProcessor.UploadFileResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping({"api/files"})
class UploadedController {

    private final static Logger logger = LoggerFactory.getLogger(UploadedController.class);
    private final UploadedService uploadedService;
    private final Logged logged;
    private final FilesProcessor filesProcessor;

    public UploadedController(UploadedService uploadedService, Logged logged, FilesProcessor filesProcessor, Environment environment) {
        this.uploadedService = uploadedService;
        this.logged = logged;
        this.filesProcessor = filesProcessor;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
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
    @ApiPageable
    @ApiOperation(value = "Fetches uploaded files by its owner", response = UploadedDTO.class)
    public Page<UploadedDTO> findByOwner(@PageableDefault(size = 3, direction = Sort.Direction.DESC, sort = "id") Pageable pageable) {
        logger.info("getAll: [{}]", pageable);
        return uploadedService.findByOwner(pageable);
    }

    @PostMapping
    public UploadFileResponse postFile(@RequestBody MultipartFile file)
            throws FileStorageException {
        logger.info("resolving file: [{}]", file.getName());
        return uploadedService.processPostRequest(file, null);
    }

    @PostMapping("/article")
    public UploadFileResponse postImageToArticle(@RequestBody MultipartFile file, HttpServletRequest r)
            throws FileStorageException {
        logger.info("resolving request: [{}]", r);
        return uploadedService.processPostRequest(file, r);
    }

}