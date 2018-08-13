# BakingApp (WiP)
The BakingApp allow a user to select a recipe and see video-guided steps for how to complete it.

- BakingApp for Udacity Android Developer Nanodegree
- This app retrieves data from udacity

![BakingApp college](https://preview.ibb.co/ezchd9/Baking_App_college.png)

## Built With

* [Appcombat](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView) - A flexible view for providing a limited window into a large data set
* [Picasso](http://square.github.io/picasso/) - Allows for hassle-free image loading in the application
* [Volley](https://github.com/google/volley) - HTTP library
* [CardView](https://developer.android.com/reference/android/support/v7/widget/CardView) - A FrameLayout with a rounded corner background and shadow
* [Exoplayer](https://github.com/google/ExoPlayer) -  Application level media player for Android
* [Schematic](https://github.com/SimonVT/schematic) - Automatically generate a ContentProvider
* [Espresso](https://developer.android.com/training/testing/espresso/) - Write concise, beautiful, and reliable Android UI tests

## Common Project Requirements 

App is written solely in the Java Programming Language.
App utilizes stable release versions of all libraries, Gradle, and Android Studio. 
App conforms to common standards found in the [Android Nanodegree General Project Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html).
Application uses Master Detail Flow to display recipe steps and navigation between them.
Application uses Exoplayer to display videos.
Application properly initializes and releases video assets when appropriate.
Application should properly retrieve media assets from the provided network links. It should properly handle network requests.
Application makes use of Espresso to test aspects of the UI.
Application sensibly utilizes a third-party library to enhance the app's features. That could be helper library to interface with ContentProviders if you choose to store the recipes, a UI binding library to avoid writing findViewById a bunch of times, or something similar.
Application has a companion homescreen widget.
Widget displays ingredient list for desired recipe.

## General App Usage

### Display recipes
App should display recipes from provided network resource. 

### App Navigation
App should allow navigation between individual recipes and recipe steps.

### Utilization of RecyclerView
App uses RecyclerView and can handle recipe steps that include videos or images.

App conforms to common standards found in the Android Nanodegree General Project Guidelines.

## Components and Libraries

### Master Detail Flow and Fragments
Application uses Master Detail Flow to display recipe steps and navigation between them.

### Exoplayer to display videos
Application uses Exoplayer to display videos.

### Proper utilization of video assets
Application properly initializes and releases video assets when appropriate.

### Proper network asset utilization
Application should properly retrieve media assets from the provided network links. It should properly handle network requests.

### UI Testing
Application makes use of Espresso to test aspects of the UI.

### Third-party libraries
Application sensibly utilizes a third-party library to enhance the app's features. That could be helper library to interface with ContentProviders if you choose to store the recipes, a UI binding library to avoid writing findViewById a bunch of times, or something similar. 

## Homescreen Widget

### Application has a companion homescreen widget.
### Widget displays ingredient list for desired recipe.


## License
```php
Copyright 2017 The Android Open Source Project, Inc.
Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
```
