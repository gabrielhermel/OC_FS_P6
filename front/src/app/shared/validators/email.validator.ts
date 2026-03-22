import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Email validator requiring domain with TLD
 */
export function emailValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) {
      return null;
    }

    const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    return emailPattern.test(value) ? null : { email: true };
  };
}
