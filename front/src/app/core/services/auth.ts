import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, tap, catchError, throwError, EMPTY, take } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../shared/models/auth';
import { User } from '../../shared/models/user';
import { Store } from './store';
import { UpdateProfileRequest } from '../../shared/models/update-profile';

/**
 * Authentication service handling user registration, login, logout, and token management.
 */
@Injectable({
  providedIn: 'root',
})
export class Auth {
  private static readonly TOKEN_KEY = 'auth_token';
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  /** Current authenticated user (null if not logged in) */
  private readonly _currentUser = signal<User | null>(null);
  readonly currentUser = this._currentUser.asReadonly();

  /** Whether a user is currently authenticated: derived from currentUser */
  readonly isAuthenticated = computed(() => this._currentUser() !== null);

  /** Whether auth has finished initializing */
  private readonly _authInitialized = signal(false);
  readonly authInitialized = this._authInitialized.asReadonly();

  constructor(
    private http: HttpClient,
    private store: Store,
  ) {
    this.initAuth();
  }

  /**
   * Initialize auth state by fetching current user if token exists.
   * If token is invalid, user is logged out.
   */
  private initAuth(): void {
    const token = this.getToken();

    if (!token) {
      this._authInitialized.set(true);
      return;
    }

    this.http
      .get<User>(`${environment.apiUrl}/user/profile`)
      .pipe(
        take(1),
        tap((user) => this._currentUser.set(user)),
        catchError(() => {
          this.logout();
          return EMPTY;
        }),
      )
      .subscribe({
        complete: () => this._authInitialized.set(true),
      });
  }

  /**
   * Register a new user
   */
  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request).pipe(
      tap((response) => this.handleAuthSuccess(response)),
      catchError((error) => this.handleError(error)),
    );
  }

  /**
   * Login an existing user
   */
  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap((response) => this.handleAuthSuccess(response)),
      catchError((error) => this.handleError(error)),
    );
  }

  /**
   * Logout the current user.
   * Component is responsible for navigation after logout.
   */
  logout(): void {
    this.store.remove(Auth.TOKEN_KEY);
    this._currentUser.set(null);
  }

  /**
   * Get the stored authentication token
   */
  getToken(): string | null {
    return this.store.get(Auth.TOKEN_KEY);
  }

  /**
   * Get current user profile with subscriptions
   */
  getProfile(): Observable<User> {
    return this.http
      .get<User>(`${environment.apiUrl}/user/profile`)
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * Update user profile (username, email, password)
   */
  updateProfile(request: UpdateProfileRequest): Observable<User> {
    return this.http.put<User>(`${environment.apiUrl}/user/profile`, request).pipe(
      tap((user) => this._currentUser.set(user)),
      catchError((error) => this.handleError(error)),
    );
  }

  /**
   * Handle successful authentication (login or register)
   */
  private handleAuthSuccess(response: AuthResponse): void {
    this.store.set(Auth.TOKEN_KEY, response.token);
    this._currentUser.set(response.user);
  }

  /**
   * Handle HTTP errors from authentication and profile requests
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      if (error.error?.errors && typeof error.error.errors === 'object') {
        const validationErrors = Object.values(error.error.errors) as string[];
        errorMessage = validationErrors.join('\n');
      } else if (error.error?.message) {
        errorMessage = error.error.message;
      }
    }

    return throwError(() => new Error(errorMessage));
  }
}
