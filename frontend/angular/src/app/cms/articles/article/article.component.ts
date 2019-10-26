import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ArticleService } from 'src/app/_services/article.service';
import { ArticleModel } from 'src/app/_model/article.model';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css']
})
export class ArticleComponent implements OnInit {

  @Output() event = new EventEmitter<any>();

  link: string;
  @Input()
  article: ArticleModel;
  images_upload_url = environment.apiUrl + 'files/article';

  constructor(private articleService: ArticleService, private router: Router, private logger: NGXLogger) { }

  ngOnInit() {
    if (!this.article) {
      this.article = {};
    }
  }

  delete() {
    this.article.hide = true;
    this.articleService.postArticle(this.article).subscribe(r => {
      this.article = {};
      this.event.emit(this.article);
      this.router.navigate(['/cms/articles']);
    });
  }

  edit() {
    this.articleService.postArticle(this.article).subscribe(r => {
      this.article = r;
      this.event.emit(this.article);
      this.router.navigate(['/cms/articles/' + r.link]);
    });
  }

  uploading (blobInfo, success, failure) {
    this.logger.info(success);
    this.logger.info(failure);
    this.articleService.postImageToArticle(blobInfo).subscribe(success, failure);
  };

}
