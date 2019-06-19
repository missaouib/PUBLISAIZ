import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { AppService } from 'src/app/app.service';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit {
  @Input() title: string;
  logged: any;
  admin = false;

  constructor(private logger: NGXLogger , private router: Router, private appService: AppService) { }

  ngOnInit() {
    this.logged = this.appService.getSessionLogged();
    if (this.logged && this.logged.roles) {
      this.admin = this.logged.roles.indexOf('admin') > -1;
    }
    this.subscribeForLoggedEvent();
  }


  private subscribeForLoggedEvent() {
    this.appService.getLoggedEvent().subscribe((l) => {
      this.logger.info('subscribeForLoggedEvent notified with : ', l);
      if (l && l.login && l.roles) {
        this.logged = l;
        this.admin = this.logged.roles.indexOf('admin') > -1;
      } else {
        this.logged = null;
        this.admin = false;
      }
    });
  }
}
