import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {Service} from "../models/Service";


@Injectable({
  providedIn: 'root'
})
export class KryServiceService {

  constructor(private http: HttpClient) { }

  getServices(): Observable<Service[]> {
    console.log("getServices");
    return this.http.get<Service[]>('/kry/api/services');
  }

  addService(service:Service) {
    console.log("addService");
    return this.http.post('/kry/api/services',JSON.stringify(service));
  }

  deleteService(service:Service) {
    return this.http.delete('/kry/api/services/delete',JSON.stringify(service));
  }



}
