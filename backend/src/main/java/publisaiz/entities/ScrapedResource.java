package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.lang.NonNull;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "url"))
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@ApiIgnore
public class ScrapedResource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @Column(length = 500)
    private String url;

    @JoinTable(name = "websites_relations")
    @ManyToMany
    private Set<ScrapedResource> connections = new HashSet<>();

    @JoinTable(name = "websites_emails")
    @ManyToMany
    private Set<ScrapedEmail> emails = new HashSet<>();

    @Column(length = 100000)
    private String content;

    private ZonedDateTime accessDate;

    public ScrapedResource() {
    }

    public ScrapedResource(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<ScrapedResource> getConnections() {
        return connections;
    }

    public void setConnections(Set<ScrapedResource> connections) {
        this.connections = connections;
    }

    public Set<ScrapedEmail> getEmails() {
        return emails;
    }

    public void setEmails(Set<ScrapedEmail> emails) {
        this.emails = emails;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content.substring(0, content.length() > 10000 ? 9999 : content.length());
    }

    public ZonedDateTime getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(ZonedDateTime accessDate) {
        this.accessDate = accessDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScrapedResource scraped = (ScrapedResource) o;
        return url.equals(scraped.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "ScrapedResource{" +
                "url='" + url + '\'' +
                ", connections=" + connections +
                ", emails=" + emails +
                ", content='" + content + '\'' +
                '}';
    }
}
