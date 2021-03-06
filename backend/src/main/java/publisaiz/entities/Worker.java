package publisaiz.entities;

import org.hibernate.annotations.Cache;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;

import static org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL;

@Entity
@Cacheable
@Cache(usage = TRANSACTIONAL)
@ApiIgnore
class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String companyRole;

    public Worker() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompanyRole() {
        return companyRole;
    }

    public void setCompanyRole(String companyRole) {
        this.companyRole = companyRole;
    }
}
