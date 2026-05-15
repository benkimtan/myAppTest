# MyTestApp

![Android Min SDK 24](https://img.shields.io/badge/Android%20Min%20SDK-24-blue) ![Target SDK 36](https://img.shields.io/badge/Target%20SDK-36-blue) ![Kotlin 1.9+](https://img.shields.io/badge/Kotlin-1.9%2B-purple) ![Jetpack%20Compose](https://img.shields.io/badge/Jetpack%20Compose-Enabled-orange) ![Hilt](https://img.shields.io/badge/Hilt-DI-green)

## Description

MyTestApp is a small Android sample that demonstrates a **dual‑view architecture**:

* A classic “Hello World” screen that shows and removes a text message.  
* A **Ticker Tracker** screen that lets users add, view, and delete stock‑ticker transactions stored locally with **Room**.  

The app uses **MVVM** (ViewModel + LiveData/Flow), **Kotlin Coroutines**, and **Jetpack Compose** dependencies (even though the UI is currently XML‑based) to showcase modern Android development practices.

## Features

- **Hello World** view with a show/hide button.  
- **Remove** button to dismiss the message.  
- **Transaction list** displayed in a `RecyclerView` with a `LinearLayoutManager`.  
- **Add Transaction** dialog (ticker + quantity) with validation.  
- **Delete Transaction** confirmation dialog.  
- **View selector spinner** that toggles between the Hello World view and the Ticker Tracker view.  
- **Room** database integration for persisting transactions.  
- **Kotlin Coroutines + Flow** for reactive UI updates.  
- **Hilt** placeholder for future dependency injection (the project already includes the `ksp` plugin and `javax.inject` library).  

## Tech Stack

- **Language:** Kotlin  
- **UI Framework:** XML layouts (Compose libraries are added for future migration)  
- **Architecture:** MVVM (ViewModel, Repository, Room)  
- **Core Libraries**
  - `androidx.core:core-ktx`
  - `androidx.lifecycle:lifecycle-runtime-ktx`
  - `androidx.activity:activity-compose`
  - `androidx.compose.ui:ui`, `material3`, `ui-tooling-preview` (via Compose BOM)
  - `androidx.appcompat:appcompat`
  - `androidx.recyclerview:recyclerview`
  - `androidx.cardview:cardview`
  - `androidx.constraintlayout:constraintlayout`
- **Dependency Injection:** Hilt (`javax.inject` & `ksp` plugin) – ready for integration.  
- **Database:** Room (`runtime`, `ktx`, compiler via KSP)  
- **Asynchronous:** Kotlin Coroutines, Flow  
- **Build Tools:** Gradle Kotlin DSL, Android Gradle Plugin 8+, Java 11 compatibility  

## Getting Started

### Prerequisites

- **Android Studio** (latest stable version, e.g., Flamingo or newer).  
- **JDK 11** (configured in `compileOptions`).  
- **Gradle** (wrapper included).

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/benkimtan/myAppTest.git
   cd myAppTest
   ```

2. **Open the project** in Android Studio (`File → Open → myAppTest`).  

3. **Sync Gradle** (Android Studio will prompt automatically).  

4. **Run the app**  
   - Choose an emulator or connected device.  
   - Press **Run** (`Shift+F10` or the green Play button).  

## Architecture

The project follows **MVVM**:

- **View (Activity)** – `MainActivity` handles UI interactions, sets up the spinner, and forwards actions to the `ViewModel`.  
- **ViewModel** – `TransactionViewModel` (not shown here) exposes a `Flow<List<Transaction>>` (`allTransactions`) and CRUD functions.  
- **Repository** – `TransactionRepository` abstracts the Room DAO.  
- **Room** – Provides type‑safe SQLite persistence with compile‑time verification via KSP.  

All asynchronous operations are launched in `lifecycleScope` to respect the Activity lifecycle.

---