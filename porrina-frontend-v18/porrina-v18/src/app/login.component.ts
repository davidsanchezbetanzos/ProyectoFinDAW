import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SocialAuthService, GoogleLoginProvider, GoogleSigninButtonModule } from '@abacritt/angularx-social-login';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, GoogleSigninButtonModule],
  template: `
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 80vh;">
      <h1>Bienvenido a la Porriña</h1>
      <p>Inicia sesión con tu cuenta de Google</p>
     <asl-google-signin-button type="standard" size="large"></asl-google-signin-button>
    </div>
  `
})
export class LoginComponent implements OnInit {
  private authService = inject(SocialAuthService);
  private http = inject(HttpClient);
  private router = inject(Router);

  ngOnInit() {
    this.authService.authState.subscribe((user) => {
      if (user  && user.email && user.name) {
        console.log('Usuario de Google:', user);
        this.loginConBackend(user.email, user.name);
      }
    });
  }

  loginConGoogle() {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  loginConBackend(email: string, nombre: string) {
    const url = `http://localhost:8080/api/auth/test-google?email=${email}&nombre=${nombre}`;
    this.http.get<any>(url).subscribe({
      next: (usuario) => {
        console.log('Backend OK:', usuario);
        if (!usuario.nick) {
          this.router.navigate(['/completar-registro'],{
          queryParams: { email: usuario.email }
        });
        }
        else {
      this.router.navigate(['/usuarios']);
    }            
      },
      error: (err) => console.error('Error Backend:', err)
    });
  }
}