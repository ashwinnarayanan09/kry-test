import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {Service} from "../models/Service";


@Injectable({
  providedIn: 'root'
})

export class KryServiceService {

  constructor(private http: HttpClient) { }

  getServices(): Observable<Service[]> {
    return this.http.get<Service[]>('/kry/api/services');
  }

  addService(service:Service) {
    return this.http.post('/kry/api/services',JSON.stringify(service));
  }

  updateService(service:Service) {
    return this.http.put('/kry/api/services',JSON.stringify(service));
  }

  deleteService(service:Service) {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), body: JSON.stringify(service)
    };
    return this.http.delete('/kry/api/services',httpOptions);
  }



}
