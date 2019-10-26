package publisaiz.functionalities.users;

import publisaiz.api.dto.UserProfileDTO;
import publisaiz.entities.UserProfile;

class Userprofiedto2Userprofile {

    public Userprofiedto2Userprofile() {
    }

    static public UserProfile userProfileDTO2UserProfile(UserProfileDTO profileDTO, UserService userService) {
        UserProfile profile;
        if (profileDTO.getId() != null)
            profile = userService.findProfileById(profileDTO.getId()).orElseGet(UserProfile::new);
        else profile = new UserProfile();
        if (profileDTO.getBackgroundPhoto() != null)
            userService.findUploadedByFileURL(profileDTO.getBackgroundPhoto())
                    .ifPresent(a -> profile.setBackgroundPhoto(a));
        if (profileDTO.getProfilePhoto() != null)
            userService.findUploadedByFileURL(profileDTO.getProfilePhoto())
                    .ifPresent(a -> profile.setProfiePhoto(a));
        if (profileDTO.getDescription() != null)
            profile.setDescription(profileDTO.getDescription());
        profile.setEnabled(profileDTO.getEnabled());
        return profile;
    }
}