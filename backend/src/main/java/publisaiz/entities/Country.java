package publisaiz.entities;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@ApiIgnore
public class Country {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "country")
    private Set<Company> companies;

    public Country() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }
}
