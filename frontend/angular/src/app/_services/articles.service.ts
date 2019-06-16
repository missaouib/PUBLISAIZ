import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { NGXLogger } from 'ngx-logger';
@Injectable({
    providedIn: 'root'
})
export class ArticlesService {

    constructor(private logger: NGXLogger, private httpclient: HttpClient) {
    }

    getArticles(page: number): Observable<any> {
        return this.httpclient.get<any>(environment.apiUrl + 'articles/all?page=' + page);
    }

    getUserArticles(page: number): any {
        return this.httpclient.get<any>(environment.apiUrl + 'articles/my?page=' + page);
    }

    getArticlesWithFilter(page: number, search: any) {
        search.page = page;
        this.logger.debug('ArticlesService#getArticlesWithFilter: ', search);
        return this.httpclient.get<any>( environment.apiUrl + 'articles/form', { params: search } );
    }
}
