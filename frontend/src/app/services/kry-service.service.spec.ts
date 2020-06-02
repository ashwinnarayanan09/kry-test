import { TestBed } from '@angular/core/testing';

import { KryServiceService } from './kry-service.service';

describe('KryServiceService', () => {
  let service: KryServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KryServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
