package publisaiz.functionalities.uploaded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import publisaiz.api.dto.UploadedDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.Article;
import publisaiz.entities.Uploaded;
import publisaiz.entities.User;

import javax.servlet.http.HttpServletRequest;
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
class UploadedService {

    private static final Logger logger = LoggerFactory.getLogger(UploadedService.class);
    private final UploadedRepository uploadedRepository;
    private final ArticleRepositoryForUploaded articleRepositoryForUploaded;
    private final Path fileStorageLocation;
    private final Logged logged;
    private final Environment environment;

    public UploadedService(UploadedRepository uploadedRepository, ArticleRepositoryForUploaded articleRepositoryForUploaded, Environment environment, Logged logged) throws FileStorageException {
        this.uploadedRepository = uploadedRepository;
        this.articleRepositoryForUploaded = articleRepositoryForUploaded;
        this.environment = environment;
        this.fileStorageLocation = Paths.get(environment.getProperty("file.upload-dir"))
                .toAbsolutePath().normalize();
        this.logged = logged;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            logger.info("Could not create the directory where the uploaded files will be stored.");
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    private Page<Uploaded> findByOwner(User owner, Pageable pageable) {
        return uploadedRepository.findByOwnerAndHideFalse(owner, pageable);
    }

    public Optional<Uploaded> findById(Long id) {
        Optional<Uploaded> file = uploadedRepository.findByIdAndHideFalse(id);
        logger.info("findByUrl [{}]", file);
        if (file.isPresent() && file.get().getHide())
            return Optional.empty();
        return file;
    }


    public File getFileById(Long id) {
        Optional<Uploaded> byId = uploadedRepository.findByIdAndHideFalse(id);
        if (byId.isEmpty())
            throw new IllegalArgumentException("no such file");
        File file = new File(byId.get().getFileStorage());
        return file;
    }

    public void delete(Uploaded uploaded, User logged) {
        File file = null;
        if (uploaded != null && uploaded.getOwner().equals(logged)) {
            file = new File(uploaded.getFileStorage());
            uploaded.setHide(true);
            uploadedRepository.save(uploaded);
        }
    }

    private Uploaded saveFile(MultipartFile file, User user, String id) throws FileStorageException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Long fileId = null;
        try {
            validateParameters(file, user, fileName);
            Path targetLocation = this.fileStorageLocation.resolve(user.getLogin() + "/" + fileName);
            File f = targetLocation.getParent().toFile();
            if (!f.exists()) {
                logger.info(" creating directory [{}] exists [{}]", f);
                Files.createDirectories(f.toPath());
            }
            logger.debug("directory [{}] exists [{}]", f, f.exists());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Uploaded uploaded = uploadedRepository.save(new Uploaded(targetLocation, user));
            logger.info("saved in [{}]", targetLocation.toAbsolutePath());
            addFileToArticle(id, uploaded);
            return uploaded;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private void validateParameters(MultipartFile file, User user, String fileName) throws FileStorageException {
        if(fileName == null ){
            throw new FileStorageException(String.format("can't determine filename of file [size %d]", file.getSize()));
        }
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
        if (file == null) {
            throw new FileStorageException("no files sent - is it multipart with index 'file' - please validate..." + fileName);
        }
        if (user == null) {
            throw new FileStorageException("Could not find user! " + fileName);
        }
    }

    private void addFileToArticle(String link, Uploaded uploaded) {
        if (link != null)
            articleRepositoryForUploaded.findByLink(link).ifPresent(a -> {
                a.setImage("files/one/" + uploaded.getId());
                articleRepositoryForUploaded.save(a);
            });
    }

    public Page<UploadedDTO> findByOwner(Pageable pageable) {
        return UploadedDTO.create(findByOwner(logged.getUser(), pageable));
    }

    public Optional<Article> findArticleByLink(String address) {
        return articleRepositoryForUploaded.findByLink(address);
    }

    public FilesProcessor.UploadFileResponse processPostRequest(MultipartFile file, HttpServletRequest request) throws FileStorageException {
        logger.info("processPostRequest : id = [{}], file size : [{}]", request, file.getSize());
        Uploaded uploaded = saveFile(file, logged.getUser(), resolveArticleLink(request));
        String fileDownloadUri = uploaded.getLink(environment);
        return new FilesProcessor.UploadFileResponse(uploaded.getId(), fileDownloadUri, file.getContentType(), file.getSize());
    }

    private String resolveArticleLink(HttpServletRequest request) {
        try {
            String[] r = request.getRequestURI().split("/");
            String link = r[r.length - 1];
            return link;
        } catch (Exception e) {
            return null;
        }
    }
}
