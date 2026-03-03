import { Routes } from '@angular/router';
import { HomeComponent } from './home.component';
import { ClasificacionIndividualComponent } from './clasificacion-individual.component';
import { ClasificacionEquiposComponent } from './clasificacion-equipos.component';
import { JornadaActualComponent } from './jornada-actual.component';
import { HistoricoJornadaComponent } from './historico-jornada.component';
import { LoginComponent } from './login.component';
import { CompletarRegistroComponent } from './completar-registro.component';
import { UsuariosList } from './usuarios-list.component';
import { AdminPanelComponent } from './admin-panel.component';

export const routes: Routes = [

{ path: 'home', component: HomeComponent },
  { path: 'clasificacion', component: ClasificacionIndividualComponent },
  { path: 'equipos', component: ClasificacionEquiposComponent },
  { path: 'jornada', component: JornadaActualComponent },
  { path: 'historico', component: HistoricoJornadaComponent },
  { path: 'login', component: LoginComponent },
  { path: 'completar-registro', component: CompletarRegistroComponent },
  { path: 'usuarios', component: UsuariosList },
  { path: 'admin-panel', component: AdminPanelComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }, 
  { path: '**', redirectTo: 'home' }
];