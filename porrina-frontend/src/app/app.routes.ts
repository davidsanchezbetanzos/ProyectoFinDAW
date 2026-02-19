import { Routes } from '@angular/router';

import { UsuariosList } from './features/usuarios/usuarios-list/usuarios-list';


export const routes: Routes = [
     { path: 'usuarios', component: UsuariosList },
  { path: '', redirectTo: 'usuarios', pathMatch: 'full' }

];
