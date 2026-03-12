import { Routes } from '@angular/router';
import { PublicLayout } from './core/layout/public-layout/public-layout';
import { AuthLayout } from './core/layout/auth-layout/auth-layout';
import { Landing } from './features/landing/landing';
import { Register } from './features/auth/register/register';
import { Feed } from './features/articles/feed/feed';
import { publicGuard } from './core/guards/public-guard';
import { authGuard } from './core/guards/auth-guard';
import { MainLayout } from './core/layout/main-layout/main-layout';
import { Login } from './features/auth/login/login';
import { Topics } from './features/topics/list/topics';
import { ArticleDetail} from './features/articles/detail/detail';
import { CreateArticle } from './features/articles/create/create';

export const routes: Routes = [
  // Public routes (no navbar)
  {
    path: '',
    component: PublicLayout,
    canActivate: [publicGuard],
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
    canActivate: [publicGuard],
    children: [
      {
        path: 'register',
        component: Register,
      },
      {
        path: 'login',
        component: Login,
      },
    ],
  },
  // Secured routes (full navbar with user menu)
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      {
        path: 'articles',
        component: Feed,
      },
      {
        path: 'articles/create',
        component: CreateArticle,
      },
      {
        path: 'articles/:id',
        component: ArticleDetail,
      },
      {
        path: 'topics',
        component: Topics,
      },
    ],
  },
];
