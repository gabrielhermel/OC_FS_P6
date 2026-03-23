# MDD (Monde de Dév) - Developer Social Network

Full-stack social network application for developers. Users can subscribe to programming topics, create articles, and engage through comments.

## Table of Contents

- [Clone the Repository](#clone-the-repository)
- [Back-End](#back-end)
  - [Prerequisites](#backend-prerequisites)
  - [Database Setup](#database-setup)
  - [Environment Configuration](#backend-env-config)
  - [Running the Back-End](#running-the-back-end)
  - [API Endpoints](#api-endpoints)
- [Front-End](#front-end)
  - [Prerequisites](#frontend-prerequisites)
  - [Frontend Setup](#frontend-setup)
  - [Environment Configuration](#frontend-env-config)
  - [Running the Front-End](#running-the-front-end)
- [Project Structure](#project-structure)

---

## Clone the Repository

Navigate to the directory where the project should be located. Follow the instructions on GitHub for cloning the repository using HTTPS.

## Back-End

<a id="backend-prerequisites"></a>

### Prerequisites

- **MySQL 8.0+**
- **Git**
- **curl** (required for SDKMAN installation)
- **openssl** (for generating JWT secret)
- **WSL** (if running on Windows)

**Note for macOS users:** All bash commands should work natively.

**Verify installations:**

Check MySQL:

```bash
mysql --version
```

Check Git:

```bash
git --version
```

Check curl:

```bash
curl --version
```

Check openssl:

```bash
openssl version
```

All commands should return version information. If any command is not found, the corresponding tool needs to be installed before proceeding.

---

### Database Setup

**Create the database and user:**

```bash
mysql -u root -p
```

```sql
CREATE DATABASE mdd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'mdd_user'@'localhost' IDENTIFIED BY 'mdd_password';
GRANT ALL PRIVILEGES ON mdd.* TO 'mdd_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

**Customizable values:**

- `mdd` - database name (any name can be used)
- `mdd_user` - database username (any username can be used)
- `mdd_password` - database password (a secure password should be used)

**Note:** These values will be needed when configuring the backend.

**Run the schema script:**

```bash
mysql -u mdd_user -p mdd < schema.sql
```

The username and database name should be replaced with the chosen values.

**Seed sample topics:**

**Note:** Topics are required for the application to function properly. Users cannot create topics through the interface.

```bash
mysql -u mdd_user -p mdd
```

```sql
INSERT INTO topic (name, description) VALUES
('JavaScript', 'Everything about JavaScript and modern frameworks'),
('Java', 'Java programming language and ecosystem'),
('Python', 'Python development and data science'),
('DevOps', 'CI/CD, containers, and infrastructure as code');
```

---

<a id="backend-env-config"></a>

### Environment Configuration

**Install SDKMAN:**

Launch a new terminal and type:

```bash
curl -s "https://get.sdkman.io" | bash
```

Follow the on-screen instructions to wrap up the installation. Afterward, open a new terminal or run:

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Confirm the installation:

```bash
sdk version
```

**Note:** All subsequent commands pertaining to the back-end (including those to run the application) should be run from the `back` directory:

```bash
cd back
```

**Install Java using the project's `.sdkmanrc`:**

```bash
sdk env install
```

**Configure environment variables:**

Copy the example environment file:

```bash
cp example.env .env
```

Edit `.env` with the appropriate settings in nano (or your preferred text editor):

```bash
nano .env
```

**Configuration values to update in `.env`:**

| Variable               | Description                              | Default/Example                                      |
| ---------------------- | ---------------------------------------- | ---------------------------------------------------- |
| `DB_URL`               | MySQL connection URL                     | `jdbc:mysql://localhost:3306/mdd?serverTimezone=UTC` |
| `DB_USERNAME`          | Database username                        | The username from database setup                     |
| `DB_PASSWORD`          | Database password                        | The password from database setup                     |
| `JWT_SECRET`           | Secret key for JWT tokens (min 32 chars) | Generate using: `openssl rand -hex 32`               |
| `JWT_EXPIRATION`       | JWT token validity in milliseconds       | `86400000` (24 hours)                                |
| `CORS_ALLOWED_ORIGINS` | Frontend URL for CORS                    | `http://localhost:4200` (default Angular dev server) |

**Finding connection details:**

- **MySQL URL:** Default is `jdbc:mysql://localhost:3306/<database_name>?serverTimezone=UTC`
  - `<database_name>` should be replaced with the database name defined during setup (e.g., `mdd`)
  - MySQL typically runs on port `3306` (the MySQL configuration should be checked if different)
- **Frontend URL:** Angular CLI typically serves on `http://localhost:4200`
  - This will be configured when the frontend is set up

---

### Running the Back-End

**Note:** Ensure that these commands are run from the `back` directory.

Load Java version from `.sdkmanrc`:

```bash
sdk env
```

Load environment variables from `.env`:

```bash
set -a
source .env
set +a
```

Run the application:

```bash
./mvnw clean compile spring-boot:run
```

The API will start on _http://localhost:8080_ (default).

**To change the port:**

1. `SERVER_PORT=9000` should be added to the `.env` file
2. `server.port=${SERVER_PORT}` should be added to `src/main/resources/application.properties`

**To stop the application:** Press `Ctrl+C`

---

### API Endpoints

Base URL: `http://localhost:8080/api`

All endpoints except `/api/auth/**` require JWT authentication via `Authorization: Bearer <token>` header.

#### Authentication

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

#### User

- `GET /api/user/profile` - Get current user profile
- `PUT /api/user/profile` - Update user profile

#### Topics

- `GET /api/themes` - Get all topics with subscription status

#### Subscriptions

- `POST /api/subscriptions/{topicId}` - Subscribe to topic
- `DELETE /api/subscriptions/{topicId}` - Unsubscribe from topic

#### Articles

- `GET /api/articles?sort=asc|desc` - Get article feed (default: desc)
- `GET /api/articles/{id}` - Get article details with comments
- `POST /api/articles` - Create new article

#### Comments

- `POST /api/articles/{articleId}/comments` - Create comment on article

**For detailed request/response formats and examples, see:** [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

---

## Front-End

<a id="frontend-prerequisites"></a>

### Prerequisites

- **nvm (Node Version Manager)**: manages Node.js versions
- **curl** or **wget** (Linux/macOS only): required for nvm installation

**Install nvm:**

**Linux/macOS:**
Follow the installation instructions on the [official nvm installation guide](https://github.com/nvm-sh/nvm#install--update-script).

**Windows:**
Follow the installation instructions on [nvm-windows](https://github.com/coreybutler/nvm-windows).

**After installation:**
Close and reopen your terminal; then (from any directory) verify:

```bash
nvm --version
```

---

<a id="frontend-setup"></a>

### Frontend Setup

**Note:** All frontend commands should be run from the `front` directory.

```bash
cd front
```

**Install Node.js version specified in `.nvmrc`:**

```bash
nvm install
nvm use
```

**Install dependencies:**

```bash
npm install
```

**Note:** Angular CLI is installed locally as a dev dependency. All `ng` commands should be run with `npx` (e.g., `npx ng serve`) to use the project-specific version and avoid conflicts with other Angular projects.

---

<a id="frontend-env-config"></a>

### Environment Configuration

The frontend connects to the backend API via the `apiUrl` configured in environment files.

**Default configuration:**

- **Development:** `http://localhost:8080/api` (configured in `src/environments/environment.development.ts`)
- **Production:** `http://localhost:8080/api` (configured in `src/environments/environment.ts`)

**To change the API URL:**

Edit `src/environments/environment.development.ts` for development:

```typescript
export const environment = {
  production: false,
  apiUrl: "http://localhost:9000/api", // Change port or host as needed
};
```

Or edit `src/environments/environment.ts` for production builds.

---

### Running the Front-End

**Notes:**

- Ensure these commands are run from the `front` directory
- The backend must be running for the application to function

**Load the correct Node.js version:**

```bash
nvm use
```

**Start the development server:**

```bash
npx ng serve
```

The application will start on _http://localhost:4200_ (default).

**To change the port:**

```bash
npx ng serve --port 4300
```

**To stop the application:** Press `Ctrl+C`

**Access the application:** Open a browser and navigate to `http://localhost:4200`

---

## Project Structure

```
.
├── back/                              # Backend (Spring Boot)
│   ├── src/main/java/.../mddapi/
│   │   ├── controller/                # REST endpoints (HTTP layer)
│   │   ├── service/                   # Business logic
│   │   ├── repository/                # Data access (Spring Data JPA)
│   │   ├── assembler/                 # Compose complex DTOs from multiple services
│   │   ├── mapper/                    # Entity <-> DTO conversion (MapStruct)
│   │   ├── model/                     # JPA entities (database models)
│   │   ├── dto/                       # Data Transfer Objects
│   │   │   ├── model/                 # Simple response DTOs
│   │   │   ├── request/               # Request DTOs with validation
│   │   │   └── response/              # Complex response DTOs
│   │   ├── security/                  # JWT authentication & authorization
│   │   ├── exception/                 # Custom exceptions & global error handler
│   │   └── validation/                # Custom validation annotations
│   ├── src/main/resources/
│   │   └── application.properties     # Application configuration
│   ├── .sdkmanrc                      # SDKMAN Java version configuration
│   └── pom.xml                        # Maven dependencies
├── front/                             # Frontend (Angular)
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/                  # Core functionality (singleton services, guards, interceptors)
│   │   │   │   ├── guards/            # Route guards (auth, public)
│   │   │   │   ├── interceptors/      # HTTP interceptors (auth token injection)
│   │   │   │   ├── layout/            # Layout components (auth, main, public, navbar)
│   │   │   │   └── services/          # Core services (auth, store)
│   │   │   ├── features/              # Feature modules
│   │   │   │   ├── articles/          # Articles feature (feed, detail, create)
│   │   │   │   │   └── services/      # Article-specific services
│   │   │   │   ├── auth/              # Authentication (login, register)
│   │   │   │   ├── landing/           # Landing page
│   │   │   │   ├── profile/           # User profile management
│   │   │   │   └── topics/            # Topics browsing & subscription
│   │   │   │       └── services/      # Topic-specific services
│   │   │   ├── shared/                # Shared code across features
│   │   │   │   ├── models/            # TypeScript interfaces/types
│   │   │   │   └── validators/        # Custom form validators
│   │   │   ├── styles/                # Theme configuration
│   │   │   ├── app.config.ts          # Application configuration
│   │   │   ├── app.routes.ts          # Route definitions
│   │   │   └── app.ts                 # Root component
│   │   ├── environments/              # Environment-specific configuration
│   │   ├── styles.scss                # Global styles
│   │   ├── _variables.scss            # SCSS design tokens
│   │   └── index.html                 # HTML entry point
│   ├── .nvmrc                         # Node.js version specification
│   ├── package.json                   # npm dependencies and scripts
│   ├── angular.json                   # Angular CLI configuration
│   └── tsconfig.json                  # TypeScript configuration
├── schema.sql                         # Database schema creation script
├── example.env                        # Example environment variables (backend)
└── README.md                          # This file
```
