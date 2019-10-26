/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Cacheable
@ApiIgnore
public class Comment {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(columnDefinition = "text")
    private String content;
    @ManyToOne
    private User author;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ZonedDateTime date;
    @OneToMany
    private List<Comment> comments;
    private Boolean hidden = false;

    public Comment() {
    }

    public Comment(Long id, List<Comment> comments, User author, ZonedDateTime date, String content) {
        this.id = id;
        this.content = content;
        this.comments = comments;
        this.author = author;
        this.date = date;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
