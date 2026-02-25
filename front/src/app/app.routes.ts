import { Routes } from '@angular/router';
import { PublicLayout } from './core/layout/public-layout/public-layout';
import { Landing } from './features/landing/landing';

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
];
