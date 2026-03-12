import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Article } from '../../../shared/models/article';
import { CreateCommentRequest } from '../../../shared/models/create-comment';
import { Comment } from '../../../shared/models/comment';
import { CreateArticleRequest } from '../../../shared/models/create-article';

@Injectable({
  providedIn: 'root',
})
export class ArticlesService {
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

  /**
   * Create a comment on an article
   */
  createComment(articleId: number, request: CreateCommentRequest): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/${articleId}/comments`, request);
  }

  /**
   * Create a new article
   */
  createArticle(request: CreateArticleRequest): Observable<Article> {
    return this.http.post<Article>(this.apiUrl, request);
  }
}
