import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { ArticleModel } from '../_model/article.model';
import { NGXLogger } from 'ngx-logger';


@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  constructor(private httpclient: HttpClient, private logger: NGXLogger) {
  }

  getArticle(link: string): Observable<ArticleModel> {
    this.logger.debug('ArticleService getArticle', link);
    return this.httpclient.get<ArticleModel>(environment.apiUrl + 'articles/article/' + link);
  }

  postArticle(article: ArticleModel): Observable<any> {
    this.logger.debug('ArticleService postArticle', article);
    return this.httpclient.post<any>(environment.apiUrl + 'articles', article);
  }
}
