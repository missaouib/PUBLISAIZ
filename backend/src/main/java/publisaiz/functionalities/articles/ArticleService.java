/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publisaiz.functionalities.articles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import publisaiz.api.dto.ArticleDTO;
import publisaiz.api.dto.ArticleSearchFormDTO;
import publisaiz.config.logged.Logged;
import publisaiz.entities.Article;
import publisaiz.entities.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
    private final ArticleRepository articleRepository;
    private final Logged logged;

    public ArticleService(ArticleRepository articleRepository, Logged logged) {
        this.articleRepository = articleRepository;
        this.logged = logged;
    }

    private Article save(Article a) {
        return articleRepository.save(a);
    }

    public Page<ArticleDTO> getArticles(Pageable pageable) {
        return ArticleDTO.convertPage(articleRepository.findAllByHideFalseOrHideIsNull(pageable));
    }

    public ResponseEntity<ArticleDTO> getResponseWithArticle(String link) {
        return ResponseEntity.ok(new ArticleDTO(articleRepository.findByLink(link).filter(a -> isVisible(a)).orElseGet(Article::new)));
    }

    public Optional<Article> getArticle(String link) {
        return articleRepository.findByLink(link);
    }

    private Page<Article> getArticlesByAuthor(User author, Pageable pageable) {
        return articleRepository.findMyArticles(author, pageable);
    }

    public ResponseEntity<ArticleDTO> editArticle(ArticleDTO articleDTO) {
        logger.debug("editArticle(ArticleDTO articleDTO) [{}]", articleDTO);
        Article article = Articledto2Article.articleDTOtoEntity(articleDTO, this, logged);
        if (isAuthorisedToEditArticle(article))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(new ArticleDTO(save(article)));
    }

    private boolean isAuthorisedToEditArticle(Article article) {
        logger.debug("trying to create article [{}] with user [{}]", article, logged.getUser());
        return logged.getUser() == null || !logged.getUser().getLogin().equals(article.getAuthor().getLogin());
    }


    public Optional<Article> getArticle(Long id) {
        if (id == null)
            return Optional.empty();
        return articleRepository.findById(id);
    }

    public Page<ArticleDTO> getMyArticles(Pageable pageable) {
        return ArticleDTO.convertPage(getArticlesByAuthor(logged.getUser(), pageable));
    }

    public Page<ArticleDTO> getByForm(ArticleSearchFormDTO form, Pageable pageable) {
        Page<Article> articles = articleRepository.findArticleByForm(form, pageable);
        return ArticleDTO.convertPage(articles);
    }

    public Boolean delete(String link) {
        var res = articleRepository.findByLink(link).filter(a -> isVisible(a));
        res.ifPresent(a -> setAsHiddenAndSave(a));
        return res.isPresent();
    }

    private boolean isVisible(Article a) {
        return a.getHide() == null || !a.getHide();
    }

    private Article setAsHiddenAndSave(Article a) {
        a.setHide(true);
        return articleRepository.save(a);
    }
}
