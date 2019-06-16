import { Component, OnInit, OnDestroy } from '@angular/core';
import { RestControllersService } from './rest-controllers.service';
import { controllers } from 'chart.js';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-rest-controllers',
  templateUrl: './rest-controllers.component.html',
  styleUrls: ['./rest-controllers.component.css']
})
export class RestControllersComponent implements OnInit, OnDestroy {

  page = 0;
  controllers = [];
  sub: any;
  last: any;

  constructor(private restControllersService: RestControllersService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    this.refreshControllers();
    console.log(this.controllers);
    this.sub = this.route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.page = Number.parseInt(params['page']) || 0;
        this.refreshControllers();
      });
  }
  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  nextPage() {
    this.router.navigate(['product-list'], { queryParams: { page: this.page + 1 } });
  }

  refreshControllers() {
    this.restControllersService.getAllControllers(this.page).subscribe(
      r => {
        this.controllers = r;
        this.last = r.lastPage;
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
