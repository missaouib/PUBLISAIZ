import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, EventEmitter, Output } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LoggedModel } from '../_model/login.model';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
@Injectable({
  providedIn: 'root'
})
export class LoginService {

  authenticated = false;
  public loggedEvent: EventEmitter<any> = new EventEmitter();
  headers: any;
  credentials;
  subscribers = [];

  constructor(private httpclient: HttpClient, private router: Router, private logger: NGXLogger) { }

  subscribeForLoggedUser(a: Function) {
    this.subscribers.push(a);
  }

  getLoggedEvent() {
    this.logger.info('LoginService#getLoggedEvent');
    return this.loggedEvent;
  }

  logout() {
    sessionStorage.removeItem('logged');
    this.httpclient.get(environment.apiUrl + 'users/logout', {}).subscribe(r => this.loggedEvent.emit({}));
  }

  authenticate(credentials?: LoggedModel) {
    this.optionalNewHeaders(credentials);
    return this.selectApiCall()
      .subscribe(
        ok => this.processPositiveResponse(ok, credentials),
        err => this.processErrorResponse());
  }

  private processErrorResponse() {
    this.logger.debug('LoginService#processErrorResponse');
    this.loggedEvent.emit(null);
    this.subscribers.forEach(s => s(null));
  }

  private processPositiveResponse(ok: any, credentials?: LoggedModel) {
    //this.logger.debug('LoginService#processPositiveResponse httpclient.subscribe ok:', ok);
    this.loggedEvent.emit(ok);
    this.subscribers.forEach(s => s(ok));
    sessionStorage.setItem('logged', JSON.stringify(ok));
    if (credentials && credentials.rememberMe) {
      this.logger.error('feature not implemented yet');
    }
   // this.logger.debug('LoginService#processPositiveResponse this.router.url', this.router.url);
    if (this.router.url.indexOf('/login') === 0) {
      this.router.navigateByUrl('blog');
    }
  }

  private optionalNewHeaders(credentials: LoggedModel) {
    if (credentials && credentials.login && credentials.password) {
      this.headers = new HttpHeaders(credentials ? {
        authorization: 'Basic ' + btoa(credentials.login + ':' + credentials.password)
      } : {});
    }
  }

  private selectApiCall() {
    if (this.headers) {
    return this.httpclient.get<any>(environment.apiUrl + 'users/whoami', { headers: this.headers });
    } else {
      return this.httpclient.get<any>(environment.apiUrl + 'users/whoami');
    }
  }
}
