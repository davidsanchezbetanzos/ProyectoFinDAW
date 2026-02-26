import { Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { UsuariosList } from './usuarios-list';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'usuarios', component: UsuariosList },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];