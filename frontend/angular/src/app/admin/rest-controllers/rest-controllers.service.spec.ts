import { TestBed } from '@angular/core/testing';

import { RestControllersService } from './rest-controllers.service';

describe('RestControllersService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RestControllersService = TestBed.get(RestControllersService);
    expect(service).toBeTruthy();
  });
});
