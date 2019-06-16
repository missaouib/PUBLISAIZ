import { Injectable } from '@angular/core';
import { LoginService } from './_services/login.service';
import { NGXLogger } from 'ngx-logger';

@Injectable({
  providedIn: 'root'
})
export class AppService {
  logged: any;
  verifyUser = true;

  constructor(private ls: LoginService, private logger: NGXLogger) {
    this.ls = ls;
    this.refreshLogged();
  }

  private refreshLogged() {
    this.getSessionLogged();
    setInterval(() => {
      this.turnOnLOGGEDCheckerForLoggedUsersOnly();
      if (this.verifyUser) {
        this.ls.authenticate();
      } else {
        this.ls.loggedEvent.emit({});
      }
    }, 10000);
  }

  private turnOnLOGGEDCheckerForLoggedUsersOnly() {
    if (this.logged && this.logged.login) {
      this.verifyUser = true;
    }
  }

  getLoggedEvent() {
    this.logger.info('AppService#getLoggedEvent');
    return this.ls.loggedEvent;
  }

  getSessionLogged() {
    if (!this.logged) {
      const loggedString = sessionStorage.getItem('logged');
      this.logger.debug('AppService#getSessionLogged :: this.logged ', this.logged);
      this.logger.debug('AppService#getSessionLogged', loggedString);
      if (loggedString && loggedString !== 'undefined' && loggedString.length > 0) {
        this.logged = JSON.parse(loggedString);
      }
    }
  }
}
