package publisaiz.functionalities.articles;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import publisaiz.api.dto.ArticleDTO;
import publisaiz.api.dto.ArticleSearchFormDTO;
import publisaiz.config.swagger.ApiPageable;

@RestController
@RequestMapping("api/articles")
class ArticlesController {

    private final ArticleService articleService;
    private final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    public ArticlesController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("all")
    @ApiPageable
    @ApiOperation(value = "Fetches articles")
    public Page<ArticleDTO> getAll(
            @PageableDefault(sort = "created", direction = Sort.Direction.DESC, size = 3)
                    Pageable pageable) {
        logger.debug("pageable: [{}]", pageable);
        Page<ArticleDTO> articles = articleService.getArticles(pageable);
        logger.debug("articles: [{}]", articles);
        return articles;
    }

    @GetMapping("my")
    @ApiPageable
    @ApiOperation(value = "Fetches logged user articles")
    public Page<ArticleDTO> getAllmy(
            @SortDefault(sort = "created", direction = Sort.Direction.DESC)
            @PageableDefault(size = 3) Pageable pageable) {
        logger.debug("getAllmy: [{}]", pageable);
        return articleService.getMyArticles(pageable);
    }

    @GetMapping("article/{link}")
    @ApiOperation(value = "Fetches articles by link")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable String link) {
        logger.debug("getArticle: [{}]", link);
        ResponseEntity<ArticleDTO> responseWithArticle = articleService.getResponseWithArticle(link);
        logger.debug("response", responseWithArticle);
        return responseWithArticle;
    }

    @PostMapping
    @ApiOperation(value = "Creating or editing article")
    public ResponseEntity<ArticleDTO> editArticle(@RequestBody ArticleDTO entity) {
        logger.debug("editArticle: [{}]", entity);
        return articleService.editArticle(entity);
    }

    @PostMapping("form")
    @ApiOperation(value = "Fetches articles by form")
    public Page<ArticleDTO> getByForm(
            @RequestBody ArticleSearchFormDTO articleSearchFormDTO,
            @PageableDefault(size = 3) Pageable pageable) {
        logger.info("articleSearchFormDTO: [{}]", articleSearchFormDTO);
        return articleService.getByForm(articleSearchFormDTO, pageable);
    }

    @DeleteMapping("article/{link}")
    @ApiOperation(value = "Delete article")
    public ResponseEntity<Boolean> deleteArticle(@PathVariable String link) {
        logger.debug("deleteArticle: [{}]", link);
        Boolean res = articleService.delete(link);
        logger.debug("response [{}]", res);
        if (res) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }
}