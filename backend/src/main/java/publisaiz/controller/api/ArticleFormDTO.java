package publisaiz.controller.api;

import java.time.ZonedDateTime;

public class ArticleFormDTO {
    private ZonedDateTime before;
    private ZonedDateTime after;
    private String author;
    private String term;

    public ArticleFormDTO() {
    }

    public ZonedDateTime getBefore() {
        return before;
    }

    public void setBefore(ZonedDateTime before) {
        this.before = before;
    }

    public ZonedDateTime getAfter() {
        return after;
    }

    public void setAfter(ZonedDateTime after) {
        this.after = after;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "ArticleFormDTO{" +
                "before=" + before +
                ", after=" + after +
                ", author='" + author + '\'' +
                ", term='" + term + '\'' +
                '}';
    }
}
