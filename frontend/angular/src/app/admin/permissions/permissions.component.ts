import { Component, OnInit } from '@angular/core';
import { PermissionsService } from './permissions.service';

@Component({
  selector: 'app-permissions',
  templateUrl: './permissions.component.html',
  styleUrls: ['./permissions.component.css']
})
export class PermissionsComponent implements OnInit {
  page: number;
  permissions = [];

  constructor(private service: PermissionsService) { }

  ngOnInit() {
    this.refreshPermissions();
  }

  refreshPermissions(){
    this.service.getAll(this.page).subscribe((r: any) => {
      this.permissions = r.content;
      console.log(r);
    });
  }

  switchActive(permission) {
    permission.active = !permission.active;
    this.service.update(permission).subscribe(r=>console.log(r))
  }

}
