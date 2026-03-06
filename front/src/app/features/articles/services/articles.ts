import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Article } from '../../../shared/models/article.model';

@Injectable({
  providedIn: 'root',
})
export class Articles {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/articles`;

  /**
   * Get article feed sorted by creation date
   * @param sort 'asc' for oldest first, 'desc' for newest first
   */
  getFeed(sort: 'asc' | 'desc' = 'desc'): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}?sort=${sort}`);
  }

  /**
   * Get article by ID
   */
  getById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/${id}`);
  }
}
