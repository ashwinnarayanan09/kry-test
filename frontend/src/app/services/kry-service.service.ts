import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {Service} from "../modal/Service";

@Injectable({
  providedIn: 'root'
})
export class KryServiceService {

  constructor(private http: HttpClient) { }

  getServices(): Observable<Service[]> {
    console.log("getServices");
    return this.http.get<Service[]>('/kry/api/services');
  }

  addService() {
    return this.http.post('localhost:8083/services',{});
  }

  deleteService() {
    return this.http.get('localhost:8083/services');
  }



}
