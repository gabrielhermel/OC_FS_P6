import { Component, signal, inject, OnInit, DestroyRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TopicsService } from '../services/topics';
import { Topic } from '../../../shared/models/topic';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

/**
 * Topics list page component.
 * Displays all topics with subscription functionality.
 */
@Component({
  standalone: true,
  selector: 'app-topics',
  imports: [CommonModule, MatButtonModule],
  templateUrl: './topics.html',
  styleUrl: './topics.scss',
})
export class Topics implements OnInit {
  private topicsService = inject(TopicsService);
  private snackBar = inject(MatSnackBar);
  private destroyRef = inject(DestroyRef);

  readonly topics = signal<Topic[]>([]);
  readonly isLoading = signal(true);
  readonly hasError = signal(false);

  /**
   * Load topics on component initialization
   */
  ngOnInit(): void {
    this.loadTopics();
  }

  /**
   * Load topics from API, sorted by subscription status
   */
  loadTopics(): void {
    this.isLoading.set(true);
    this.hasError.set(false);
    this.topicsService
      .getAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (topics) => {
          const sorted = this.sortTopics(topics);
          this.topics.set(sorted);
          this.isLoading.set(false);
        },
        error: (error) => {
          console.error('Error loading topics:', error);
          this.isLoading.set(false);
          this.hasError.set(true);

          let message = 'Erreur lors du chargement des thèmes';

          if (error instanceof Error) {
            message = error.message;
          } else if (error?.status === 0) {
            message = 'Impossible de contacter le serveur';
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
   * Sort topics: unsubscribed first, then subscribed
   */
  private sortTopics(topics: Topic[]): Topic[] {
    return [...topics].sort((a, b) => {
      if (a.subscribed === b.subscribed) return 0;
      return a.subscribed ? 1 : -1;
    });
  }

  /**
   * Subscribe to a topic
   */
  onSubscribe(topic: Topic): void {
    if (topic.subscribed) return;

    this.topicsService
      .subscribe(topic.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          const updatedTopics = this.topics().map((t) =>
            t.id === topic.id ? { ...t, subscribed: true } : t,
          );
          this.topics.set(updatedTopics);
        },
        error: (error) => {
          console.error('Error subscribing to topic:', error);

          const message =
            error instanceof Error ? error.message : "Erreur lors de l'abonnement au thème";

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
   * Truncate description to specified number of lines
   */
  truncateDescription(description: string, maxLines: number): string {
    const lines = description.split('\n');
    if (lines.length <= maxLines) {
      return description;
    }
    return lines.slice(0, maxLines).join('\n') + '...';
  }
}
