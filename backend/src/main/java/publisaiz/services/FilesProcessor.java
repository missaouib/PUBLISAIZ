package publisaiz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import publisaiz.config.Logged;
import publisaiz.datasources.database.entities.Role;
import publisaiz.datasources.database.entities.Uploaded;

import javax.security.sasl.AuthenticationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BinaryOperator;

@Service
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FilesProcessor {

    private final static Logger logger = LoggerFactory.getLogger(FilesProcessor.class);
    private final Logged logged;

    public FilesProcessor(Logged logged) {
        this.logged = logged;
    }


    public BinaryOperator<String> getBinaryOperator() {
        return (a, b) -> (b != null && b.length() > 0) ? b : a;
    }

    public FileInputStream formResponse(Optional<Uploaded> uploaded) throws IOException {
        logger.debug("formResponse [{}]", uploaded);
        if (hasGotRightsForFile(uploaded)) {
            logger.debug("uploaded [{}]", uploaded);
            String fileStorage = getStorage(uploaded);
            logger.debug("fileStorage [{}]", fileStorage);
            FileInputStream fileStream = new FileInputStream(fileStorage);
            logger.debug("fileStream [{}]", fileStream);
            Optional<String> format = Arrays.stream(fileStorage.split(".")).reduce(getBinaryOperator());
            logger.debug("format [{}]", format);
            final HttpHeaders headers = new HttpHeaders();
            logger.debug("headers [{}]", headers);
            if (format.isPresent()) {
                headers.setContentType(MediaType.valueOf(format.get().toLowerCase()));
                logger.debug("format.isPresent [{}]", headers);
            }
            return fileStream;
        } else throw new AuthenticationException("You do not have rights for this file!");
    }

    public boolean hasGotRightsForFile(Optional<Uploaded> uploaded) {
        logger.debug("hasGotRightsForFile uploaded: [{}]", uploaded);
        boolean hasRights = isFile(uploaded) && (isNotPrivate(uploaded)
                || (isLogged() && (isAdmin() || isOwner(uploaded))));
        logger.info("hasGotRightsForFile hasRights: [{}]", hasRights);
        return hasRights;
    }

    private boolean isLogged() {
        boolean r = logged.getUser() != null;
        logger.info("hasGotRightsForFile : ", r);
        return r;
    }

    private boolean isFile(Optional<Uploaded> uploaded) {
        boolean r = uploaded.isPresent() && uploaded.get().getOwner() != null;
        logger.info("isFile : ", r);
        return r;
    }

    private boolean isNotPrivate(Optional<Uploaded> uploaded) {
        logger.debug("isNotPrivate [{}]", uploaded);
        if (uploaded.isEmpty())
            return false;
        if (uploaded.get().getPriv() == null)
            return true;
        boolean r = uploaded.get().getPriv().booleanValue() != true ||
                !uploaded.get().getPriv();
        logger.info("hasGotRightsForFile : ", r);
        return r;
    }

    private boolean isOwner(Optional<Uploaded> uploaded) {
        boolean r = uploaded.get().getOwner().getLogin() == logged.getUser().getLogin();
        logger.info("hasGotRightsForFile : ", r);
        return r;
    }

    private boolean isAdmin() {
        boolean r = logged.getUser().getRoles().contains(new Role("admin"));
        logger.info("hasGotRightsForFile : ", r);
        return r;
    }

    public String getStorage(Optional<Uploaded> uploaded) {
        String n;
        return (n = uploaded.get().getFileStorage()).isEmpty() ? uploaded.get().getFileName() : n;
    }

    public static class UploadFileResponse {
        private String fileId;
        private String imageUrl;
        private String fileType;
        private long size;

        public UploadFileResponse(Long fileId, String imageUrl, String fileType, long size) {
            this.fileId = fileId.toString();
            this.imageUrl = imageUrl;
            this.fileType = fileType;
            this.size = size;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}