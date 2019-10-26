import { Component, OnInit, ViewChild } from '@angular/core';
import { ArticlesService as ArticlesService } from 'src/app/_services/articles.service';
import { environment } from '../../../environments/environment';
import { NGXLogger } from 'ngx-logger';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {
  articles = [];
  page = 0;
  environment: { production: boolean; apiUrl: string; };
  search: { before?: Date, after?: Date, author?: string, term?: string} = {};
  @ViewChild('before', { static: false })
  after: any;
  @ViewChild('after', {static: false})
  before: any;
  beforeCache: any;
  afterCache: any;
  totalElements;
  articlesOnPage: Number;

  constructor(private articlesService: ArticlesService,
    public config: BsDatepickerConfig,
    private logger: NGXLogger) {
    config.containerClass = 'theme-dark-blue';
  }

  ngOnInit() {
    this.environment = environment;
    this.articlesService.getArticles(this.page).subscribe((p: any) => {
      this.articles = p.content;
      this.totalElements = p.totalElements;
      this.articlesOnPage = p.pageable.pageSize;
    });
    this.observeDates();
  }

  filter(e?) {
    if (e) { this.page = e.page; }
    if (!this.page) { this.page = 1; }
    this.logger.debug('this.search.after', this.search.after);
    this.articlesService
      .getArticlesWithFilter(this.page -1, {
        before: this.search.before ? this.search.before.toISOString() : '',
        after: this.search.after ? this.search.after.toISOString() : '',
        author: this.search.author ? this.search.author : '',
        term: this.search.term ? this.search.term : ''
      })
      .subscribe((p: any) => {
        this.logger.debug('getArticlesWithFilter', p);
        this.totalElements = p.totalElements;
        this.articlesOnPage = p.pageable.pageSize;
        this.articles = p.content;
    });
  }


  private observeDates() {
    setInterval(() => {
      if (this.before.nativeElement.value && this.beforeCache !== this.before.nativeElement.value) {
        this.beforeCache = this.before.nativeElement.value;
        this.filter();
      }
      if (this.after.nativeElement.value && this.afterCache !== this.after.nativeElement.value) {
        this.afterCache = this.after.nativeElement.value;
        this.filter();
      }
    }, 100);
  }
}
