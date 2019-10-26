package publisaiz.api.dto;

import org.springframework.data.domain.Page;
import publisaiz.entities.Uploaded;

public class UploadedDTO {

    private String url;
    private Long id;

    public UploadedDTO(Long id) {
        this.url = "files/one/" + id;
        this.id = id;
    }

    public static Page<UploadedDTO> create(Page<Uploaded> byOwner) {
        return byOwner.map(f -> new UploadedDTO(f.getId()));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
