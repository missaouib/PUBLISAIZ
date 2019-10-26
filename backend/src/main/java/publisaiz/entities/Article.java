/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@ApiIgnore
public class Article {

    transient private static final int TITLE_IN_URL_MAX_LENGTH = 60;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "varchar(255)")
    private String title;
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Tag> tags = new HashSet<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.REFRESH)
    private User author = new User();
    @Column(columnDefinition = "TEXT")
    private String content;
    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Comment> comments = new HashSet<>();
    @Column(unique = true)
    private String link;
    @Column(name = "featured_image")
    private String image;
    private Boolean hide = false;
    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime edited;

    public Article(User user, String title, String content) {
        author = user;
        setTitle(title);
        setContent(content);
    }

    public Article() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setLinkBasedOnTitle(title);
    }

    private void setLinkBasedOnTitle(String title) {
        if (title != null) {
            String titleInUrl = title.trim().replaceAll("[^a-zA-Z0-9\\-\\s\\.]", "_");
            titleInUrl = titleInUrl.replaceAll("[\\-| |\\.]+", "-");

            trying:
            for (int i = 0; i < 10; i++) {
                try {
                    if (titleInUrl.length() > TITLE_IN_URL_MAX_LENGTH) {
                        this.link = titleInUrl.substring(0, TITLE_IN_URL_MAX_LENGTH - 3) + "...";
                    } else {
                        this.link = titleInUrl;
                    }
                    break trying;
                } catch (Exception e) {
                    System.err.print(Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getEdited() {
        return edited;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tags=" + tags.size() +
                ", author=" + author.getLogin() +
                ", content='" + content + '\'' +
                ", comments=" + comments.size() +
                ", link='" + link + '\'' +
                ", image='" + image + '\'' +
                ", hide=" + hide +
                ", created=" + created +
                ", edited=" + edited +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(getId(), article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
