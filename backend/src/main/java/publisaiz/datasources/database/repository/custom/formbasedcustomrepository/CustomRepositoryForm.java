package publisaiz.datasources.database.repository.custom.formbasedcustomrepository;

import javax.persistence.TypedQuery;

public interface CustomRepositoryForm {
    void setParameters(TypedQuery query, TypedQuery countQuery);

    String setFilters();
}
