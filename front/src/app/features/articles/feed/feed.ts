import { Component, signal, inject, OnInit, DestroyRef } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { ArticlesService } from '../services/articles';
import { Article } from '../../../shared/models/article';
import { MatSnackBar } from '@angular/material/snack-bar';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

/**
 * Articles feed page component.
 * Displays user's subscribed articles with sorting and creation options.
 */
@Component({
  standalone: true,
  selector: 'app-feed',
  imports: [CommonModule, MatButtonModule],
  templateUrl: './feed.html',
  styleUrl: './feed.scss',
})
export class Feed implements OnInit {
  private articlesService = inject(ArticlesService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  private destroyRef = inject(DestroyRef);

  articles = signal<Article[]>([]);
  sortOrder = signal<'asc' | 'desc'>('desc');
  isLoading = signal(true);
  hasError = signal(false);

  /**
   * Load articles on component initialization
   */
  ngOnInit(): void {
    this.loadArticles();
  }

  /**
   * Load articles from API with current sort order
   */
  loadArticles(): void {
    this.isLoading.set(true);
    this.hasError.set(false);
    this.articlesService
      .getFeed(this.sortOrder())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (articles) => {
          this.articles.set(articles);
          this.isLoading.set(false);
        },
        error: (error) => {
          console.error('Error loading articles:', error);
          this.isLoading.set(false);
          this.hasError.set(true);

          let message = 'Erreur lors du chargement des articles';

          if (error instanceof Error) {
            message = error.message;
          } else if (error?.status === 0) {
            message = 'Impossible de contacter le serveur';
          } else if (error?.status === 404) {
            message = 'Articles introuvables';
          }

          this.snackBar.open(message, 'Fermer', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['error-snackbar'],
          });
        },
      });
  }

  /**
   * Toggle sort order between ascending and descending
   */
  toggleSort(): void {
    this.sortOrder.set(this.sortOrder() === 'asc' ? 'desc' : 'asc');
    this.loadArticles();
  }

  /**
   * Navigate to article creation page
   */
  navigateToCreate(): void {
    this.router.navigate(['/articles/create']);
  }

  /**
   * Navigate to article detail page
   */
  navigateToArticle(id: number): void {
    this.router.navigate(['/articles', id]);
  }

  /**
   * Format ISO date string to dd/mm/yyyy
   */
  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  /**
   * Truncate content to specified number of lines
   */
  truncateContent(content: string, maxLines: number): string {
    const lines = content.split('\n');
    if (lines.length <= maxLines) {
      return content;
    }
    return lines.slice(0, maxLines).join('\n') + '...';
  }
}
