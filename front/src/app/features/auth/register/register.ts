import { Component, signal, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
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
  registerForm: FormGroup;
  isSubmitting = signal(false);

  passwordTooltip =
    'Le mot de passe doit contenir :\n' +
    '\xa0\xa0• Au moins 8 caractères\n' +
    '\xa0\xa0• Un chiffre\n' +
    '\xa0\xa0• Une lettre minuscule\n' +
    '\xa0\xa0• Une lettre majuscule\n' +
    '\xa0\xa0• Un caractère spécial (@#$%^&+=!)';

  private snackBar = inject(MatSnackBar);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), passwordValidator()]],
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid || this.isSubmitting()) {
      return;
    }

    this.isSubmitting.set(true);

    const request: RegisterRequest = this.registerForm.value;

    this.authService.register(request).subscribe({
      next: () => {
        this.router.navigate(['/articles']);
      },
      error: (error: Error) => {
        this.snackBar.open(error.message, 'Fermer', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
        });
        this.isSubmitting.set(false);
      },
    });
  }

  navigateToLanding(): void {
    this.router.navigate(['/']);
  }
}
