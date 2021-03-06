package publisaiz.entities;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@ApiIgnore
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String registeredNumber;
    private String vatNumber;
    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id")
    private Country country;
    @OneToMany(targetEntity = Worker.class, mappedBy = "company")
    private Set<Worker> workers;

    public Company() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisteredNumber() {
        return registeredNumber;
    }

    public void setRegisteredNumber(String registeredNumber) {
        this.registeredNumber = registeredNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }
}
