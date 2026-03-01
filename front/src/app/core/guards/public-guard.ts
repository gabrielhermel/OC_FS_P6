import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { toObservable } from '@angular/core/rxjs-interop';
import { filter, take, map } from 'rxjs';
import { Auth } from '../services/auth';

export const publicGuard: CanActivateFn = () => {
  const auth = inject(Auth);
  const router = inject(Router);

  return toObservable(auth.authInitialized).pipe(
    filter((initialized) => initialized),
    take(1),
    map(() => (auth.isAuthenticated() ? router.createUrlTree(['/articles']) : true)),
  );
};
