import { Component, OnInit, OnDestroy } from '@angular/core';
import { RestControllersService } from './rest-controllers.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-rest-controllers',
  templateUrl: './rest-controllers.component.html',
  styleUrls: ['./rest-controllers.component.css']
})
export class RestControllersComponent implements OnInit {

  page = 0;
  controllers = [];
  totalElements: any;

  constructor(
    private logger: NGXLogger,
    private restControllersService: RestControllersService) { 

    }

  ngOnInit() {
    this.refreshControllers();
    console.log(this.controllers);
  }

  refreshControllers(e?) {
    if (e) { this.page = e.page -1; }
    this.restControllersService.getAllControllers(this.page).subscribe(
      r => {
        this.logger.info('refreshControllers', r);
        this.controllers = r.content;
        this.totalElements = r.totalElements;
        console.log(this.controllers);
      }
    );
  }

  confirmChanges(controller, addRole, deleteRole, addPermission, deletePermission) {
    console.log(this.controllers);
    console.log(controller);
    controller.addRole = addRole.value;
    controller.deleteRole = deleteRole.value;
    controller.addPermission = addPermission.value;
    controller.deletePermission = deletePermission.value;
    this.restControllersService
      .post(controller)
      .subscribe(c => {this.controllers
        .filter(t => c.id === t.id)
        .concat([c]);
        this.refreshControllers();
      });
  }

}
