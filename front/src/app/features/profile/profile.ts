import { Component, signal, inject, OnInit, DestroyRef } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';
import { Auth } from '../../core/services/auth';
import { TopicsService } from '../topics/services/topics';
import { Topic } from '../../shared/models/topic';
import { UpdateProfileRequest } from '../../shared/models/update-profile';
import { passwordValidator } from '../../shared/validators/password.validator';
import { MatIconModule } from '@angular/material/icon';
import { emailValidator } from '../../shared/validators/email.validator';

/**
 * User profile page component.
 * Allows users to update their profile and manage topic subscriptions.
 */
@Component({
  standalone: true,
  selector: 'app-profile',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTooltipModule,
    MatIconModule,
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {
  private fb = inject(FormBuilder);
  private auth = inject(Auth);
  private topicsService = inject(TopicsService);
  private snackBar = inject(MatSnackBar);
  private destroyRef = inject(DestroyRef);

  subscriptions = signal<Topic[]>([]);
  isLoadingProfile = signal(true);
  isSubmitting = signal(false);
  hidePassword = signal(true);

  profileForm = this.fb.nonNullable.group({
    username: this.fb.nonNullable.control<string>(''),
    email: this.fb.nonNullable.control<string>('', [emailValidator()]),
    password: this.fb.nonNullable.control<string>('', [
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

  /**
   * Whether submit button should be enabled
   */
  get canSubmit(): boolean {
    const hasValue = !!(this.username.value || this.email.value || this.password.value);
    return hasValue && !this.isSubmitting();
  }

  get username() {
    return this.profileForm.controls.username;
  }

  get email() {
    return this.profileForm.controls.email;
  }

  get password() {
    return this.profileForm.controls.password;
  }

  /**
   * Load user profile on component initialization
   */
  ngOnInit(): void {
    this.loadProfile();
  }

  /**
   * Load current user profile with subscriptions
   */
  loadProfile(): void {
    this.isLoadingProfile.set(true);
    this.auth
      .getProfile()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (user) => {
          this.subscriptions.set(user.subscriptions || []);
          this.profileForm.patchValue({
            username: user.username,
            email: user.email,
            password: '',
          });
          this.isLoadingProfile.set(false);
        },
        error: (error) => {
          console.error('Error loading profile:', error);
          this.isLoadingProfile.set(false);
          this.snackBar.open('Erreur lors du chargement du profil', 'Fermer', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['error-snackbar'],
          });
        },
      });
  }

  /**
   * Submit profile update
   */
  onSubmit(): void {
    // Get current user for comparison
    const currentUser = this.auth.currentUser();
    if (!currentUser) {
      this.snackBar.open('Erreur: utilisateur non connecté', 'Fermer', {
        duration: 5000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
        panelClass: ['error-snackbar'],
      });
      return;
    }

    // Check if any field has actually changed
    const usernameChanged = this.username.value && this.username.value !== currentUser.username;
    const emailChanged = this.email.value && this.email.value !== currentUser.email;
    const passwordChanged = this.password.value && this.password.value.trim() !== '';

    // If nothing changed, show message and return
    if (!usernameChanged && !emailChanged && !passwordChanged) {
      this.snackBar.open('Aucune modification détectée', 'Fermer', {
        duration: 5000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
        panelClass: ['notify-snackbar'],
      });
      return;
    }

    // Client-side validation for changed fields
    if (emailChanged && this.email.invalid) {
      this.snackBar.open("L'email doit être valide", 'Fermer', {
        duration: 5000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
        panelClass: ['error-snackbar'],
      });
      // Revert to original value
      this.email.setValue(currentUser.email);
      return;
    }

    if (passwordChanged && this.password.invalid) {
      this.snackBar.open("Le mot de passe n'est pas valide", 'Fermer', {
        duration: 5000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
        panelClass: ['error-snackbar'],
      });
      // Clear password field
      this.password.setValue('');
      return;
    }

    if (!this.canSubmit) {
      return;
    }

    this.isSubmitting.set(true);

    const request: UpdateProfileRequest = {};
    if (usernameChanged) request.username = this.username.value;
    if (emailChanged) request.email = this.email.value;
    if (passwordChanged) request.password = this.password.value;

    this.auth
      .updateProfile(request)
      .pipe(
        finalize(() => this.isSubmitting.set(false)),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (user) => {
          // Reset form with new values
          this.profileForm.patchValue({
            username: user.username,
            email: user.email,
            password: '',
          });
          this.snackBar.open('Profil mis à jour avec succès', 'Fermer', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['notify-snackbar']
          });
        },
        error: (error) => {
          console.error('Error updating profile:', error);
          const message =
            error instanceof Error ? error.message : 'Erreur lors de la mise à jour du profil';

          this.snackBar.open(message, 'Fermer', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['error-snackbar'],
          });

          this.profileForm.patchValue({
            username: currentUser.username,
            email: currentUser.email,
            password: '',
          });
        },
      });
  }

  /**
   * Unsubscribe from a topic
   */
  onUnsubscribe(topic: Topic): void {
    this.topicsService
      .unsubscribe(topic.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.subscriptions.update((subs) => subs.filter((t) => t.id !== topic.id));
        },
        error: (error) => {
          console.error('Error unsubscribing:', error);
          const message = error instanceof Error ? error.message : 'Erreur lors du désabonnement';

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
