import { Routes } from '@angular/router';
import { PublicLayout } from './core/layout/public-layout/public-layout';
import { AuthLayout } from './core/layout/auth-layout/auth-layout';
import { Landing } from './features/landing/landing';
import { Register } from './features/auth/register/register';

export const routes: Routes = [
  // Public routes (no navbar)
  {
    path: '',
    component: PublicLayout,
    children: [
      {
        path: '',
        component: Landing,
      },
    ],
  },
  // Auth routes (navbar with logo only)
  {
    path: '',
    component: AuthLayout,
    children: [
      {
        path: 'register',
        component: Register,
      },
    ],
  },
];
