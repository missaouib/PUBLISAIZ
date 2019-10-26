package publisaiz.functionalities.articles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import publisaiz.api.dto.ArticleDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.Article;

import java.util.Objects;

class Articledto2Article {
    private final static Logger logger = LoggerFactory.getLogger(Articledto2Article.class);

    static Article articleDTOtoEntity(ArticleDTO articleDTO, ArticleService articleService, Logged logged) {
        logger.info("ArticleDTO articleDTO: [{}], ArticleService articleService: [{}]: [{}]",
                articleDTO,
                articleService,
                logged);

        Article article;
        article = articleService.getArticle(articleDTO.getLink())
                .orElse(articleService.getArticle(articleDTO.getId())
                        .orElse(new Article(logged.getUser(), articleDTO.getTitle(), articleDTO.getContent())));
        logger.info("found: [{}]", article);
        bindArticle(articleDTO, article, logged);
        logger.info("binded: [{}] with user [{}]", article, logged);
        return article;
    }

    static private void bindArticle(ArticleDTO articleDTO, Article article, Logged logged) {
        if (article.getAuthor() == null
                || logged.getUser() != null
                || Objects.requireNonNull(logged.getUser()).getLogin().equals(article.getAuthor().getLogin())) {
            article.setAuthor(logged.getUser());
        }
        article.setImage(articleDTO.getImage());
        article.setContent(articleDTO.getContent());
        article.setTitle(articleDTO.getTitle());
        article.setHide(articleDTO.getHide());
        article.setAuthor(logged.getUser());
    }
}