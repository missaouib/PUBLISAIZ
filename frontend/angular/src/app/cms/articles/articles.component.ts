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
  @ViewChild('articleComponent', {static: false})
  articleComponent: ArticleComponent;
  logged = {};
  articles = <ArticleModel[]>[];
  link: string;
  article: ArticleModel;
  images: any;
  environment: { production: boolean; apiUrl: string; };
  articlesPerPage: Number;
  imagesPerPage: Number;

  constructor(private router: Router,
    private articlesService: ArticlesService,
    private articleService: ArticleService,
    private route: ActivatedRoute,
    private logger: NGXLogger,
    private filesService: FilesService) { }

    articlesTotalItems = 0;
    articlesPage = 1;
    imagesTotalItems = 0;
    imagesPage = 1;

  ngOnInit() {
    this.environment = environment;
    if (!this.logged) {
      this.router.navigateByUrl('login');
    }
    this.refreshAll();
  }


  updateArticle(image) {
    this.article.image = image;
    this.articleService.postArticle(this.article).subscribe(
      r => {
        this.article = r.content;
        this.refreshAll();
      });
  }

  onEvent(event) {
    this.refreshAll();
  }

  public refreshAll() {
    this.logger.info('refreshAll');
    this.loadArticle(this.route.snapshot.params.link);
    this.getImages();
    this.refreshArticles();
  }

  public refreshArticles(e?) {
    if (e) { this.articlesPage = e.page; }
    if(this.articlesPage){
      this.articlesService.getUserArticles(this.articlesPage -1).subscribe(p => {
        this.logger.info('refreshArticles: ', p);
        this.articles = p.content;
        this.articlesTotalItems = p.totalElements;
        this.articlesPerPage = p.pageable.pageSize;
        this.logger.info('refreshArticles: ', this.articlesPage);
      });
    }
  }

  loadArticle(link) {
    this.logger.info('loadArticle(link): ', link);
    this.link = link;
    if (this.link) {
      this.articleService.getArticle(this.link)
        .subscribe(r => this.article = r);
    }
  }

  getImages(e?) {
    if (e) { this.imagesPage = e.page; }
    this.filesService.getFiles(this.imagesPage -1).subscribe(r => {
      this.logger.info('getImages: ', r);
      this.images = r.content;
      this.imagesTotalItems = r.totalElements;
      this.imagesPerPage = r.pageable.pageSize;
    });
    console.log(this.images);
  }
}
