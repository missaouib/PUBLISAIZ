package publisaiz.datasources.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import publisaiz.controller.api.ArticleFormDTO;
import publisaiz.datasources.database.entities.Article;
import publisaiz.datasources.database.entities.User;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAll(Pageable pageable);

    Optional<Article> findArticleByLink(String link);

    Page<Article> findArticlesByAuthor(User author, Pageable pageable);

    default Page<Article> findByForm(EntityManager em, ArticleFormDTO af, Pageable pageable){
        String hql = "select a from Article a ";
        String filter = "";
        if(af.getBefore()!=null) filter += " and a.created < :before ";
        if(af.getAfter()!=null) filter += " and a.created > :after ";
        if(af.getAuthor()!=null&& af.getAuthor().length() > 0) filter += " and (a.author.login like :author or a.author.name like :author or a.author.surname like :author) ";
        if(af.getTerm()!=null&& af.getTerm().length() > 0) filter += " and (a.title like :term or a.content like :term) ";
        if(filter!="") filter = " where " + filter.strip().substring(3);
        System.out.println(hql + filter);
        TypedQuery<Article> query = em.createQuery(hql + filter, Article.class);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        if(af.getBefore()!=null) query.setParameter("before", af.getBefore());
        if(af.getAfter()!=null) query.setParameter("after", af.getAfter());
        if(af.getAuthor()!=null && af.getAuthor().length() > 0) query.setParameter("author", "%"+af.getAuthor()+"%");
        if(af.getTerm()!=null && af.getTerm().length() > 0) query.setParameter("term", "%"+af.getTerm()+"%");
        var resultList = query.getResultList();
        return new PageImpl<>(resultList);
    }

}
