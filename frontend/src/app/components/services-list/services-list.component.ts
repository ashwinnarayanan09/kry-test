import { Component, OnInit } from '@angular/core';
import {KryServiceService} from "../../services/kry-service.service";
import {Service} from "../../models/Service";


const SERVICE_DATA: Service[] = [
  {name: 'patient-service', url: '/kry/patient', dateAdded: '2020-06-02',status:'OK'},
  {name: 'doctor-service', url: '/kry/doctor', dateAdded: '2020-06-02',status:'ERROR'},
];


@Component({
  selector: 'app-services-list',
  templateUrl: './services-list.component.html',
  styleUrls: ['./services-list.component.css']
})
export class ServicesListComponent implements OnInit {

  displayedColumns: string[] = ['name', 'url', 'dateAdded','status'];
  dataSource = SERVICE_DATA;

  services:Service[];


  constructor(private kryService:KryServiceService) { }

  ngOnInit(){

    this.getServices();
  }

  getServices(){

    this.kryService
      .getServices()
      .subscribe(result => {
          this.services = result;
          this.dataSource = this.services;
          console.log(this.services);
        },
        error => {
          console.log("Error fetching service");
        });

  }

}
