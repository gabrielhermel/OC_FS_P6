import { Component, signal, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { AuthService } from '../../../core/services/auth';
import { RegisterRequest } from '../../../shared/models/auth.model';
import { passwordValidator } from '../../../shared/validators/password.validator';

/**
 * User registration page component.
 * Handles new user account creation with form validation.
 */
@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  readonly isSubmitting = signal(false);

  readonly registerForm = this.fb.nonNullable.group({
    username: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8), passwordValidator()]],
  });

  passwordTooltip =
    'Le mot de passe doit contenir :\n' +
    '\xa0\xa0• Au moins 8 caractères\n' +
    '\xa0\xa0• Un chiffre\n' +
    '\xa0\xa0• Une lettre minuscule\n' +
    '\xa0\xa0• Une lettre majuscule\n' +
    '\xa0\xa0• Un caractère spécial (@#$%^&+=!)';

  // Getters for cleaner template access
  get username() {
    return this.registerForm.controls.username;
  }

  get email() {
    return this.registerForm.controls.email;
  }

  get password() {
    return this.registerForm.controls.password;
  }

  onSubmit(): void {
    if (this.registerForm.invalid || this.isSubmitting()) {
      return;
    }

    this.isSubmitting.set(true);

    const request: RegisterRequest = this.registerForm.getRawValue();

    this.authService
      .register(request)
      .pipe(finalize(() => this.isSubmitting.set(false)))
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

  navigateToLanding(): void {
    this.router.navigate(['/']);
  }
}
