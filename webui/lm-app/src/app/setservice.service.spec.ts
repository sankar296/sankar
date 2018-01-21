import { TestBed, inject } from '@angular/core/testing';

import { SetserviceService } from './setservice.service';

describe('SetserviceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SetserviceService]
    });
  });

  it('should be created', inject([SetserviceService], (service: SetserviceService) => {
    expect(service).toBeTruthy();
  }));
});
