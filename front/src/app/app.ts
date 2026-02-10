import { Component, OnInit, OnDestroy, signal, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { TopicService } from './services/topic.service';
import { Topic } from './models/topic.model';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit, OnDestroy {
  protected readonly title = signal('MDD Topics');
  protected topics = signal<Topic[]>([]);
  protected loading = signal(true);
  protected error = signal<string | null>(null);

  private topicService = inject(TopicService);
  private topicsSubscription?: Subscription;

  ngOnInit(): void {
    this.topicsSubscription = this.topicService.getAllTopics().subscribe({
      next: (data) => {
        this.topics.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load topics');
        this.loading.set(false);
        console.error('Error loading topics:', err);
      },
    });
  }

  ngOnDestroy(): void {
    this.topicsSubscription?.unsubscribe();
  }
}
