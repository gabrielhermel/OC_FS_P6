import { Component, signal, inject, OnInit, DestroyRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import { ArticlesService } from '../services/articles';
import { TopicsService } from '../../topics/services/topics';
import { Topic } from '../../../shared/models/topic';
import { CreateArticleRequest } from '../../../shared/models/create-article';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

/**
 * Article creation page component.
 * Allows users to create new articles with topic selection.
 */
@Component({
  standalone: true,
  selector: 'app-create-article',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './create.html',
  styleUrl: './create.scss',
})
export class CreateArticle implements OnInit {
  private fb = inject(FormBuilder);
  private articlesService = inject(ArticlesService);
  private topicsService = inject(TopicsService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  private destroyRef = inject(DestroyRef);

  topics = signal<Topic[]>([]);
  isLoadingTopics = signal(true);
  isSubmitting = signal(false);

  articleForm = this.fb.nonNullable.group({
    topicId: this.fb.nonNullable.control<number | null>(null, [Validators.required]),
    title: this.fb.nonNullable.control<string>('', [Validators.required]),
    content: this.fb.nonNullable.control<string>('', [Validators.required]),
  });

  /**
   * Load topics on component initialization
   */
  ngOnInit(): void {
    this.loadTopics();
  }

  /**
   * Load topics for dropdown
   */
  loadTopics(): void {
    this.isLoadingTopics.set(true);
    this.topicsService
      .getAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (topics) => {
          this.topics.set(topics);
          this.isLoadingTopics.set(false);
        },
        error: (error) => {
          console.error('Error loading topics:', error);
          this.isLoadingTopics.set(false);
          this.snackBar.open('Erreur lors du chargement des thèmes', 'Fermer', {
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
   * Submit article creation
   */
  onSubmit(): void {
    if (this.articleForm.invalid) {
      this.articleForm.markAllAsTouched();
      return;
    }

    if (this.isSubmitting()) {
      return;
    }

    this.isSubmitting.set(true);

    const request: CreateArticleRequest = {
      topicId: this.articleForm.value.topicId!,
      title: this.articleForm.value.title!,
      content: this.articleForm.value.content!,
    };

    this.articlesService
      .createArticle(request)
      .pipe(
        finalize(() => this.isSubmitting.set(false)),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (article) => {
          this.router.navigate(['/articles', article.id]);
        },
        error: (error) => {
          console.error('Error creating article:', error);
          const message =
            error instanceof Error ? error.message : "Erreur lors de la création de l'article";

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
