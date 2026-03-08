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



  ngOnInit() {
    
    // Recuperar usuario de localStorage si existe (si el usuario ya se logueo anteriormente
    const savedUser = localStorage.getItem('user_porrina');
    if (savedUser) {
      this.user = JSON.parse(savedUser);
      console.log('Usuario recuperado del caché:', this.user);
    }
    
    
    this.authService.authState.subscribe((googleUser) => { //escuchamos cuando google devuelve su objeto user
    if (googleUser) {      
      // Cuando exista ya tendremos nombre, foto y email
      
      // Llamamos a nuestro backend para traer los datos de nuestra DB filtrando por el email
      this.http.get(`http://localhost:8080/api/usuarios/perfil/${googleUser.email}`)
        .subscribe({
          next: (dbUser: any) => {           
            //cuando llegue el objeto del get, lo metemos en dbUser
            // FUSIONAMOS AMBOS OBJETOS
            // Usamos el "spread operator" (...) para juntar todo en una sola variable
            this.user = { ...googleUser, ...dbUser };
            console.log('Usuario completo cargado:', this.user);
            localStorage.setItem('user_porrina', JSON.stringify(this.user)); //lo guardamos en localStorage
          },
          error: (err) => {
            if (err.status === 404) {
              this.registrarNuevoUsuario(googleUser);
            }
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
  // Limpiamos la variable del componente 
  // Esto hace que el *ngIf del HTML reaccione al instante y oculte el avatar
  this.user = null;

  //  Borramos el user del localstorage
  localStorage.removeItem('user_porrina');

  // Cerramos sesión en Google
  // Usamos un .catch por si Google da error (a veces pasa si la sesión ya caducó)
  this.authService.signOut()
    .then(() => {
      console.log('Sesión de Google cerrada');
      this.router.navigate(['/home']);
    })
    .catch((error) => {
      console.log('Google ya estaba cerrado o hubo un error, redirigiendo igual...');
      this.router.navigate(['/home']);
    });
  }

    // Método para hacer el POST si no existe
registrarNuevoUsuario(googleUser: any) {
  const nuevoUsuario = {
    nombre: googleUser.name,
    email: googleUser.email,
    nick: googleUser.email.split('@')[0], // Nick inicial
    rol: 'USER'
  };

  this.http.post('http://localhost:8080/api/usuarios', nuevoUsuario) 
    .subscribe({
      next: (dbUser: any) => {
        console.log('Usuario registrado con éxito tras login');
         this.user = { ...googleUser, ...dbUser }; //fusionamos los objetos
         localStorage.setItem('user_porrina', JSON.stringify(this.user)); //lo guardamos en localStorage
      },
      error: (postErr) => console.error('Error al registrar usuario', postErr)
    });
}

}