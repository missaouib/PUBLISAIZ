package publisaiz.datasources.database.repository.custom.formbasedcustomrepository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import publisaiz.datasources.database.repository.custom.CustomArticleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class CustomRepositoryAbs<V, K> implements CustomArticleRepository {

    public static final String ORDER_BY_A_CREATED_DESC = " order by a.created desc";
    public static final String SELECT_COUNT = "select count(*) ";
    public static String FROM;
    private final Class<K> keyParameterClass;
    private final Class<V> typeParameterClass;
    @PersistenceContext
    EntityManager em;

    public CustomRepositoryAbs(Class<V> valueParameterClass, Class<K> keyParameterClass) {
        this.typeParameterClass = valueParameterClass;
        this.keyParameterClass = keyParameterClass;
        FROM = "from " + valueParameterClass.getName() + " a ";
    }

    public Page<V> findByForm(CustomRepositoryForm af, Pageable pageable) {
        String filter = setFilters(af);
        //TODO: enable sorting and ordering via Pageable
        //Sort sorting = pageable.getSortOr(Sort.by(Sort.Order.desc("a.created")));
        TypedQuery<V> query = em.createQuery(FROM
                .concat(filter)
                .concat(ORDER_BY_A_CREATED_DESC), typeParameterClass);
        TypedQuery<Long> countQuery = em.createQuery(SELECT_COUNT
                .concat(FROM)
                .concat(filter), Long.class);
        setPages(pageable, query);
        setParameters(af, query, countQuery);
        PageImpl<V> res = getResultPage(pageable, query, countQuery);
        return res;
    }

    @NotNull
    private PageImpl<V> getResultPage(Pageable pageable, TypedQuery<V> query, TypedQuery<Long> countQuery) {
        var resultList = query.getResultList();
        Long count = countQuery.getSingleResult();
        return new PageImpl<>(resultList, pageable, count);
    }

    private void setPages(Pageable pageable, TypedQuery<?> query) {
        int ps = pageable.getPageSize();
        int first = pageable.getPageNumber() * ps;
        first = first > 0 ? first : 0;
        query.setFirstResult(first);
        int pageSize = ps > 0 && ps < pageable.getPageSize() ? ps : pageable.getPageSize();
        query.setMaxResults(pageSize);
    }

    private void setParameters(CustomRepositoryForm af, TypedQuery<V> query, TypedQuery<Long> countQuery) {
        af.setParameters(query, countQuery);
    }

    @NotNull
    private String setFilters(CustomRepositoryForm af) {
        String filter = af.setFilters();
        return filter;
    }
}
