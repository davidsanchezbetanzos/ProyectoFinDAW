import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const savedUser = localStorage.getItem('user_porrina');

  if (savedUser) {
    const user = JSON.parse(savedUser);

    // Verificamos si el rol es exactamente ADMIN
    if (user.rol === 'ADMIN') {
      return true; // Pasa, es el jefe
    }
  }

  // Si no hay usuario o no es admin, redirigimos y bloqueamos
  console.warn('🚫 Acceso denegado: No tienes permisos de administrador');
  router.navigate(['/home']);
  return false;
};