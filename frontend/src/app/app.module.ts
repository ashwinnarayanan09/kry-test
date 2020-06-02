import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTableModule} from '@angular/material/table';
import { AppComponent } from './app.component';
import { ServicesListComponent } from './components/services-list/services-list.component';

import { HomeComponent } from './components/home/home.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {routing} from "./app.routing";
import {HttpClientModule} from "@angular/common/http";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from "@angular/forms";
import { AddServiceComponent } from './components/services-list/add-service/add-service.component';
import {MatFormField} from "@angular/material/form-field";
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
  declarations: [
    AppComponent,
    ServicesListComponent,
    HomeComponent,
    AddServiceComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    routing,
    MatDialogModule,
    MatListModule,
    MatTabsModule,
    MatTableModule,
    MatInputModule,
    HttpClientModule,
    MatButtonModule,
    NgbModule,
    FormsModule
  ],
  entryComponents: [AddServiceComponent],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
