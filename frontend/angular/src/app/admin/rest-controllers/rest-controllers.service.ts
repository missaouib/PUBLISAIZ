import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestControllersService {

  constructor(private httpclient: HttpClient) { }

  public getAllControllers(page?: number): Observable<any>{
    return this.httpclient.get(environment.apiUrl + 'controllers?page='+page);
  }

  public post(controller: any): Observable<any> {
    return this.httpclient.post(environment.apiUrl + 'controllers', controller);
  }
}
