import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SocialAuthService, GoogleSigninButtonModule } from '@abacritt/angularx-social-login';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, GoogleSigninButtonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  authService = inject(SocialAuthService);
  router = inject(Router);
  http = inject(HttpClient);
  
  user: any = null;

  ngOnInit() {this.authService.authState.subscribe((googleUser) => { //escuchamos cuando google devuelve su objeto user
    if (googleUser) {
      // Cuando exista ya tendremos nombre, foto y email
      
      // Llamamos a nuestro backend para traer los datos de nuestra DB fultrando por el email
      this.http.get(`http://localhost:8080/api/usuarios/perfil/${googleUser.email}`)
        .subscribe({
          next: (dbUser: any) => { //cuando llegue el objeto del get, lo metemos en dbUser
            // FUSIONAMOS AMBOS OBJETOS
            // Usamos el "spread operator" (...) para juntar todo en una sola variable
            this.user = { ...googleUser, ...dbUser };
            console.log('Usuario completo cargado:', this.user);
          },
          error: (err) => {
            console.error('Error al traer datos extra del usuario', err);
            this.user = googleUser; // Si no carga la peticion get a BD, al menos dejamos los datos de Google
          }
        });
    } else {
      this.user = null;
    }
  });

  }

  logout() {
    this.authService.signOut().then(() => {
      this.router.navigate(['/login']);
    });
  }
}