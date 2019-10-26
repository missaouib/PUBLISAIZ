package publisaiz.api.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import publisaiz.entities.Article;

import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticleDTO {
    private Long id;
    @Size(max = 200, min = 5)
    private String title;
    private Set<String> tags;
    private String author;
    private String content;
    private Set<ArticleCommentDto> comments;
    private String link;
    private String image;
    private Boolean hide;
    private String created;
    private String edited;

    private ArticleDTO() {
    }

    public ArticleDTO(Article article) {
        id = article.getId();
        title = article.getTitle();
        content = article.getContent();
        author = article.getAuthor().getLogin();
        link = article.getLink();
        image = article.getImage();
        if (article.getCreated() != null)
            created = article.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (article.getEdited() != null)
            edited = article.getEdited().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (article.getTags() != null && !article.getTags().isEmpty()) {
            tags = article.getTags().stream().map(t -> t.getValue()).collect(Collectors.toSet());
        }
        if (article.getComments() != null)
            comments = article.getComments().stream().map(c -> new ArticleCommentDto(c)).collect(Collectors.toSet());
        Logger logger = LoggerFactory.getLogger(ArticleDTO.class);
        logger.debug("articleDTO: ", toString());
        logger.debug("article: ", article);
    }

    public static Page<ArticleDTO> convertPage(Page<Article> articles) {
        return articles.map(c -> new ArticleDTO(c));
    }

    public static List<ArticleDTO> convertPage(Iterable<Article> articles) {
        List<ArticleDTO> res = new ArrayList<>();
        articles.iterator().forEachRemaining(a -> res.add(new ArticleDTO(a)));
        return res;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<ArticleCommentDto> getComment() {
        return this.comments;
    }

    public void setComment(Set<ArticleCommentDto> comment) {
        this.comments = comment;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean isHide() {
        return this.hide;
    }

    public Boolean getHide() {
        return this.hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEdited() {
        return this.edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
