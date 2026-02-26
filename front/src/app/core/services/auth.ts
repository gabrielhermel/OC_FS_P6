import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../shared/models/auth.model';
import { User } from '../../shared/models/user.model';

/**
 * Authentication service handling user registration, login, logout, and token management.
 */
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  /** Current authenticated user (null if not logged in) */
  currentUser = signal<User | null>(null);

  /** Whether a user is currently authenticated */
  isAuthenticated = signal(false);

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {
    this.initializeAuth();
  }

  /**
   * Initialize authentication state from stored token
   */
  private initializeAuth(): void {
    const token = this.getToken();
    if (token) {
      // TODO: Validate token and fetch user data
      this.isAuthenticated.set(true);
    }
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
   * Logout the current user
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.currentUser.set(null);
    this.isAuthenticated.set(false);
    this.router.navigate(['/']);
  }

  /**
   * Get the stored authentication token
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Handle successful authentication (login or register)
   */
  private handleAuthSuccess(response: AuthResponse): void {
    localStorage.setItem(this.TOKEN_KEY, response.token);
    this.currentUser.set(response.user);
    this.isAuthenticated.set(true);
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

    console.error('Authentication error:', error);
    return throwError(() => new Error(errorMessage));
  }
}
