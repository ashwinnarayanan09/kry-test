import {PreloadAllModules, RouterModule, Routes} from "@angular/router";
import {ModuleWithProviders} from "@angular/core";
import {HomeComponent} from "./components/home/home.component";
import {ServicesListComponent} from "./components/services-list/services-list.component";
import {AddServiceComponent} from "./components/add-service/add-service.component";


const appRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'services/add', component: AddServiceComponent},

];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes, { useHash: true, preloadingStrategy: PreloadAllModules });
