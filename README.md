# Crypto-transfer demo 
Android app mocking funds transfer screen in a cryptocurrency wallet app. 
[Deblock](https://deblock.com/en-FR) app used as a UI/UX reference.

## App consists of two screens:
* Money transfer screen with multi-currency support and real-time transfer fees updates (OK, not exactly realtime, but still once a 6 sec).
* Currency selection bottom sheet. Used to pick the base "fiat" currency with exchange rates being updated on the first launch. Values received are kept in cache to not spam the "gentle" API.

## Stack:
* Kotlin 100%
* Jetpack Compose
* Kotlin Coroutines/Flow
* Hilt
* Retrofit2 + Okhttp
* Jetpack ViewModel
* Gradle Kotlin DSL

The app uses open APIs Etherscan and Gecko to get "Etherium Gas Price" and conversion fees.

## Architecture
The app architecture created using Clean Architecture principles:
* UI, domain logic, APIs are placed to different layers with no direct dependencies.
* UI split onto two parts: the visual part and user controls (Compose), and the data/logic (ViewModel)
* Business logic implemented using multiple small UseCase classes.
* Core data received thru the separate repository that can use API abstractions to fetch the data

## Improvements TODO
Would be nice to improve the app in the following areas:
* Add tests. At this point it was out of the task scope unfortunately.
* Improve "loading" state for *Currencies selector* screen. Just a ProgressIndicator does not look that nice.
* Implement more solid local caching/data storage solution for the whole models used in the app. Jetpack's Room should fit perfectly.
* Add API keys to enable faster API fetch and lower spam-barrier.

Currency icons made by Freepik from [flaticon.com](https://www.flaticon.com/authors/freepik)