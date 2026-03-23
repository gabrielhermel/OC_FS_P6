import { Component, signal, inject, DestroyRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { Auth } from '../../../core/services/auth';
import { RegisterRequest } from '../../../shared/models/auth';
import { emailValidator } from '../../../shared/validators/email.validator';
import { passwordValidator } from '../../../shared/validators/password.validator';

/**
 * User registration page component.
 * Handles new user account creation with form validation.
 */
@Component({
  standalone: true,
  selector: 'app-register',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTooltipModule,
    MatIconModule,
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  private fb = inject(FormBuilder);
  private auth = inject(Auth);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  private destroyRef = inject(DestroyRef);

  /** Prevents duplicate submissions and manages submit button disabled state */
  readonly isSubmitting = signal(false);
  readonly hidePassword = signal(true);

  /** Strongly typed reactive form. */
  readonly registerForm = this.fb.nonNullable.group({
    username: this.fb.nonNullable.control<string>('', [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(50),
    ]),
    email: this.fb.nonNullable.control<string>('', [Validators.required, emailValidator()]),
    password: this.fb.nonNullable.control<string>('', [
      Validators.required,
      Validators.minLength(8),
      passwordValidator(),
    ]),
  });

  passwordTooltip =
    'Le mot de passe doit contenir :\n' +
    '\xa0\xa0• Au moins 8 caractères\n' +
    '\xa0\xa0• Un chiffre\n' +
    '\xa0\xa0• Une lettre minuscule\n' +
    '\xa0\xa0• Une lettre majuscule\n' +
    '\xa0\xa0• Un caractère spécial (@#$%^&+=!)';

  // Getters keep template strongly typed
  get username() {
    return this.registerForm.controls.username;
  }

  get email() {
    return this.registerForm.controls.email;
  }

  get password() {
    return this.registerForm.controls.password;
  }

  /** Handles registration submission. */
  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    if (this.isSubmitting()) {
      return;
    }

    this.isSubmitting.set(true);

    const request: RegisterRequest = this.registerForm.getRawValue();

    this.auth
      .register(request)
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
