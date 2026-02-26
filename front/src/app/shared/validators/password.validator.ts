import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Validates that password contains at least:
 * - 8 characters
 * - One digit
 * - One lowercase letter
 * - One uppercase letter
 * - One special character (@#$%^&+=!)
 */
export function passwordValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) {
      return null;
    }

    const hasNumber = /[0-9]/.test(value);
    const hasLower = /[a-z]/.test(value);
    const hasUpper = /[A-Z]/.test(value);
    const hasSpecial = /[@#$%^&+=!]/.test(value);
    const hasMinLength = value.length >= 8;

    const passwordValid = hasNumber && hasLower && hasUpper && hasSpecial && hasMinLength;

    return passwordValid ? null : { passwordStrength: true };
  };
}
