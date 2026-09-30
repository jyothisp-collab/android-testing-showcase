# Android Testing Showcase

A compact Android reference implementation demonstrating production testing patterns for client review. It shows how to test ViewModels, repositories, databases, and network layers using modern Android testing tools.

## What It Demonstrates

- **Turbine** for testing `StateFlow` and `Flow` emissions from ViewModels and repositories.
- **Fake repositories** as lightweight test doubles that avoid mocking framework overhead.
- **MockWebServer** for end-to-end HTTP testing with real Retrofit clients.
- **In-memory Room databases** for testing DAO operations without device or emulator.
- **Coroutines test** with `runTest`, `UnconfinedTestDispatcher`, and `MainDispatcherRule`.
- **MockK** for verifying interactions with framework classes like `WorkManager`.
- **Sealed `Result<T>`** types that make success and error states explicit and testable.
- Hilt dependency injection with testable module bindings.

## Architecture Overview

```
app/src/main/java/com/example/androidtestingshowcase/
  core/
    common/          — Result<T> sealed class
    data/            — Models, repository interface, fake implementation
    database/        — Room entity, DAO, database setup
    di/              — Hilt module for networking and database
    network/         — Retrofit API, OkHttp client, demo backend interceptor
  features/
    home/            — Home screen with item list, ViewModel
    items/           — Item list and detail screens
  ui/                — Navigation host and Material 3 theme
```

The project structure mirrors a real app. The only difference is that the data layer uses a demo backend interceptor instead of a real server, so tests run without network access.

## Testing Strategy

Tests are organized by layer, from low-level infrastructure to high-level behavior:

| Layer | Test Type | Tools |
|---|---|---|
| Network interceptor | Unit | JUnit, OkHttp test rules |
| Repository | Unit | Turbine, fake implementations |
| Room DAO | Integration | In-memory database, Turbine |
| ViewModel | Unit | Turbine, MockK, `MainDispatcherRule` |
| HTTP client | Integration | MockWebServer, Retrofit |
| Data model | Unit | JUnit assertions |

## Test Files

- **`DemoBackendInterceptorTest`** — Verifies the interceptor returns valid JSON for items and single-item endpoints with simulated delay.
- **`FakeItemsRepositoryTest`** — Verifies the fake repository returns items, handles refresh, and supports set/clear operations.
- **`HomeViewModelTest`** — 4 tests using Turbine: initial loading state, items loaded from repository, refresh triggers reload, error state on failure.
- **`ShowcaseDatabaseTest`** — In-memory Room tests: insert and query items, upsert replaces existing, clear removes all.
- **`ApiClientTest`** — Verifies Retrofit creation with correct base URL and logging interceptor.
- **`ShowcaseItemTest`** — Data class equality, copy behavior, default values, toString.
- **`MainDispatcherRule`** — JUnit `TestRule` that swaps `Dispatchers.Main` with an `UnconfinedTestDispatcher`.

## Key Testing Patterns

### StateFlow Testing with Turbine

```kotlin
viewModel.uiState.test {
    awaitItem() // initial loading state
    val loaded = awaitItem() // populated state
    assertEquals(2, loaded.items.size)
    cancelAndIgnoreRemainingEvents()
}
```

### In-Memory Room Testing

```kotlin
val database = Room.inMemoryDatabaseBuilder(context, ShowcaseDatabase::class.java)
    .allowMainThreadQueries()
    .build()
```

### MockWebServer for HTTP Testing

```kotlin
server.enqueue(MockResponse().setResponseCode(200).setBody("""[...]"""))
val items = api.getItems()
```

### Fake Repositories

Fakes implement the repository interface with in-memory `MutableStateFlow` values. They avoid MockK overhead for simple read/write behavior and make tests deterministic.

## Build And Run

Requirements:

- Android Studio with an installed Android SDK.
- JDK 17 or newer.

Commands:

```bash
./gradlew test
./gradlew assembleDebug
```

## Dependencies

- **Turbine** — Flow testing library from Cash App.
- **MockK** — Kotlin-native mocking framework.
- **MockWebServer** — OkHttp's in-process HTTP server for integration tests.
- **kotlinx-coroutines-test** — Test dispatchers and `runTest` scope.
- **Room testing** — In-memory database builder for DAO tests.

## Out Of Scope

- UI instrumentation tests with Espresso or Compose Test.
- Code coverage reporting and CI integration.
- Test fixtures and factory patterns for complex model creation.
- Performance testing and benchmark rules.
- Mutation testing.
