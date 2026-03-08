import { Component,inject,OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clasificacion-individual',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './clasificacion-individual.component.html',
  styleUrl: './clasificacion-individual.component.css' 
})
export class ClasificacionIndividualComponent implements OnInit {
  private http = inject(HttpClient);
  usuarios: any[] = [];
  public cargando: boolean = true;

  ngOnInit() {
    this.obtenerRanking();
  }

  obtenerRanking() {
    this.http.get<any[]>('http://localhost:8080/api/usuarios/clasificacion')
      .subscribe({
        next: (data) => {
          this.usuarios = data;
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error al cargar la clasificación', err);
          this.cargando = false;
        }
      });
  }
}