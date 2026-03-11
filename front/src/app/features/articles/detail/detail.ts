import { Component, signal, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ArticlesService } from '../services/articles';
import { Article } from '../../../shared/models/article.model';

/**
 * Article detail page component.
 * Displays full article with comments and comment creation.
 */
@Component({
  standalone: true,
  selector: 'app-detail',
  imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatIconModule],
  templateUrl: './detail.html',
  styleUrl: './detail.scss',
})
export class Detail implements OnInit {
  private articlesService = inject(ArticlesService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  readonly TEXTAREA_ROWS = 5;

  article = signal<Article | null>(null);
  isLoading = signal(true);
  hasError = signal(false);
  isSubmitting = signal(false);

  commentForm = this.fb.nonNullable.group({
    content: this.fb.nonNullable.control<string>('', [Validators.required]),
  });

  /**
   * Load article on component initialization
   */
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadArticle(id);
    }
  }

  /**
   * Load article from API
   */
  loadArticle(id: number): void {
    this.isLoading.set(true);
    this.hasError.set(false);
    this.articlesService.getById(id).subscribe({
      next: (article) => {
        this.article.set(article);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error loading article:', error);
        this.isLoading.set(false);
        this.hasError.set(true);

        let message = "Erreur lors du chargement de l'article";

        if (error instanceof Error) {
          message = error.message;
        } else if (error?.status === 0) {
          message = 'Impossible de contacter le serveur';
        } else if (error?.status === 404) {
          message = 'Article introuvable';
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
   * Navigate back to articles feed
   */
  goBack(): void {
    this.router.navigate(['/articles']);
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
   * Submit a new comment
   */
  onSubmitComment(): void {
    if (this.commentForm.invalid || this.isSubmitting() || !this.article()) {
      return;
    }

    this.isSubmitting.set(true);

    const request = this.commentForm.getRawValue();
    const articleId = this.article()!.id;

    this.articlesService.createComment(articleId, request).subscribe({
      next: (newComment) => {
        const currentArticle = this.article()!;
        const updatedArticle = {
          ...currentArticle,
          comments: [...(currentArticle.comments || []), newComment],
        };
        this.article.set(updatedArticle);
        this.commentForm.reset();
        this.isSubmitting.set(false);
      },
      error: (error) => {
        console.error('Error creating comment:', error);
        this.isSubmitting.set(false);

        const message =
          error instanceof Error ? error.message : 'Erreur lors de la création du commentaire';

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
