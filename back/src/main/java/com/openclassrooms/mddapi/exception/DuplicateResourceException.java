package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when attempting to create a resource that already exists. For example,
 * registering with a username or email that's already taken.
 */
public class DuplicateResourceException extends RuntimeException {

  public DuplicateResourceException(String message) {
    super(message);
  }
}