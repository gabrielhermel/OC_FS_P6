import { Routes } from '@angular/router';
import { PublicLayoutComponent } from './core/layout/public-layout/public-layout';
import { LandingComponent } from './features/landing/landing';

export const routes: Routes = [
  // Public routes (no navbar)
  {
    path: '',
    component: PublicLayoutComponent,
    children: [
      {
        path: '',
        component: LandingComponent,
      },
    ],
  },
];
