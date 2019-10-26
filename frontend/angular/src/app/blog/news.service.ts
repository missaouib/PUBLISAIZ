import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  constructor(private logger: NGXLogger, private httpclient: HttpClient) {
  }

  getPage(page: number): Observable<any> {
    return this.httpclient.get(environment.apiUrl + 'scrapped/all?page=' + page);
  }
}
