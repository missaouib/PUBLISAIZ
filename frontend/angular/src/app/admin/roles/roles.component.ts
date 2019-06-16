import { Component, OnInit } from '@angular/core';
import { RolesService as RolesService } from './roles.service';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.css']
})
export class RolesComponent implements OnInit {

  page: any = '';
  roles: any[];
  newRole: any = { name: '' }
  findRole: any = { name: '', createdDate: '', createdBy: '', editedDate: '', editedBy: '', controller: '', user: '' }

  constructor(private service: RolesService) { }

  ngOnInit() {
    this.refreshGroups();
  }

  private refreshGroups() {
    this.service.getAll(this.page).subscribe((r: any) => this.roles = r.content);
  }

  createNewRole() {
    this.service.save(this.newRole).subscribe((r: any) => 
      { if (this.roles.map(a=>a.name).indexOf(r.name)<0)
        { this.roles.unshift(r); }
      });
  }

  onKeydown(event) {
    if (event.key === "Enter") {
      this.createNewRole();
    }
  }

  removeController(role, controller) {
    console.log({ role: role, controller: controller });
    this.service.removeController(role, controller)
    .subscribe(
      (returned: any) => 
        this.roles = this.roles.filter(r => r.name!=returned.name)
    );
  }

  removeUser(role, user) {
    console.log({role: role, user: user});
    this.service.removeUser(role, user)
      .subscribe((returned: any) => this.refreshGroups());
  }

  showControllers(role) {
    role.showControllers = !role.showControllers;
    role.showUsers = false;
  }

  showUsers(role) {
    role.showUsers = !role.showUsers;
    role.showControllers = false;
  }
}
