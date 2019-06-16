import { Component, OnInit } from '@angular/core';
import { UsersService } from './users.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  user = {};
  users = [];
  page = 0;

  constructor(private service: UsersService) { }


  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.service.getAll(this.page).subscribe((r: any) => {
      this.users = r;
      console.log(r);
    });
  }

  selected(user) {
    this.user = user;
  }

  switchActive(user) {
    user.active = !user.active;
    user.password = '"irrelevant password"';
    this.service.update(user).subscribe(r => this.refresh(), e => { this.refresh(); console.log(e); });
  }

  showPermissions(user) {
    user.showPermissions = !user.showPermissions;
    user.showRoles = false;
  }

  showRoles(user) {
    user.showRoles = !user.showRoles;
    user.showPermissions = false;
  }

}
