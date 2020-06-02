import { Component, OnInit } from '@angular/core';
import {KryServiceService} from "../../services/kry-service.service";
import {Service} from "../../models/Service";
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {AddServiceComponent} from "../add-service/add-service.component";
import {MatDialog} from "@angular/material/dialog";

const SERVICE_DATA: Service[] = [
  {name: 'patient-service',hostname:'localhost',port:8083, url: '/kry/patient', created: '2020-06-02',status:'OK'},
  {name: 'doctor-service', hostname:'localhost',port:8090,url: '/kry/doctor', created: '2020-06-02',status:'ERROR'},
];


@Component({
  selector: 'app-services-list',
  templateUrl: './services-list.component.html',
  styleUrls: ['./services-list.component.css']
})
export class ServicesListComponent implements OnInit {

  displayedColumns: string[] = ['name','hostname','port','url', 'created','status'];
  dataSource:Service[];

  services:Service[];
  service:Service;

  constructor(private kryService:KryServiceService,private modalService: NgbModal,private dialog: MatDialog) { }

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

  //Modals

  openAddService(content) {
    const modalRef = this.modalService.open(AddServiceComponent);
    modalRef.result.then((result) => {
      console.log(result);
      if (result.result == "OK") {
        this.service = result.service;
        console.log(this.service.hostname);
      }

    }, (reason) => {
      console.log(reason);
    });
  }

}
