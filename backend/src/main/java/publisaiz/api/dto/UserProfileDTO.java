package publisaiz.api.dto;

import org.springframework.core.env.Environment;
import publisaiz.entities.UserProfile;

import java.util.List;

public class UserProfileDTO {
    private Long id;
    private Boolean enabled;
    private Boolean personal;
    private CompanyDTO company;
    private String description;
    private String brand;
    private String link;
    private String profilePhoto;
    private String backgroundPhoto;
    private List<String> files;
    private List<UserProfileDTO> followers;
    private List<UserProfileDTO> following;
    private List<UserProfileDTO> friends;


    public UserProfileDTO() {
    }

    public UserProfileDTO(UserProfile userProfile, Environment environment) {
        setId(userProfile.getId());
        setEnabled(userProfile.getEnabled());
        setDescription(userProfile.getDescription());
        if (userProfile.getBackgroundPhoto() != null)
            setBackgroundPhoto(userProfile.getBackgroundPhoto().getLink(environment));
        if (userProfile.getProfiePhoto() != null)
            setProfilePhoto(userProfile.getProfiePhoto().getLink(environment));
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    private void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getPersonal() {
        return personal;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    private void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getBackgroundPhoto() {
        return backgroundPhoto;
    }

    private void setBackgroundPhoto(String backgroundPhoto) {
        this.backgroundPhoto = backgroundPhoto;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<UserProfileDTO> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserProfileDTO> followers) {
        this.followers = followers;
    }

    public List<UserProfileDTO> getFollowing() {
        return following;
    }

    public void setFollowing(List<UserProfileDTO> following) {
        this.following = following;
    }

    public List<UserProfileDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<UserProfileDTO> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "UserProfileDTO{" +
                "id=" + id +
                ", enabled=" + enabled +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", backgroundPhoto='" + backgroundPhoto + '\'' +
                ", personal=" + personal +
                ", company=" + company +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
