import { Component, OnInit } from '@angular/core';
import {KryServiceService} from "../../services/kry-service.service";
import {Service} from "../../models/Service";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {MatDialog} from '@angular/material/dialog';
import {AddServiceComponent} from "./add-service/add-service.component";
import {UpdateServiceComponent} from "./update-service/update-service.component";

@Component({
  selector: 'app-services-list',
  templateUrl: './services-list.component.html',
  styleUrls: ['./services-list.component.css']
})
export class ServicesListComponent implements OnInit {

  name:string='';
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
        },
        error => {
          console.log(error);
        });

  }

  //Modals

  openAddServiceDialog(): void {
    const dialogRef = this.dialog.open(AddServiceComponent);

    dialogRef.afterClosed().subscribe(result => {
      this.service = result;
      this.kryService.addService(this.service).subscribe(
        res => {
         console.log(res);
         this.getServices();
        },
        error => {
          console.log(error);
        });;
    });


  }

  openUpdateServiceDialog(service:Service): void {
    const dialogRef = this.dialog.open(UpdateServiceComponent,{data:service});

    dialogRef.afterClosed().subscribe(result => {
      this.service = result;
      this.service.name = this.selectedService.name;
      console.log(result);
      this.kryService.updateService(this.service).subscribe(
        res => {
          console.log(res);
          this.getServices();
        },
        error => {
          console.log(error);
        });;
    });


  }

  deleteService(service:Service){
    this.kryService.deleteService(service).subscribe(
      res => {
        console.log(res);
        this.getServices();
      },
      error => {
        console.log(error);
      });;
  }

}
