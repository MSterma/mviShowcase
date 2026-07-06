# MVI Showcase - Country Explorer

A modern Android application showcasing **MVI (Model-View-Intent)** architecture, **Clean Architecture** principles, and the latest Jetpack Compose libraries. The app allows users to search for countries, view details, and demonstrates efficient pagination and state management.

##  Key Features

- **Country Search**: Real-time search.
- **Pagination**: Infinite scrolling for country lists.
- **Detailed View**: View country details like capital, population, and flag.
- **Modern UI**: Built entirely with Jetpack Compose and Material 3.

##  Tech Stack

- **Kotlin**: Kotlin.
- **Jetpack Compose**: For the entire UI layer.
- **MVI Architecture**: Uni-directional data flow for predictable state management.
- **Clean Architecture**: Multi-module setup (app, feature, core) for better scalability and testability.
- **Koin**: Lightweight dependency injection.
- **Ktor**: Asynchronous HTTP client for network requests.
- **Navigation 3**: The latest navigation component from Google.
- **Coil**: Image loading for country flags.
- **Kotlinx Serialization**: Type-safe JSON parsing.
- **Coroutines & Flow**: For asynchronous programming and reactive streams.

##  Project Structure

The project follows a modularized Clean Architecture approach:

- `:app`: Entry point, DI configuration, and global navigation.
- `:feature:home`: Contains the main search and details screens.
- `:core:data`: Implementation of repositories and data sources.
- `:core:domain`: Business logic, UseCases, and Repository interfaces.
- `:core:network`: Ktor client configuration and API models.
- `:core:model`: Shared domain models across modules.
- `:core:common`: Utility classes and result wrappers.
- `:core:ui`: Shared UI components and design system.

##  Getting Started

### Prerequisites

- **Android Studio Ladybug (2024.2.1)** or newer.
- **JDK 17** or newer.

### Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/mviShowcase.git
   ```

2. **API Key Setup**:
   The app uses the [REST Countries API](https://api.restcountries.com/).
   - Obtain an API key from their website.
   - Open `local.properties` in the root directory.
   - Add your API key:
     ```properties
     rest_countries_api_key="your_api_key_here"
     ```

3. **Build and Run**:
   - Open the project in Android Studio.
   - Sync Gradle.
   - Run the `app` module on an emulator or physical device.

