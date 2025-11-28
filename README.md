# Book Publishing Backend

[![GitHub release](https://img.shields.io/github/v/release/CescFe/book-publishing-app?color=blue)](https://github.com/CescFe/book-publishing-app/releases/latest)
[![GitHub license](https://img.shields.io/github/license/CescFe/book-publishing-app?color=blue)](https://github.com/CescFe/book-publishing-app/blob/main/LICENSE)

Frontend client for the Book Publishing platform. Native Android application which consumes the [book-publishing-backend](https://github.com/CescFe/book-publishing-backend) RESTful API to manage books, authors, and collections.

## 📌 About the Project

### 🔎 Tech Stack

- **Kotlin** 2.0
- **Jetpack Compose** - Modern declarative UI
- **Material Design 3** - Design system
- **Hilt** - Dependency injection
- **Retrofit** - HTTP client
- **Kotlin Coroutines** - Asynchronous programming
- **Jetpack Navigation** - Navigation framework
- **ViewModel** - UI data holder
- **Gradle** - Build system

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
│ ├── components/ # Shared UI components
│ ├── auth/ # Authentication flows
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

### 🌍 Internationalization

The app is developed natively in English but displays all UI text in Catalan to end users. The architecture supports easy future translations:

- **Development**: English string resources (`values/strings.xml`)
- **Users**: Catalan translations (`values-ca/strings.xml`)
- **Future-ready**: Easy addition of new languages

### 🧪 Testing Strategy

- **Unit Tests**: ViewModels, Use Cases, Repository logic
- **UI Tests**: Critical user flows with Compose testing
- **Integration Tests**: API service integration

### Code Quality

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

## License

This project is licensed under the MIT License (see the [LICENSE](LICENSE) file for details).
