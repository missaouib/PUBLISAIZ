import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PermissionsService {

  constructor(private http: HttpClient) { 
  }

  public getAll(page) {
    return this.http.get(environment.apiUrl + 'permissions?page=' + page);
  }
  
  update(permission: any) {
    return this.http.post(environment.apiUrl + 'permissions', permission);
  }
}
