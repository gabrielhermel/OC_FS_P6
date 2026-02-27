import { Component, inject, Input, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

/**
 * Navbar component with two variants:
 * - 'public': Displays only the logo (for login/register pages)
 * - 'secured': Displays logo, navigation links, and user avatar (for authenticated pages)
 */
@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, MatIconModule, MatButtonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  @Input() variant: 'public' | 'secured' = 'secured';

  private router = inject(Router);

  /** Controls mobile menu visibility */
  protected menuOpen = signal(false);

  /** Toggle mobile menu open/closed state */
  toggleMenu(): void {
    this.menuOpen.update((v) => !v);
  }

  /** Close mobile menu (used by backdrop and navigation actions) */
  closeMenu(): void {
    this.menuOpen.set(false);
  }

  logout(): void {
    this.closeMenu();
    // TODO: Implement logout logic with AuthService
    this.router.navigate(['/']);
  }
}
