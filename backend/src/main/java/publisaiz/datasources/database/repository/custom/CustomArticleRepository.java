package publisaiz.datasources.database.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import publisaiz.controller.api.ArticleFormDTO;
import publisaiz.datasources.database.entities.Article;

public interface CustomArticleRepository {
    Page<Article> findArticleByForm(ArticleFormDTO af, Pageable pageable);
}
