package publisaiz.functionalities.uploaded;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publisaiz.entities.Article;

import java.util.Optional;

@Repository
interface ArticleRepositoryForUploaded extends JpaRepository<Article, Long> {

    Optional<Article> findByLink(String link);

}