import { Topic } from "./topic";

export interface User {
  id: number;
  username: string;
  email: string;
  subscriptions?: Topic[];
}
