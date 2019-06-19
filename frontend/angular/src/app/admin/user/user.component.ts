import { Component, OnInit, Output, EventEmitter , Input } from '@angular/core';
import { UsersService } from '../users/users.service';
import { RolesService } from '../roles/roles.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  @Input()
  user: any = {};
  roles: [];
  error = [];
  @Output()
  event: EventEmitter<any> = new EventEmitter<any>();

  constructor(private service: UsersService, private rolesService: RolesService) { }

  ngOnInit() {
    this.rolesService.getAll(0).subscribe(
      (r: any) => this.roles = r.content
    );
  }

  create(user, selectedRole) {
    console.log('selectedRole', selectedRole);
    if (!this.user.roles) {this.user.roles = [];}
    if (selectedRole) {this.user.roles.push(selectedRole);}
    if (user.id) {this.user.password = 'irrelevant ignored string'; }
    this.service.update(this.user).subscribe(r => {
      this.user = {};
      this.error = [];
      this.event.emit(r);
    },
      e => this.error = e.error
    );
  }

}
