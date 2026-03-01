import { Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, tap, catchError, throwError, EMPTY, take } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../shared/models/auth.model';
import { User } from '../../shared/models/user.model';
import { Store } from './store';

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

    this.fetchCurrentUser().subscribe({
      complete: () => this._authInitialized.set(true),
    });
  }

  /**
   * Fetch current user profile from backend.
   * If request fails (expired/invalid token), logout.
   */
  private fetchCurrentUser(): Observable<User> {
    return this.http.get<User>(`${environment.apiUrl}/user/profile`).pipe(
      take(1),
      tap((user) => this._currentUser.set(user)),
      catchError(() => {
        this.logout();
        return EMPTY;
      }),
    );
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
   * Handle successful authentication (login or register)
   */
  private handleAuthSuccess(response: AuthResponse): void {
    this.store.set(Auth.TOKEN_KEY, response.token);
    this._currentUser.set(response.user);
  }

  /**
   * Handle HTTP errors from authentication requests
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      // Backend error
      errorMessage = error.error?.message || errorMessage;
    }

    return throwError(() => new Error(errorMessage));
  }
}
