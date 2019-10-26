import { Injectable  } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  event: any;

  constructor(private http: HttpClient, private logger: NGXLogger) {
  }

  getMyProfile() : any{
    return this.http.get<Array<Profile>>(environment.apiUrl + 'users/my_profile');
  }

  postProfile(profile: Profile) {
    return this.http.post<Profile>(environment.apiUrl + 'users/my_profile', profile);
  }

  deleteProfile(profile: Profile) {
    this.logger.debug('UsersService::deleteProfile', profile);
    return this.http.delete<boolean>(environment.apiUrl + 'users/delete_profile/' + profile.id);
  }

  public getAll(page) {
    return this.http.get(environment.apiUrl + 'users?page=' + page);
  }
  getAuthors(page) {
    return this.http.get(environment.apiUrl + 'users/authors?page=' + page);
  }

  public update(user: any) {
    this.logger.debug(user);
    return this.http.post(environment.apiUrl + 'users', user);
  }
}

export interface Profile {
  id: number;
  profilePhoto: string;
  backgroundPhoto: string;
  description: string;
  posts: Array<any>;
}