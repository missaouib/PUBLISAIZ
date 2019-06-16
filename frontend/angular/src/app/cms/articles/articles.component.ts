import { Component, OnInit, ViewChild } from '@angular/core';
import { ArticleModel } from 'src/app/_model/article.model';
import { Router, ActivatedRoute } from '@angular/router';
import { ArticlesService as ArticlesService } from 'src/app/_services/articles.service';
import { ArticleService } from 'src/app/_services/article.service';
import { ArticleComponent } from './article/article.component';
import { environment } from '../../../environments/environment';
import { FilesService } from 'src/app/_services/files.service';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {
  @ViewChild('articleComponent')
  articleComponent: ArticleComponent;
  logged = {};
  articles = <ArticleModel[]>[];
  page = 0;
  link: string;
  article: ArticleModel;
  images: any;
  environment: { production: boolean; apiUrl: string; };

  constructor(private router: Router,
    private articlesService: ArticlesService,
    private articleService: ArticleService,
    private route: ActivatedRoute,
    private logger: NGXLogger,
    private filesService: FilesService) { }

  ngOnInit() {
    this.environment = environment;
    if (!this.logged) {
      this.router.navigateByUrl('login');
    }
    this.refreshAll();
  }

  updateArticle(image) {
    this.article.image = image;
    this.articleService.postArticle(this.article).subscribe(r => this.article = r.content);
    this.refreshAll();
  }

  onEvent(event) {
    this.refreshAll();
  }

  private refreshAll() {
    this.logger.info('refreshAll');
    this.loadArticle(this.route.snapshot.params.link);
    this.refreshArticles();
    this.getImages();
  }

  private refreshArticles() {
    this.articlesService.getUserArticles(this.page).subscribe(p => {
      this.articles = p.content; 
      this.logger.info('refreshArticles(link): ', this.page);
    });
  }

  loadArticle(link) {
    this.logger.info('loadArticle(link): ', link);
    this.link = link;
    if (this.link) {
      this.articleService.getArticle(this.link)
        .subscribe(r => this.article = r);
    }
  }


  getImages() {
    this.filesService.getFiles(this.page).subscribe(r => this.images = r.content);
    console.log(this.images);
  }
}
