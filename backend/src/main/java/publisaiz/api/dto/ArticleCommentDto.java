package publisaiz.api.dto;

import publisaiz.entities.Comment;

import java.time.ZonedDateTime;

class ArticleCommentDto {

    private Long id;
    private ZonedDateTime date;
    private Integer author;
    private String content;

    public ArticleCommentDto() {
    }

    public ArticleCommentDto(Comment c) {
        content = c.getContent();
        author = c.getAuthor().getId();
        date = c.getDate();
        id = c.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
