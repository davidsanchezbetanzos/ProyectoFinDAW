import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-usuarios-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuarios-list.html',
  styleUrl: './usuarios-list.css',
})
export class UsuariosList implements OnInit{
  usuarios: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {

     console.log("ngOnInit ejecutado");

    this.http.get<any[]>('http://localhost:8080/api/usuarios')
      .subscribe(data => this.usuarios = data);
  }

}
