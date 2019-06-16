import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FilesService {

  constructor(private httpclient: HttpClient) { }

  public getFiles(page: number): Observable<any> {
    return this.httpclient.get<any>(environment.apiUrl + 'files/all?page=' + page);
  }
  
}
