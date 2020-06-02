import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Service} from "../../models/Service";
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-add-service',
  templateUrl: './add-service.component.html',
  styleUrls: ['./add-service.component.css']
})
export class AddServiceComponent implements OnInit {

  service:Service;
  name:string;
  hostname:string;
  port:number;
  url:string;

  constructor(public activeModal: NgbActiveModal) {
  }

  ngOnInit(){
    this.service = new Service();
  }

  save(){

    this.service.name = this.name;
    this.service.hostname = this.hostname;
    this.activeModal.close({
      "result":"OK",
      "service":this.service
    });
  }


}
