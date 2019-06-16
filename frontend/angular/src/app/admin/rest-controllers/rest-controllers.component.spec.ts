import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RestControllersComponent } from './rest-controllers.component';

describe('RestControllersComponent', () => {
  let component: RestControllersComponent;
  let fixture: ComponentFixture<RestControllersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RestControllersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RestControllersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
