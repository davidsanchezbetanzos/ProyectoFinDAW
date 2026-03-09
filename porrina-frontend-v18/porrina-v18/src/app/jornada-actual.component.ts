import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-jornada-actual',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './jornada-actual.component.html',
  styleUrl: './jornada-actual.component.css'
 
})
export class JornadaActualComponent {
  private http = inject(HttpClient);
  
  user: any = null;
  jornadaActiva: any = null;
  pronosticosUsuario: any[] = []; // Los 3 pronósticos del usuario logueado
  todosLosPronosticos: any[] = []; // Lista global para la tabla inferior

  ngOnInit() {
    // Recuperar usuario del localStorage
    const savedUser = localStorage.getItem('user_porrina');
    if (savedUser) this.user = JSON.parse(savedUser);

    // 
    this.cargarJornadaActiva();
  }

  cargarJornadaActiva() {
    this.http.get('http://localhost:8080/api/jornadas/activa').subscribe((j: any) => {
      this.jornadaActiva = j;
      if (this.jornadaActiva && this.user) {
        this.cargarMisPronosticos();
        this.cargarTodosLosPronosticos();
      }
    });
  }

  cargarMisPronosticos() {
    // Buscamos si el usuario ya tiene pronósticos para esta jornada
    this.http.get(`http://localhost:8080/api/pronosticos/usuario/${this.user.id}/jornada/${this.jornadaActiva.id}`)
      .subscribe((res: any) => {
        this.pronosticosUsuario = res;
        // Si no existen, preparamos objetos vacíos vinculados a los partidos
        if (this.pronosticosUsuario.length === 0) {
          this.pronosticosUsuario = this.jornadaActiva.partidos.map((p: any) => ({
            partido: p,
            usuario: { id: this.user.id },
            golesLocal: null,
            golesVisitante: null
          }));
        }
      });
  }

  guardarPronosticos() {
    this.http.post('http://localhost:8080/api/pronosticos/guardar-varios', this.pronosticosUsuario)
      .subscribe(() => {
        alert('¡Pronósticos guardados!');
        this.cargarTodosLosPronosticos(); // Refrescar muro
      });
  }

  cargarTodosLosPronosticos() {
  this.http.get<any[]>(`http://localhost:8080/api/pronosticos/jornada/${this.jornadaActiva.id}/todos`)
    .subscribe((data) => { 
      const grupos = data.reduce((acc: any, current: any) => {
        const userId = current.usuario.id;
        if (!acc[userId]) {
          acc[userId] = { 
            usuarioId: userId,
            nombre: current.usuario.nombre, 
            apuestas: [] 
          };
        }
        acc[userId].apuestas.push(current);
        return acc;
      }, {});

      // ORDENAR: Esto asegura que el Partido 1 siempre sea el primero, el 2 el segundo, etc.
      const listaAgrupada = Object.values(grupos);
      listaAgrupada.forEach((user: any) => {
        user.apuestas.sort((a: any, b: any) => a.partido.id - b.partido.id);
      });
      
      this.todosLosPronosticos = listaAgrupada;
    });
  }
}