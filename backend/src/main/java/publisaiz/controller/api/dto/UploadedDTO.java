package publisaiz.controller.api.dto;

import org.springframework.data.domain.Page;
import publisaiz.datasources.database.entities.Uploaded;

public class UploadedDTO {

    private String url;

    public UploadedDTO(String url) {
        this.url = url;
    }

    public static Page<UploadedDTO> create(Page<Uploaded> byOwner) {
        return byOwner.map(f -> new UploadedDTO("files/one/" + f.getId()));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
