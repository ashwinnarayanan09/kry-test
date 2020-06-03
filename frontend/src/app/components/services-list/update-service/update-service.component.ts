import {Component, Inject, OnInit} from '@angular/core';
import {Service} from "../../../models/Service";
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-update-service',
  templateUrl: './update-service.component.html',
  styleUrls: ['./update-service.component.css']
})
export class UpdateServiceComponent implements OnInit {

  service:Service;
  name:String;
  constructor( @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.service = new Service();
    console.log(this.data);
  }

}
