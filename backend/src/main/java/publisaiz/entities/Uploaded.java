package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.core.env.Environment;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Uploaded {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String fileName;
    private String fileStorage;
    @ManyToOne
    private User owner;
    private String cathegory;
    private String title;
    private String description;
    private Boolean hide = false;
    private Boolean priv = false;
    private Boolean loggedOnly;
    @ManyToMany
    private Set<UserProfile> profileAssigned;

    public Uploaded() {
    }

    public Uploaded(Path targetLocation, User user) {
        fileName = targetLocation.getFileName().toString();
        fileStorage = targetLocation.toString();
        owner = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String filename) {
        this.fileName = filename;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getCathegory() {
        return cathegory;
    }

    public void setCathegory(String cathegory) {
        this.cathegory = cathegory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Boolean getPriv() {
        return priv;
    }

    public void setPriv(Boolean priv) {
        this.priv = priv;
    }

    public Boolean getLoggedOnly() {
        return loggedOnly;
    }

    public void setLoggedOnly(Boolean loggedOnly) {
        this.loggedOnly = loggedOnly;
    }

    public String getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(String fileStorage) {
        this.fileStorage = fileStorage;
    }

    public String getLink(Environment environment) {
        return Objects.requireNonNull(environment.getProperty("publisaiz.api"))
                .concat("files")
                .concat("/one/")
                .concat(id.toString());
    }

    @Override
    public String toString() {
        return "Uploaded{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileStorage='" + fileStorage + '\'' +
                ", cathegory='" + cathegory + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", hide=" + hide +
                ", priv=" + priv +
                ", loggedOnly=" + loggedOnly +
                '}';
    }
}
