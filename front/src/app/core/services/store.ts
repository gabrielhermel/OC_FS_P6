import { Injectable } from '@angular/core';

/**
 * Abstract storage service for managing browser storage.
 * Allows easy swapping between localStorage, sessionStorage, or mocking for tests.
 */
@Injectable({
  providedIn: 'root',
})
export class Store {
  private storage: Storage | null =
    typeof window !== 'undefined' ? localStorage : null;

  /**
   * Store a value
   */
  set(key: string, value: string): void {
    this.storage?.setItem(key, value);
  }

  /**
   * Retrieve a value
   */
  get(key: string): string | null {
    return this.storage?.getItem(key) ?? null;
  }

  /**
   * Remove a value
   */
  remove(key: string): void {
    this.storage?.removeItem(key);
  }

  /**
   * Clear all storage
   */
  clear(): void {
    this.storage?.clear();
  }

  /**
   * Check if a key exists
   */
  has(key: string): boolean {
    return this.storage?.getItem(key) !== null;
  }
}