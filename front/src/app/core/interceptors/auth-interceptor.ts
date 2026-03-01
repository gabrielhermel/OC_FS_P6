import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { Store } from '../services/store';

/**
 * Auth interceptor:
 * - Attaches JWT token to all outgoing requests
 * - Handles 401 responses by logging out and redirecting
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const store = inject(Store);
  const router = inject(Router);
  const token = store.get('auth_token');

  // Clone request and add Authorization header if token exists
  const authReq = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      })
    : req;

  return next(authReq).pipe(
    catchError((error: unknown) => {
      if (error instanceof HttpErrorResponse && error.status === 401) {
        // Only handle 401 for non-auth endpoints (avoid interfering with login/register errors)
        const isAuthEndpoint = req.url.includes('/auth/');

        if (!isAuthEndpoint) {
          // Clear token and redirect
          store.remove('auth_token');

          // Avoid redundant navigation
          if (router.url !== '/') {
            router.navigate(['/']);
          }
        }
      }
      return throwError(() => error);
    }),
  );
};
