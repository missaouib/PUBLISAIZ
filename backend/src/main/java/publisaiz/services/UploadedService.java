package publisaiz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import publisaiz.config.Logged;
import publisaiz.controller.api.dto.UploadedDTO;
import publisaiz.datasources.database.entities.Uploaded;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.ArticleRepository;
import publisaiz.datasources.database.repository.UploadedRepository;
import publisaiz.exceptions.FileStorageException;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class UploadedService {

    private static final Logger logger = LoggerFactory.getLogger(UploadedService.class);
    private final UploadedRepository uploadedRepository;
    private final ArticleRepository articleRepository;
    private final Path fileStorageLocation;
    private final Logged logged;


    public UploadedService(UploadedRepository uploadedRepository, ArticleRepository articleRepository, Environment environment, Logged logged) throws FileStorageException {
        this.uploadedRepository = uploadedRepository;
        this.articleRepository = articleRepository;
        this.fileStorageLocation = Paths.get(environment.getProperty("file.upload-dir"))
                .toAbsolutePath().normalize();
        this.logged = logged;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Page<Uploaded> findByOwner(User owner, Pageable pageable) {
        return uploadedRepository.findByOwner(owner, pageable);
    }

    public Optional<Uploaded> findById(Long id) {
        Optional<Uploaded> file = uploadedRepository.findById(id);
        logger.info("findById [{}]", file);
        return file;
    }


    public File getFileById(Long id) {
        Optional<Uploaded> byId = uploadedRepository.findById(id);
        if (byId.isEmpty())
            throw new IllegalArgumentException("no such file");
        File file = new File(byId.get().getFileStorage());
        return file;
    }

    public void delete(Uploaded uploaded, User logged) {
        File file = null;
        if (uploaded != null && uploaded.getOwner().equals(logged)) {
            file = new File(uploaded.getFileStorage());
            uploadedRepository.delete(uploaded);
            if (file != null && file.exists())
                file.delete();
        }
    }

    public Long saveFile(MultipartFile file, User user, Long id) throws FileStorageException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Long fileId = null;
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Uploaded uploaded = uploadedRepository.save(new Uploaded(targetLocation, user));
            logger.info("saved in [{}]", targetLocation.toAbsolutePath());
            addFileToArticle(id, uploaded);
            fileId = uploaded.getId();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
        return fileId;
    }

    private void addFileToArticle(Long id, Uploaded uploaded) {
        if (id != null)
            articleRepository.findById(id).get().setImage("files/one/" + uploaded.getId());
    }

    public ResponseEntity<?> findByOwner(Pageable pageable) {
        return ResponseEntity.ok(UploadedDTO.create(findByOwner(logged.getUser(), pageable)));
    }
}
