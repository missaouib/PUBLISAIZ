package publisaiz.utils.repository.custom.formbased;

import javax.persistence.TypedQuery;

public interface CustomRepositoryForm {

    void setParameters(TypedQuery query, TypedQuery countQuery);

    String setFilters();
}
