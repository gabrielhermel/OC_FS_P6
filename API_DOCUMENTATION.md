# MDD API Documentation

Complete API reference for the MDD (Monde de Dév) backend.

**Base URL:** `http://localhost:8080/api`

**Authentication:** All endpoints except `/api/auth/**` require a JWT token:
```
Authorization: Bearer <token>
```

---

## Table of Contents

- [Authentication](#authentication)
  - [Register](#register)
  - [Login](#login)
- [User](#user)
  - [Get Profile](#get-profile)
  - [Update Profile](#update-profile)
- [Topics](#topics)
  - [Get All Topics](#get-all-topics)
- [Subscriptions](#subscriptions)
  - [Subscribe to Topic](#subscribe-to-topic)
  - [Unsubscribe from Topic](#unsubscribe-from-topic)
- [Articles](#articles)
  - [Get Article Feed](#get-article-feed)
  - [Get Article Details](#get-article-details)
  - [Create Article](#create-article)
- [Comments](#comments)
  - [Create Comment](#create-comment)
- [Error Responses](#error-responses)
- [HTTP Status Codes](#http-status-codes)

---

## Authentication

### Register

**POST** `/api/auth/register`

**Request:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123!"
}
```

**Password Requirements:**
- Minimum 8 characters
- At least one digit, lowercase letter, uppercase letter, and special character (@#$%^&+=!)

**Response (201):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com"
  }
}
```

---

### Login

**POST** `/api/auth/login`

**Request:**
```json
{
  "usernameOrEmail": "johndoe",
  "password": "SecurePass123!"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com"
  }
}
```

---

## User

### Get Profile

**GET** `/api/user/profile`

**Response (200):**
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "doe.john@example.com",
  "subscriptions": [
    {
      "id": 2,
      "name": "Java",
      "description": "Discussions sur Java, Spring, JPA...",
      "subscribed": true
    }
  ]
}
```

---

### Update Profile

**PUT** `/api/user/profile`

**Request:** (all fields optional)
```json
{
  "username": "john_updated",
  "email": "newemail@example.com",
  "password": "NewSecurePass123!"
}
```

**Response:**
- `200` with updated profile if changes were made
- `204` if no changes (all fields identical to current values)

---

## Topics

### Get All Topics

**GET** `/api/themes`

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "JavaScript",
    "description": "Tout sur JavaScript...",
    "subscribed": false
  },
  {
    "id": 2,
    "name": "Java",
    "description": "Discussions sur Java...",
    "subscribed": true
  }
]
```

---

## Subscriptions

### Subscribe to Topic

**POST** `/api/subscriptions/{topicId}`

**Response:**
- `201` - Newly subscribed
- `204` - Already subscribed (idempotent)

---

### Unsubscribe from Topic

**DELETE** `/api/subscriptions/{topicId}`

**Response:**
- `204` - Successfully unsubscribed

---

## Articles

### Get Article Feed

**GET** `/api/articles?sort={order}`

**Query Parameters:**
- `sort` (optional): `asc` (oldest first) or `desc` (newest first, default)

**Response (200):**
```json
[
  {
    "id": 12,
    "title": "Test Article",
    "content": "Testing transactions",
    "topicId": 2,
    "topicName": "Java",
    "authorId": 1,
    "authorName": "johndoe",
    "createdAt": "2026-02-20T13:15:36",
    "updatedAt": "2026-02-20T13:15:36"
  }
]
```

---

### Get Article Details

**GET** `/api/articles/{id}`

**Response (200):**
```json
{
  "id": 1,
  "title": "Les nouveautés d'ES2024",
  "content": "ES2024 apporte plusieurs nouvelles fonctionnalités...",
  "topicId": 1,
  "topicName": "JavaScript",
  "authorId": 1,
  "authorName": "johndoe",
  "createdAt": "2026-02-09T14:16:08",
  "updatedAt": "2026-02-11T14:16:08",
  "comments": [
    {
      "id": 1,
      "content": "Excellent article !",
      "authorId": 2,
      "authorName": "janesmith",
      "createdAt": "2026-02-10T14:16:08"
    }
  ]
}
```

---

### Create Article

**POST** `/api/articles`

**Request:**
```json
{
  "topicId": 1,
  "title": "Understanding Spring Data JPA",
  "content": "Spring Data JPA provides a powerful abstraction..."
}
```

**Note:** Author and timestamps are set automatically.

**Response (201):**
```json
{
  "id": 13,
  "title": "Understanding Spring Data JPA",
  "content": "Spring Data JPA provides a powerful abstraction...",
  "topicId": 1,
  "topicName": "JavaScript",
  "authorId": 1,
  "authorName": "johndoe",
  "createdAt": "2026-02-20T16:36:24.185781",
  "updatedAt": "2026-02-20T16:36:24.185805",
  "comments": []
}
```

---

## Comments

### Create Comment

**POST** `/api/articles/{articleId}/comments`

**Request:**
```json
{
  "content": "This is a very insightful article. Thanks for sharing!"
}
```

**Note:** Author and timestamp are set automatically.

**Response (201):**
```json
{
  "id": 13,
  "content": "This is a very insightful article. Thanks for sharing!",
  "authorId": 1,
  "authorName": "johndoe",
  "createdAt": "2026-02-20T16:36:24.211069"
}
```

---

## Error Responses

All errors follow this format:
```json
{
  "status": 400,
  "message": "Échec de la validation",
  "errors": {
    "password": "Le mot de passe doit contenir au moins 8 caractères",
    "email": "L'email doit être valide"
  },
  "timestamp": "2026-02-20T16:28:06.373373941"
}
```

## HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Resource created |
| 204 | Success with no content |
| 400 | Invalid request / validation errors |
| 401 | Missing or invalid authentication |
| 404 | Resource not found |
| 409 | Duplicate resource (username/email taken) |
| 500 | Server error |