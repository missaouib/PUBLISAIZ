import { LoginService } from '../_services/login.service';
import { LoggedModel } from '../_model/login.model';
import { OnInit, Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: LoggedModel = {};

  constructor(private loginService: LoginService) { }

  ngOnInit() {
    this.loginService.logout();
  }

  login() {
    this.loginService.authenticate(this.form);
  }

}
