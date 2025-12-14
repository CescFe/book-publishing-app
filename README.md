# Book Publishing Backend

[![GitHub release](https://img.shields.io/github/v/release/CescFe/book-publishing-app?color=blue)](https://github.com/CescFe/book-publishing-app/releases/latest)
[![GitHub license](https://img.shields.io/github/license/CescFe/book-publishing-app?color=blue)](https://github.com/CescFe/book-publishing-app/blob/main/LICENSE)

Frontend client for the Book Publishing platform. Native Android application which consumes the [book-publishing-backend](https://github.com/CescFe/book-publishing-backend) RESTful API to manage books, authors, and collections.

## 📌 About the Project

### 🔎 Tech Stack

| Technology                | Purpose                               |
|---------------------------|---------------------------------------|
| **Kotlin 2.0**            | Programming language                  |
| **Jetpack Compose**       | Modern declarative UI toolkit         |
| **Material Design 3**     | Design system with brand theming      |
| **Retrofit + OkHttp**     | HTTP client for API communication     |
| **Kotlin Coroutines**     | Asynchronous operations in ViewModels |
| **Jetpack Navigation**    | Navigation between screens            |
| **ViewModel + StateFlow** | UI state management                   |
| **Gradle + Spotless**     | Build system and code formatting      |

### 🏗️ Architecture

This project follows **Clean Architecture** with **MVVM** pattern:

- **Presentation Layer**: Composables, ViewModels, UI state management
- **Domain Layer**: Use cases, business logic, repository interfaces
- **Data Layer**: API services, repositories, data sources

### 🧱 Structure
```
app/src/main/java/org/cescfe/book_publishing_app/
├── ui/
│ ├── theme/ # Design system & theming
│ ├── navigation/ # Navigation graphs
│ ├── shared/ # Shared components
│ ├── auth/ # Authentication flow
│ ├── authors/ # Author management
│ ├── books/ # Book management
│ └── collections/ # Collection management
├── domain/
│ ├── repository/ # Repository interfaces
│ ├── model/ # Domain models
│ └── usecase/ # Business use cases
└── data/
├── remote/ # API services & DTOs
└── repository/ # Repository implementations
```

## 📱 Screens

### Login
Authentication screen with Editorial Denes branding.

<!-- TODO: Add screenshot -->

### Books
List of all books in the catalog with title, author, collection, and price.

<!-- TODO: Add screenshot -->

### Authors
List of authors with name, pseudonym, and email information.

<!-- TODO: Add screenshot -->

### Collections
List of collections with reading level, language, and genre details.

<!-- TODO: Add screenshot -->

## 🌍 Internationalization

The app is developed natively in English. In addition, the app dynamically adapts to the device language, supporting Catalan and Spanish.

## 🧪 Testing Strategy

- **Unit Tests**: ViewModels, DTOs, Repository logic
- **Instrumented Tests**: UI components with Compose Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires emulator/device)
./gradlew connectedAndroidTest
```

## ⚙️ CI/CD Workflow

### Automatic Validation

Every push to `main` and every pull request automatically runs:
- ✅ **Spotless Check** — Code formatting validation
- ✅ **Android Lint** — Static code analysis
- ✅ **Unit Tests** — ViewModel and repository tests
- ✅ **Build** — Full project compilation

### Create Tag and Release

1. Go to **Actions** → **Create Release Tag**
2. Run manually with the desired version (e.g., `v0.1.0`)
3. This creates a Git tag
4. Go to **Tags** → Click on **Release**

## Code Quality

```bash
# Check all formatting
./gradlew spotlessCheck

# Apply formatting
./gradlew spotlessApply

# Run all quality checks
./gradlew check
```

## 🔌 API Integration

The app consumes the [book-publishing-backend](https://github.com/CescFe/book-publishing-backend) RESTful API.

| Endpoint           | Description          |
|--------------------|----------------------|
| `POST /auth/login` | User authentication  |
| `GET /books`       | List all books       |
| `GET /authors`     | List all authors     |
| `GET /collections` | List all collections |

## License

This project is licensed under the MIT License (see the [LICENSE](LICENSE) file for details).
