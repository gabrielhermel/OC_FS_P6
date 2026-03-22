import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Topic } from '../../../shared/models/topic';

@Injectable({
  providedIn: 'root',
})
export class TopicsService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/themes`;
  private readonly subscriptionsUrl = `${environment.apiUrl}/subscriptions`;

  /**
   * Get all topics with subscription status
   */
  getAll(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.apiUrl);
  }

  /**
   * Subscribe to a topic
   */
  subscribe(topicId: number): Observable<void> {
    return this.http.post<void>(`${this.subscriptionsUrl}/${topicId}`, {});
  }

  /**
   * Unsubscribe from a topic
   */
  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`${this.subscriptionsUrl}/${topicId}`);
  }
}
