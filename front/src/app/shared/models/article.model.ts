import { Comment } from './comment.model';

export interface Article {
  id: number;
  title: string;
  content: string;
  topicId: number;
  topicName: string;
  authorId: number;
  authorName: string;
  createdAt: string;
  updatedAt: string;
  comments?: Comment[];
}
