# Android-Assignment

Solutions for Coding Assignments
This document provides detailed explanations, design choices, and code (or pseudocode for larger projects) for three distinct coding assignments. Each solution aims to meet the specified requirements and constraints, demonstrating fundamental data structures, algorithms, and architectural patterns.



# Q1. Least Recently Used (LRU) Cache Implementation

Problem Description
Design and implement a Least Recently Used (LRU) Cache. A cache has a fixed capacity, and when it exceeds that capacity, it must evict the least recently used item to make space for the new one. The implementation must support get(key) and put(key, value) operations with O(1) time complexity.
Solution Approach
The LRU Cache is implemented using a combination of two data structures:
Doubly Linked List: To maintain the order of usage. The head of the list represents the most recently used item, and the tail represents the least recently used item. Each node stores both the key and value.
Hash Map (std::unordered_map): To provide O(1) lookup time for keys. The hash map stores key -> pointer_to_Node mappings, allowing direct access to any node in the linked list.
When an item is accessed (get) or updated/inserted (put), its corresponding node is moved to the head of the doubly linked list. If the cache is full during a put operation, the node at the tail of the list (least recently used) is evicted.
Key Features & Concepts Demonstrated
Data Structure Design: Effective combination of a hash map and a doubly linked list.
Time Complexity: Achieving O(1) average time complexity for all core operations.
Memory Management: Proper allocation and deallocation of Node objects in C++ using new and delete (especially in the destructor).
Dummy Nodes: Use of head and tail dummy nodes to simplify linked list operations and avoid edge cases (empty list, single element).
How to Compile and Run (C++)
Save the provided C++ code (including the Node struct, LRUCache class, and main function) into a file named lru_cache.cpp.
Compile the code using a C++ compiler (e.g., g++):
g++ lru_cache.cpp -o lru_cache


Execute the compiled program:
./lru_cache

The output will show the results of the get operations as per the example test case.



# Q2. Custom HashMap Implementation
Problem Description
Implement a simplified version of a HashMap (unordered map) from scratch, without using built-in hash table libraries. It must support put(key, value), get(key), and remove(key) operations in average-case O(1) time.
Solution Approach
This HashMap is implemented using the Separate Chaining method for collision resolution:
Array of Buckets: A std::vector is used as the underlying array, where each element (bucket) can store multiple key-value pairs that hash to the same index.
Linked Lists: Each bucket is a pointer to the head of a singly linked list. Node objects within these lists store the key and value.
Custom Hash Function: A simple modulo operator (key % capacity) is used to map keys to bucket indices.
Dynamic Resizing: To maintain average O(1) performance, the hash map automatically resizes (doubles its capacity and rehashes all elements) when the load factor (number of elements / capacity) exceeds a predefined threshold (e.g., 0.7).
Key Features & Concepts Demonstrated
Hash Table Principles: Fundamental understanding of hashing, buckets, and collision resolution.
Separate Chaining: Practical application of linked lists to handle hash collisions.
Load Factor: Importance of the load factor in maintaining hash map efficiency.
Dynamic Resizing (Rehashing): Implementation of the resizing mechanism to prevent performance degradation as more elements are added.
Average Case O(1) Complexity: How proper design and resizing contribute to efficient operations.
Memory Management: Handling dynamic memory allocation and deallocation of Node objects.
How to Compile and Run (C++)
Save the provided C++ code (including the Node struct, MyHashMap class, and main function) into a file named my_hashmap.cpp.
Compile the code using a C++ compiler (e.g., g++):
g++ my_hashmap.cpp -o my_hashmap


Execute the compiled program:
./my_hashmap

The output will show the results of the get operations, demonstrating the HashMap's functionality.




# Q3. Android Book Review App MVP
Problem Description
Implement a Minimum Viable Product (MVP) version of a Book Review App for Android. The app should allow users to browse a list of books from a fake API, view detailed information for each book, and save books locally as "favorites" for offline access. The app must adhere to specific architectural and technology stack constraints.
Solution Approach (MVVM Architecture)
The application is designed using the MVVM (Model-View-ViewModel) architectural pattern to ensure separation of concerns and maintainability.
UI Layer (Presentation): Consists of Activities (e.g., BookListActivity, BookDetailActivity), XML layouts, and ViewModels (BookListViewModel, BookDetailViewModel). Activities observe LiveData from ViewModels to update the UI.
Domain Layer: Defines the core business logic and data models. It includes the Book POJO (Plain Old Java Object) and the BookRepository interface, which abstracts data operations.
Data Layer: Responsible for fetching and persisting data. It includes:
Remote Data Source: BookApiService (Retrofit interface) for fetching books from a mock API.
Local Data Source: AppDatabase and BookDao (Room Persistence Library) for storing favorite books in an SQLite database.
Repository Implementation: BookRepositoryImpl implements the BookRepository interface, coordinating data flow between the API and Room, and handling offline fallback.
Key Features & Concepts Demonstrated
MVVM Architecture: Practical implementation of Model-View-ViewModel for Android application development.
Separation of Concerns: Clear division of responsibilities among UI, Domain, and Data layers.
Networking with Retrofit: How to define API interfaces, make asynchronous network calls, and parse JSON responses.
Local Persistence with Room: Using Room to create a local SQLite database, define entities (Book), and perform CRUD (Create, Read, Update, Delete) operations via DAOs.
Reactive UI with LiveData: Utilizing LiveData for observable data streams that automatically update the UI when underlying data changes, ensuring a responsive user experience.
Repository Pattern: Abstracting data sources behind a unified BookRepository interface, making the application more flexible and testable.
Offline Mode: Basic implementation of offline access by falling back to locally saved (favorited) books when network data is unavailable.
Dependency Management: Manual dependency injection using ViewModelProvider.Factory for ViewModels.
How to Set Up and Run (Android Studio)
Note: Due to the complexity of an Android application, the provided solution for Q3 is a detailed architectural design with pseudocode and key Java/XML snippets. It is not a fully runnable, self-contained file like the C++ solutions. You will need to set up an Android Studio project and manually create the files and folders according to the specified package structure.
Create a New Android Studio Project:
Choose "Empty Activity" template.
Language: Java.
Minimum SDK: API 21 (or higher).
Update app/build.gradle: Add all the necessary dependencies for Room, Lifecycle (ViewModel, LiveData), Retrofit, Gson, RecyclerView, and CardView as outlined in the solution.
Create Package Structure: Create the following packages inside app/src/main/java/com/example/bookreviewapp (replace com.example.bookreviewapp with your actual package name):
data.local
data.remote
data.repository
domain.model
domain.repository
ui.booklist
ui.bookdetail
utils
Copy Code: Place the respective Java code snippets into their corresponding files and packages.
Create Layouts: Create the XML layout files (activity_book_list.xml, item_book.xml, activity_book_detail.xml) in app/src/main/res/layout/.
Add Drawables: Create placeholder drawables (book_placeholder.xml, book_detail_placeholder.xml) in app/src/main/res/drawable/.
Configure AndroidManifest.xml: Add INTERNET permission and usesCleartextTraffic if using HTTP, and declare activities.
Mock API Setup:
Option 1 (Recommended for testing Retrofit): Host a books.json file on a local web server (e.g., Node.js http-server, Python SimpleHTTPServer, Apache, Nginx). Update RetrofitClient.BASE_URL to point to your server's IP address (e.g., http://10.0.2.2:8080/ for Android Emulator).
Option 2 (Manual JSON parsing): If you choose to manually parse JSON from assets, you would put books.json in app/src/main/assets/ and modify BookRepositoryImpl to read and parse this file directly instead of using BookApiService.
Build and Run: Sync Gradle, then run the application on an Android Emulator or a physical device.






# OpenGL ES Solar System on Android
This project demonstrates a basic 3D solar system visualization built using OpenGL ES 2.0/3.0 on Android. It features a glowing sun, orbiting planets, and a moon, with a simple camera control via touch input.

Features
3D Sphere Rendering: Utilizes custom sphere generation for celestial bodies.
Basic Phong Lighting: Implements ambient, diffuse, and specular lighting for realistic shading on planets and moons.
Sun Glow Effect: The sun is rendered with a pulsing glow.
Orbital and Rotational Animations: Planets orbit the sun and rotate on their own axes; the moon orbits a planet.
Camera Control: Navigate the solar system using touch gestures (drag to rotate camera view).
GLSL Shaders: Custom vertex and fragment shaders written in GLSL.

Project Structure
The core components of this project are organized as follows:
OpenGLESSolarSystem/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com/
│       │   │       └── example/
│       │   │           └── openglessolarsystem/
│       │   │               ├── MainActivity.java
│       │   │               ├── MyGLRenderer.java
│       │   │               ├── CelestialBody.java
│       │   │               └── SphereGenerator.java
│       │   └── res/
│       │       └── raw/
│       │           ├── solar_system_vert.glsl
│       │           └── solar_system_frag.glsl
│       └── AndroidManifest.xml
└── ... (other Gradle and project files)


File Descriptions
MainActivity.java: The entry point of the Android application. It sets up the GLSurfaceView and initializes MyGLRenderer to handle the OpenGL rendering.
MyGLRenderer.java: Implements the GLSurfaceView.Renderer interface. This class is responsible for all OpenGL ES drawing commands, including shader loading, setting up matrices (model, view, projection), managing time for animations, and handling camera movement based on touch input.
CelestialBody.java: A simple data class representing a celestial object. It holds properties like color, orbital speed, rotation speed, radius, and the OpenGL buffer IDs (VAO, VBOs, EBO) for its rendered mesh.
SphereGenerator.java: A utility class (object in Kotlin, static methods in Java) that generates the vertex, normal, texture coordinate, and index data for a sphere mesh. It also handles buffering this data into OpenGL VBOs and an optional VAO for efficient rendering.
solar_system_vert.glsl: The vertex shader program. It transforms vertex positions, calculates normals in world space, and passes necessary data to the fragment shader.
solar_system_frag.glsl: The fragment shader program. It calculates the final color of each pixel, applying Phong lighting (ambient, diffuse, specular) for planets/moons and a custom glowing effect for the sun.

How to Run
Clone or Download: Get the project files onto your machine.
Open in Android Studio: Import the project into Android Studio.
Ensure raw directory: Make sure the app/src/main/res/raw/ directory exists and contains the two .glsl shader files. If not, create the directory and paste the shader code there.
Build and Run: Connect an Android device or launch an emulator that supports OpenGL ES 2.0 or 3.0. Build and run the project from Android Studio.


