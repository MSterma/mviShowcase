
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

##  2026-06-30

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
 


