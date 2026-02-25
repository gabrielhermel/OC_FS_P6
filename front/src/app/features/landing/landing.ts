import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

/**
 * Landing page component.
 * Displays MDD logo and navigation buttons for login/registration.
 */
@Component({
  selector: 'app-landing',
  imports: [MatButtonModule],
  templateUrl: './landing.html',
  styleUrl: './landing.scss',
})
export class Landing {
  constructor(private router: Router) {}

  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }
}
