package publisaiz.functionalities.articles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import publisaiz.api.dto.ArticleSearchFormDTO;
import publisaiz.entities.Article;
import publisaiz.utils.repository.custom.formbased.CustomRepositoryAbs;

class CustomArticleRepositoryImpl extends CustomRepositoryAbs<Article, Long> implements CustomArticleRepository {

    public CustomArticleRepositoryImpl() {
        super(Article.class, Long.class);
    }

    @Override
    public Page<Article> findArticleByForm(ArticleSearchFormDTO af, Pageable pageable) {
        return super.findByForm(af, pageable);
    }
}
