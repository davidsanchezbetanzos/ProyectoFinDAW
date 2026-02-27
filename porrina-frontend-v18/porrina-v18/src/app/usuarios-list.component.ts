import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-usuarios-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuarios-list.component.html',
  styleUrl: './usuarios-list.component.css',
})
export class UsuariosList implements OnInit{
  usuarios$!: Observable<any[]>;

  constructor(private http: HttpClient) {}

  ngOnInit() {

     console.log("ngOnInit ejecutado");

    this.usuarios$ = this.http.get<any[]>('http://localhost:8080/api/usuarios');
  }

}
