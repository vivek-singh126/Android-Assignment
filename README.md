# Android-Assignment

1. 🚀 Least Recently Used (LRU) Cache Implementation
🔍 Problem Statement
Design an LRU (Least Recently Used) Cache supporting get(key) and put(key, value) in O(1) time.

💡 Solution Approach
Uses:

Doubly Linked List to maintain the usage order.

Hash Map (unordered_map) for quick access to nodes.

Each get/put operation updates usage order. On overflow, the least recently used (tail) node is removed.

📌 Key Concepts
Time Complexity: O(1) for all operations.

Dummy head/tail nodes to simplify list operations.

Manual memory management with new and delete.

🛠️ How to Run
bash
Copy
Edit
g++ lru_cache.cpp -o lru_cache
./lru_cache
2. 🔧 Custom HashMap Implementation
🔍 Problem Statement
Build a basic HashMap from scratch (without STL containers), supporting put, get, and remove in average-case O(1).

💡 Solution Approach
Separate Chaining with linked lists for collision handling.

Dynamic resizing when load factor exceeds threshold.

Custom hash function using modulo.

📌 Key Concepts
Hash table internals and bucket array structure.

Linked list for each bucket.

Rehashing and resizing logic.

🛠️ How to Run
bash
Copy
Edit
g++ my_hashmap.cpp -o my_hashmap
./my_hashmap
3. 📖 Android Book Review App (MVP → MVVM)
🔍 Problem Statement
Build a Book Review Android App that:

Fetches a list of books from a mock API.

Shows book details.

Lets users save favorite books for offline access.

🏗️ Architecture: MVVM (Model-View-ViewModel)
✅ Features
Retrofit API integration.

Room database for local persistence.

LiveData and ViewModel for reactive UI.

Repository pattern for clean data access.

Offline support via Room DB.

🗂️ Key Modules
Data Layer: BookApiService, BookDao, BookDatabase, BookRepositoryImpl

Domain Layer: Book, BookRepository interface

Presentation Layer: Activities, ViewModels, LiveData observers

UI Components: RecyclerView, XML Layouts

📁 Suggested Package Structure
wasm
Copy
Edit
com.example.bookreviewapp/
│
├── data/
│   ├── local/
│   ├── remote/
│   └── repository/
├── domain/
│   ├── model/
│   └── repository/
├── ui/
│   ├── booklist/
│   └── bookdetail/
└── utils/
🚀 How to Run
Create Android Studio Project with Java + Empty Activity.

Add dependencies in build.gradle:

Retrofit

Room

Gson

LiveData, ViewModel

RecyclerView, CardView

Structure code as per suggested architecture.

Mock API: host books.json on http://10.0.2.2:8080/ or put it in assets/.

Add permissions in AndroidManifest.xml.

4. 🌌 OpenGL ES Solar System (Android, Java)
🌍 Features
3D visualization of a glowing sun, rotating planets, and a moon.

Realistic Phong lighting with shaders.

Sun glow effect using custom fragment shader.

Touch input to rotate the camera.

🧱 Structure
bash
Copy
Edit
OpenGLESSolarSystem/
├── MainActivity.java
├── MyGLRenderer.java
├── CelestialBody.java
├── SphereGenerator.java
└── res/raw/
    ├── solar_system_vert.glsl
    └── solar_system_frag.glsl
🧠 Components
Shaders: GLSL programs for vertex/fragment processing.

Renderer: Handles OpenGL drawing, camera controls.

CelestialBody: Contains rotation/orbit data.

SphereGenerator: Creates sphere geometry with normals.

🚀 How to Run
Open the project in Android Studio.

Ensure raw/ folder contains both shader files.

Build and run on a device or emulator with OpenGL ES 2.0+ support.


Open in Android Studio: Import the project into Android Studio.
Ensure raw directory: Make sure the app/src/main/res/raw/ directory exists and contains the two .glsl shader files. If not, create the directory and paste the shader code there.
Build and Run: Connect an Android device or launch an emulator that supports OpenGL ES 2.0 or 3.0. Build and run the project from Android Studio.


