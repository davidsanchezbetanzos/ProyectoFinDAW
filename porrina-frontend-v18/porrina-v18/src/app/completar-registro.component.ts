import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-completar-registro',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 80vh;">
      <h2>¡Casi listo!</h2>
      <p>Elige el nick que verán los demás para completar tu registro</p>
      <p><strong>Nick</strong> para la Porriña:</p> 
      
      <div style="margin-top: 20px;">
        <input type="text" [(ngModel)]="nick" placeholder="Ej: Doentes polo caldo" 
               style="padding: 10px; font-size: 16px; border-radius: 4px; border: 1px solid #ccc;">
        <button (click)="guardarNick()" [disabled]="!nick || nick.length < 3"
                style="padding: 10px 20px; margin-left: 10px; cursor: pointer; background-color: #4CAF50; color: white; border: none; border-radius: 4px;">
          Aceptar
        </button>
      </div>
      <p *ngIf="nick && nick.length < 3" style="color: red; font-size: 12px;">El nick debe tener al menos 3 caracteres.</p>
    </div>
  `
})

export class CompletarRegistroComponent {
  nick: string = '';
  email: string = ''; // Lo recuperaremos de la URL o del estado
  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  constructor() {
    // Recuperamos el email que mandamos desde el login por parámetros de ruta
    this.email = this.route.snapshot.queryParams['email'] || '';
  }

  guardarNick() {
  const emailEnc = encodeURIComponent(this.email);
  const nickEnc = encodeURIComponent(this.nick);
   // Construimos la URL con los parámetros ?id=...&nick=... 
  const url = `http://localhost:8080/api/auth/completar-registro?email=${emailEnc}&nick=${nickEnc}`;

  this.http.post(url, {}).subscribe({ 
    next: () => this.router.navigate(['/usuarios']),
    error: (err) => {
        if (err.status === 409) {
        alert('Ese nick ya está pillado, ¡busca otro!');
      } else {
        alert('Error al guardar. Inténtalo de nuevo.');      }

    }
  });
  }
}