package publisaiz.functionalities.articles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import publisaiz.entities.Article;
import publisaiz.entities.User;

import java.util.Optional;

@Repository
interface ArticleRepository extends CustomArticleRepository, JpaRepository<Article, Long> {

    Page<Article> findAllByHideFalseOrHideIsNull(Pageable pageable);

    Optional<Article> findByLink(String link);

    @Query("from Article a where (a.hide = false or a.hide = null) and author = :author")
    Page<Article> findMyArticles(@Param("author") User author, Pageable pageable);

}