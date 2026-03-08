import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clasificacion-equipos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './clasificacion-equipos.component.html',
  styleUrl: './clasificacion-equipos.component.css' // Puedes reutilizar el CSS de la individual
})
export class ClasificacionEquiposComponent implements OnInit {
  private http = inject(HttpClient);
  equipos: any[] = [];
  cargando = true;

  ngOnInit() {
    this.http.get<any[]>('http://localhost:8080/api/equipos/clasificacion')
      .subscribe({
        next: (data) => {
          this.equipos = data;
          this.cargando = false;
        },
        error: (err) => {
          console.error(err);
          this.cargando = false;
        }
      });
  }
}