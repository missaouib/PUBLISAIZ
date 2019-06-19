import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersComponent } from './users/users.component';
import { RestControllersComponent } from './rest-controllers/rest-controllers.component';
import { UserComponent } from './user/user.component';
import { RouterModule } from '@angular/router';
import { MessageComponent } from './message/message.component';
import { PermissionsComponent } from './permissions/permissions.component';
import { RolesComponent } from './roles/roles.component';
import { FormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';

const routes = [
  { path: 'users', component: UsersComponent },
  { path: 'controllers', component: RestControllersComponent },
  { path: 'users', component: UsersComponent },
  { path: 'permissions', component: PermissionsComponent },
  { path: 'user', component: UserComponent },
  { path: 'roles', component: RolesComponent },
  { path: 'message', component: MessageComponent },
  { path: '**', component: UserComponent }
];

@NgModule({
  declarations: [ UsersComponent,
                  RestControllersComponent,
                  UserComponent,
                  MessageComponent,
                  PermissionsComponent,
                  RolesComponent],
  imports: [
    PaginationModule.forRoot(),
    FormsModule,
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class AdminModule { }
