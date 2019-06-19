package publisaiz.datasources.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publisaiz.datasources.database.entities.Article;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.custom.CustomArticleRepository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends CustomArticleRepository, JpaRepository<Article, Long> {

    Page<Article> findAll(Pageable pageable);

    Optional<Article> findArticleByLink(String link);

    Page<Article> findArticlesByAuthor(User author, Pageable pageable);

}
