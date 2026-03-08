import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms'

@Component({
  selector: 'app-admin-panel',
  imports: [FormsModule, CommonModule],
  standalone: true,
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.css'
})
export class AdminPanelComponent implements OnInit {
    private http = inject(HttpClient);
  usuarios: any[] = [];
  jornadas: any[] = [];

  ngOnInit() {
    this.cargarUsuarios();
    this.obtenerJornadas();
  }

  cargarUsuarios() {
    this.http.get<any[]>('http://localhost:8080/api/usuarios')
      .subscribe({
        next: (data) => this.usuarios = data,
        error: (err) => console.error('Error al cargar usuarios', err)
      });
  }

  cambiarEstadoPago(usuario: any) {    
    this.http.put(`http://localhost:8080/api/usuarios/${usuario.id}/toggle-pago`, {})
      .subscribe(() => {
        this.cargarUsuarios(); // Recargamos la lista para ver el cambio
      });
  }

  

  obtenerJornadas() {
    this.http.get<any[]>('http://localhost:8080/api/jornadas')
      .subscribe({
        next: (data) => {
          // Ordenamos por fecha de inicio para que la más reciente/próxima se vea bien
          this.jornadas = data.sort((a, b) => new Date(b.fechaini).getTime() - new Date(a.fechaini).getTime());
        },
        error: (err) => console.error('Error al cargar jornadas', err)
      });
  }

  
  

  // Objeto para la nueva jornada
  nuevaJornada = {
    fechaini: '',
    fechafin: '',
    estado: 'PLANIFICADA',
    partidos: [
      { equipolocal: '', equipovisitante: '' },
      { equipolocal: '', equipovisitante: '' },
      { equipolocal: '', equipovisitante: '' }
    ]
  };

  crearJornada() {
    // Validación básica
    if (!this.nuevaJornada.fechaini || !this.nuevaJornada.fechafin) {
      alert('Por favor, selecciona las fechas.');
      return;
    }

    this.http.post('http://localhost:8080/api/jornadas', this.nuevaJornada)
      .subscribe({
        next: () => {
          alert('¡Jornada creada con éxito!');
          // Limpiamos el formulario
          this.resetForm();
        },
        error: (err) => console.error('Error al crear jornada', err)
      });
  }

  resetForm() {
    this.nuevaJornada = {
      fechaini: '',
      fechafin: '',
      estado: 'PLANIFICADA',
      partidos: [
        { equipolocal: '', equipovisitante: '' },
        { equipolocal: '', equipovisitante: '' },
        { equipolocal: '', equipovisitante: '' }
      ]
    };
  }

  


}