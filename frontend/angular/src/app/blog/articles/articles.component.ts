import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticlesService as ArticlesService } from 'src/app/_services/articles.service';
import { environment } from '../../../environments/environment';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {
  articles = [];
  page = 0;
  environment: { production: boolean; apiUrl: string; };
  search = { before: '', after: '', author: '', term: ''};

  constructor(private router: Router, private articlesService: ArticlesService, private logger: NGXLogger) { }

  ngOnInit() {
    this.environment = environment;
    this.articlesService.getArticles(this.page).subscribe((p: any) => {
      this.articles = p.content;
    });
  }

  filter(e) {
    this.logger.debug('filter: ', e);
    this.articlesService.getArticlesWithFilter(this.page, this.search).subscribe((p: any) => {
      this.logger.debug('p', p);
      this.articles = p;
    });
  }

}
