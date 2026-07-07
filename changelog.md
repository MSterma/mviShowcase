
# Change Log
All notable changes to this project will be documented in this file.

##  2026-06-29

Mostly research about mvi and modular architecture (data,domain)
 
### Added
Doc with notes 
Initialiased git hub repo with empty activity app

### Changed
Changed default .gitignore to  some found on the internet (general purpose .gitignore for Jetpack Compose / kotlin app)
 
### Fixed

##  2026-06-30 - task/initapp

Further reading about MVI.
Tried implementing List-Detail with mock data as HomeScreen

AppContainer - container for manual di

Country - data class used in list-detail

CountryRepository - Interface that specifies communiaction between data and domain

GetCountriesUseCase - Another layer of abstraction. I'm not entirely sure if it's needed or makes sense but it's used in example projects I've seen so I have decided to add it. Will research more about it.

HomeContainer - defines what dependencies are needed for HomeContainer (defines HomeScreen's view model)

HomeEffect - defines non-presistent events such as snackbar or notification

HomeIntent - defines all actions user can perform in Home Screen

HomeSate - defines state of Home screen

HomeViewModel - decides what to do after onIntent invocation and changes State. Interface between View and Data/Business logic



### Added
AppContainer - container for manual di

Country - data class used in list-detail

CountryRepository - Interface that specifies communiaction between data and domain

GetCountriesUseCase - Another layer of abstraction. I'm not entirely sure if it's needed or makes sense but it's used in example projects I've seen so I have decided to add it. Will research more about it.

HomeContainer - defines what dependencies are needed for HomeContainer (defines HomeScreen's view model) and provides them. Uses Singleton design pattern to create only one view model

HomeEffect - defines non-presistent events such as snackbar or notification

HomeIntent - defines all actions user can perform in Home Screen

HomeSate - defines state of Home screen

HomeViewModel - decides what to do after onIntent invocation and changes State. Interface between View and Data/Business logic


FakeCountryRepositoryImpl - Mock repository class

### Changed
MVI - added app container initialization

MainActivity - getting App Container and initializing Home Container with it's repo field.

### Fixed


##  2026-06-30 - task/fetching-countries-from-rest-api
Implemented fetching data in json format  from external REST API using KTOR.
The data is then serialized to data class object and then mapped to match country object in domain.

### added
NetworkClient - KTOR network client to handle requests to API

CountryDTO - Country Data Transfer Object - Kotlin object serialized from response

CountryMapper - maps countryDTO to match country class in domain.

CountryRepositoryImpl - concrete implementation that provides that from API

### Changed
AppContainer - added injecting client and bearer token to repo

HomeScreen - changed how list is displayed and added coil to render images asynchronously 

libs.version.toml - added required dependencies 

##  2026-07-1 - task/fetching-countries-from-rest-api

Implemented paginated list of countries and search mechanism with default compose search bar

### Addded

SearchCountriesUseCase.kt - New use case. Used for searching and default list.

### Changed
CountryRepositoryImpl.kt - changed method from getCountries to searchCountries. 

FakeCountryRepositoryImp.kt - implemented mock searchCountries

CountryRepository.kt - adjusted interface to fit new searchCountries method

HomeContainer.kt  - injecting new UseCase

HomeState.kt - add fields  to support pagination and querying (isPaginateLoading, current offset and boolean indicating whether all countries have been fetched )

HomeIntent.kt - add actions indicating need to fetch more countries or change in search query

HomeViewModel.kt -  added Job (cancellable background task) to handle search bar dynamically 

HomeScreen.kt - Implemented pagination and searching into UI by:

* printing loading indicator while waiting for next countries to be loaded
* adding default compose searchbar to handle searching

### Removed

GetCountriesUseCase.kt - not needed anymore. I'm using searchCountries with empty query instead.


##  2026-07-2 - task/fetching-countries-from-rest-api
Created sealed interface HomeUIState that helps managing UI state by making code cleaner
### Added
HomeUIState - sealed interface that keeps UI state

DataResult - sealed interfaced used for API response enumeration
### Changed

HomeState - removed Ui state variables and added ui state instance

HomeScreen - Added when statement that determines ui state (what to draw)

NetworkClient - Now uses OkHttpClient.builder() to generate engine

##  2026-07-2 - task/modular-architecture
Restructured packages to modules according to MVI/UDF clean architecture.

Also added nav3 to this pull request by mistake.
### Added
NavRoute - implemented routes and transitions
### Changed
MainActivity now uses nav3

Restructured previous view model and data model to be suitable for nav3

##  2026-07-2 - task/replace-manual-di-with-Koin

### Added

DomainModule

NetworkModule

DataModule

HomeModule 

Modules that define Koin dependencies

### Changed

MviApplication

MainActivity

Navigation  was moved to core:ui:navigation and turned into dependency passed through Koin

DI is now handled by Koin
### Removed

AppContainer

HomeContainer

Those are not needed anymore since Koin was added

Unused import statement from app/build.gradle.kts

##  2026-07-07 - task/offline-first-arch
Implemented offline-first architecture using Room and WorkManager.

### Added
CountryDatabase: Room database implementation for local persistence.

CountryEntity Database model for country data.

CountryDao: Data Access Object with support for  Flow queries and upsert operations.

OfflineFirstCountryRepository:  repository that uses Room as the Single Source of Truth.

SyncRepository: New repository dedicated to orchestrating network-to-database synchronization.

SyncWorker: WorkManager implementation for background data synchronization with exponential backoff and connectivity constraints. 

SyncCountriesUseCase: Use case that triggers background synchronization via WorkManager. 

EntityMapper: Mappers between domain models and database entities.

EntityMapperTest: Unit tests for data mapping logic.

### Changed
CountryRepository: Refactored interface to be Flow based. 

HomeViewModel: Updated to observe local database reactively and trigger background sync separately.

DataModule: Updated Koin configuration to provide Room database, DAOs, and new repositories.

libs.versions.toml Added Room and WorkManager dependencies.
