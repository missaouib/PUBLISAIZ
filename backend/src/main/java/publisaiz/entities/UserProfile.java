package publisaiz.entities;

import com.google.common.base.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "link", columnNames = {"link"}))
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@ApiIgnore
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    private Boolean enabled = true;
    private Boolean personal = false;
    @OneToOne
    private Company company;
    private String description;
    private String brand;
    private String link;
    @OneToOne
    private Uploaded profiePhoto;
    @OneToOne
    private Uploaded backgroundPhoto;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "files_profiles")
    private Set<Uploaded> files;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "followers")
    private Set<UserProfile> followers;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "following")
    private Set<UserProfile> following;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "friends")
    private Set<UserProfile> friends;
    @Version
    private
    long version;

    public UserProfile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getPersonal() {
        return personal;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Uploaded getProfiePhoto() {
        return profiePhoto;
    }

    public void setProfiePhoto(Uploaded profiePhoto) {
        this.profiePhoto = profiePhoto;
    }

    public Uploaded getBackgroundPhoto() {
        return backgroundPhoto;
    }

    public void setBackgroundPhoto(Uploaded backgroundPhoto) {
        this.backgroundPhoto = backgroundPhoto;
    }

    public Set<Uploaded> getFiles() {
        return files;
    }

    public void setFiles(Set<Uploaded> files) {
        this.files = files;
    }

    public Set<UserProfile> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<UserProfile> followers) {
        this.followers = followers;
    }

    public Set<UserProfile> getFollowing() {
        return following;
    }

    public void setFollowing(Set<UserProfile> following) {
        this.following = following;
    }

    public Set<UserProfile> getFriends() {
        return friends;
    }

    public void setFriends(Set<UserProfile> friends) {
        this.friends = friends;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
