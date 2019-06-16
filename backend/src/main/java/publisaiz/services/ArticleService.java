/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publisaiz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import publisaiz.config.Logged;
import publisaiz.controller.api.ArticleFormDTO;
import publisaiz.controller.api.dto.ArticleDTO;
import publisaiz.datasources.database.entities.Article;
import publisaiz.datasources.database.entities.User;
import publisaiz.datasources.database.repository.ArticleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
    private ArticleRepository articleRepository;
    private Logged logged;
    @PersistenceContext
    EntityManager entityManager;

    public ArticleService(ArticleRepository articleRepository, Logged logged) {
        this.articleRepository = articleRepository;
        this.logged = logged;
    }

    public Article save(Article a) {
        return articleRepository.save(a);
    }

    public ResponseEntity<Page<ArticleDTO>> getArticles(Pageable pageable) {
        return ResponseEntity.ok(ArticleDTO.convert( articleRepository.findAll(pageable)));
    }

    public ResponseEntity<?> getResponseWithArticle(String link) {
        return ResponseEntity.ok(new ArticleDTO(articleRepository.findArticleByLink(link).orElseGet(Article::new)));
    }

    public Optional<Article> getArticle(String link) {
        return articleRepository.findArticleByLink(link);
    }

    public Page<Article> getArticlesByAuthor(User author, Pageable pageable) {
        return articleRepository.findArticlesByAuthor(author, pageable);
    }

    public ResponseEntity<?> editArticle(ArticleDTO articleDTO) {
        logger.debug("editArticle(ArticleDTO articleDTO) [{}]", articleDTO);
        Article article = articleDTO.toEntity(this, logged);
        if (isAuthorisedToEditArticle(article))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(new ArticleDTO(save(article)));
    }

    private boolean isAuthorisedToEditArticle(Article article) {
        logger.debug("trying to create article [{}] with user [{}]", article, logged.getUser());
        return logged.getUser() == null || logged.getUser().getLogin() != article.getAuthor().getLogin();
    }


    public Optional<Article> getArticle(Long id) {
        if (id == null)
            return Optional.empty();
        return articleRepository.findById(id);
    }

    public ResponseEntity<?> getMyArticles(Pageable pageable) {
        return ResponseEntity.ok( ArticleDTO.convert(getArticlesByAuthor(logged.getUser(), pageable)));
    }

    public ResponseEntity<List<ArticleDTO>> getByForm(ArticleFormDTO form, Pageable pageable) {
        Iterable<Article> articles = articleRepository.findByForm(entityManager, form, pageable);
        articles.forEach(article -> this.logger.info("found article [{}]", article));
        return ResponseEntity.ok(ArticleDTO.convert(articles));
    }
}
