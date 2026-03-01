import { Component, signal, inject, DestroyRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { Auth } from '../../../core/services/auth';
import { LoginRequest } from '../../../shared/models/auth.model';

/**
 * User login page component.
 * Handles user authentication with form validation.
 */
@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private fb = inject(FormBuilder);
  private auth = inject(Auth);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  private destroyRef = inject(DestroyRef);

  /** Prevents duplicate submissions and manages submit button disabled state */
  readonly isSubmitting = signal(false);

  /** Strongly typed reactive form. */
  readonly loginForm = this.fb.nonNullable.group({
    usernameOrEmail: this.fb.nonNullable.control<string>('', [Validators.required]),
    password: this.fb.nonNullable.control<string>('', [Validators.required]),
  });

  // Getters keep template strongly typed
  get usernameOrEmail() {
    return this.loginForm.controls.usernameOrEmail;
  }

  get password() {
    return this.loginForm.controls.password;
  }

  /** Handles login submission. */
  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    if (this.isSubmitting()) {
      return;
    }

    this.isSubmitting.set(true);

    const request: LoginRequest = this.loginForm.getRawValue();

    this.auth
      .login(request)
      .pipe(
        finalize(() => this.isSubmitting.set(false)),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: () => {
          this.router.navigate(['/articles']);
        },
        error: (error: unknown) => {
          const message = error instanceof Error ? error.message : 'Une erreur est survenue';

          this.snackBar.open(message, 'Fermer', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['error-snackbar'],
          });
        },
      });
  }
}
