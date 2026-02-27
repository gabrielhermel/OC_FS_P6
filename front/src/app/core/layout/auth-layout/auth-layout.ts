import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-auth-layout',
  imports: [RouterOutlet, Navbar, MatIconModule, MatButtonModule],
  templateUrl: './auth-layout.html',
  styleUrl: './auth-layout.scss',
})
export class AuthLayout {
  private router = inject(Router);

  navigateToLanding(): void {
    this.router.navigate(['/']);
  }
}
