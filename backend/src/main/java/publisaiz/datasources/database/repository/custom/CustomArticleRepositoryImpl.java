package publisaiz.datasources.database.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import publisaiz.controller.api.ArticleFormDTO;
import publisaiz.datasources.database.entities.Article;
import publisaiz.datasources.database.repository.custom.formbasedcustomrepository.CustomRepositoryAbs;

public class CustomArticleRepositoryImpl extends CustomRepositoryAbs<Article, Long> {

    public CustomArticleRepositoryImpl() {
        super(Article.class, Long.class);
    }

    @Override
    public Page<Article> findArticleByForm(ArticleFormDTO af, Pageable pageable) {
        return super.findByForm(af, pageable);
    }
}
