import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { XlsComponent } from './xls.component';

describe('XlsComponent', () => {
  let component: XlsComponent;
  let fixture: ComponentFixture<XlsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ XlsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(XlsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
