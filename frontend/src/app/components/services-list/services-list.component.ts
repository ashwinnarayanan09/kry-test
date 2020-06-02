import { Component, OnInit } from '@angular/core';
import {KryServiceService} from "../../services/kry-service.service";
import {Service} from "../../models/Service";
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatDialogModule} from '@angular/material/dialog';
import {AddServiceComponent} from "./add-service/add-service.component";

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

  name:string='';

  displayedColumns: string[] = ['name','hostname','port','url', 'created','status'];
  dataSource:Service[];

  services:Service[];
  service:Service;
  selectedService:Service;

  constructor(private kryService:KryServiceService,private modalService: NgbModal,private dialog: MatDialog) { }

  ngOnInit(){
    this.service = new Service();
    this.getServices();
  }

  onSelect(service: Service): void {
    this.selectedService = service;
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

  openAddServiceDialog(): void {
    const dialogRef = this.dialog.open(AddServiceComponent);

    dialogRef.afterClosed().subscribe(result => {
      this.service = result;
      console.log(result);
      this.kryService.addService(this.service).subscribe(
        res => {
         console.log();
        },
        error => {
          console.log("Error");
        });;
      //this.animal = result;
    });


  }


  deleteService(service:Service){
    this.kryService.deleteService(this.service).subscribe(
      res => {
        console.log();
      },
      error => {
        console.log("Error");
      });;
  }

}
