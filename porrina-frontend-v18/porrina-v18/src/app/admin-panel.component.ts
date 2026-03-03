import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-admin-panel',
  imports: [],
  standalone: true,
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.css'
})
export class AdminPanelComponent implements OnInit {
    private http = inject(HttpClient);
  usuarios: any[] = [];

  ngOnInit() {
    this.cargarUsuarios();
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

}