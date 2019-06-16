package publisaiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import publisaiz.controller.api.ArticleFormDTO;
import publisaiz.controller.api.dto.ArticleDTO;
import publisaiz.services.ArticleService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("api/articles")
@CrossOrigin(origins = {"http://localhost:4200", "http://publisaiz"}, maxAge=3600,
        allowCredentials = "true", methods = {
        POST, GET, PATCH, PUT, DELETE, OPTIONS
})
public class ArticlesController {

    private final ArticleService articleService;
    private final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    public ArticlesController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("all")
    public ResponseEntity<?> getAll(@PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.debug("pageable: [{}]", pageable);
        ResponseEntity<?> articles = articleService.getArticles(pageable);
        logger.debug("articles: [{}]", articles);
        return articles;
    }

    @GetMapping("my")
    public ResponseEntity<?> getAllmy(@SortDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.debug("getAllmy: [{}]", pageable);
        return articleService.getMyArticles(pageable);
    }

    @GetMapping("article/{link}")
    public ResponseEntity<?> getArticle(@PathVariable String link) {
        logger.debug("getArticle: [{}]", link);
        ResponseEntity<?> responseWithArticle = articleService.getResponseWithArticle(link);
        logger.debug("response", responseWithArticle);
        return responseWithArticle;
    }
    @PostMapping
    public ResponseEntity<?> editArticle(@RequestBody ArticleDTO entity) {
        logger.debug("editArticle: [{}]", entity);
        return articleService.editArticle(entity);
    }

    @GetMapping("form")
    public ResponseEntity<List<ArticleDTO>> getByForm(ArticleFormDTO articleFormDTO, Pageable pageable){
        logger.info("articleFormDTO: [{}]", articleFormDTO);
        return articleService.getByForm(articleFormDTO, pageable);
    }
}