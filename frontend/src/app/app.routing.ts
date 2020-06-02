import {PreloadAllModules, RouterModule, Routes} from "@angular/router";
import {ModuleWithProviders} from "@angular/core";
import {HomeComponent} from "./components/home/home.component";

const appRoutes: Routes = [
  {path: '', component: HomeComponent},

];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes, { useHash: true, preloadingStrategy: PreloadAllModules });
