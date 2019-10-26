package publisaiz.functionalities.articles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import publisaiz.api.dto.ArticleSearchFormDTO;
import publisaiz.entities.Article;

interface CustomArticleRepository {
    Page<Article> findArticleByForm(ArticleSearchFormDTO af, Pageable pageable);
}
