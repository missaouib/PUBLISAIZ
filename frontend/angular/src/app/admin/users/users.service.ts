import { Injectable, ViewChild } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  event:any;

  constructor(private http: HttpClient) {
  }

  public getAll(page) {
    return this.http.get(environment.apiUrl + 'users?page=' + page);
  }

  public update(user: any) {
    console.log(user);
    return this.http.post(environment.apiUrl + 'users', user);
  }
}
