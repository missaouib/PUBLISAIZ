package publisaiz.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Cacheable
@ApiIgnore
public class Tag {

    @Id()
    @NaturalId
    private String value;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Article> articles = new HashSet<>();

    public Tag(String t) {
        value = t;
    }

    public Tag() {
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) o;
        return Objects.equals(value, tag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}