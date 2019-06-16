package publisaiz.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import publisaiz.controller.api.ArticleFormDTO;
import publisaiz.controller.api.dto.ArticleDTO;
import publisaiz.datasources.database.entities.Article;
import publisaiz.datasources.database.entities.User;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;

    @Before
    public void prepare() {
        Article a1 = new Article();
        a1.setContent("something1");
        a1.setTitle("something1");
        assert a1.getLink().length() > 0;
        Article a2 = new Article();
        a2.setContent("something2");
        a2.setTitle("something2");
        Article a3 = new Article();
        a3.setContent("something3");
        a3.setTitle("something3");
        Article a4 = new Article();
        a4.setContent("something4");
        a4.setTitle("something4");
        User user = new User();
        user.setLogin("test@test.pl");
        user.setPassword("testtesttest");
        userService.saveWithPasswordEncoding(user);
        a1.setAuthor(user);
        a2.setAuthor(user);
        a3.setAuthor(user);
        a4.setAuthor(user);
        articleService.save(a1);
        articleService.save(a2);
        articleService.save(a3);
        articleService.save(a4);
        Pageable pageable = PageRequest.of(0, 6);
        ResponseEntity<Page<ArticleDTO>> res = articleService.getArticles(pageable);
        assert res.getBody().getTotalElements() == 4;
    }

    @Test
    @DirtiesContext
    public void getByFormWithoutFilteringWithPagination() {
        //given
        ArticleFormDTO form = new ArticleFormDTO();
        Pageable pageable1 = PageRequest.of(0, 2);
        Pageable pageable2 = PageRequest.of(1, 2);
        //when
        ResponseEntity<List<ArticleDTO>> res1 = articleService.getByForm(form, pageable1);
        ResponseEntity<List<ArticleDTO>> res2 = articleService.getByForm(form, pageable2);
        //then
        assert res1.getBody().size() == 2;
        assert res2.getBody().size() == 2;
        assert res1.getBody().get(0).getTitle() == "something1";
        assert res1.getBody().get(1).getTitle() == "something2";
        assert res2.getBody().get(0).getTitle() == "something3";
        assert res2.getBody().get(1).getTitle() == "something4";
    }

    @Test
    @DirtiesContext
    public void getByFormWithFiltering() {
        //given
        ArticleFormDTO form1 = new ArticleFormDTO();
        form1.setTerm("something1");
        ArticleFormDTO form2 = new ArticleFormDTO();
        form2.setTerm("something2");
        Pageable pageable = PageRequest.of(0, 8);
        //when
        ResponseEntity<List<ArticleDTO>> res2 = articleService.getByForm(form2, pageable);
        ResponseEntity<List<ArticleDTO>> res1 = articleService.getByForm(form1, pageable);
        //then
        assert res1.getBody().size() == 1;
        assert res2.getBody().size() == 1;
        assert res1.getBody().get(0).getTitle() == "something1";
        assert res2.getBody().get(0).getTitle() == "something2";
    }

}