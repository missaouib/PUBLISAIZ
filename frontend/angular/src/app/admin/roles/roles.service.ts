import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RolesService {

  constructor(private http: HttpClient) { }

  public getAll(page) {
    return this.http.get(environment.apiUrl + 'roles?page=' + page);
  }

  save(newRole: any) {
    return this.http.post(environment.apiUrl + 'roles', newRole);
  }

  public removeUser(role: any, user: any) {
    return this.http.delete(environment.apiUrl + 'roles/role:' + role.id + '/user:' + user.id);
  }

  public removeController(role: any, controller: any) {
    return this.http.delete(environment.apiUrl + 'roles/role:' + role.id + '/controller:' + controller.id);
  }
}
