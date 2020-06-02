import { Component, OnInit } from '@angular/core';
import {Service} from "../../../models/Service";



@Component({
  selector: 'app-add-service',
  templateUrl: './add-service.component.html',
  styleUrls: ['./add-service.component.css']
})
export class AddServiceComponent implements OnInit {

  name: string = '';
  service:Service;

  constructor() { }

  ngOnInit(): void {
    this.service = new Service();
  }

  save(){
      console.log(this.service);
  }

}
