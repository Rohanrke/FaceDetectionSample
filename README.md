
# MediaPipe Tasks Face Detection Android Demo

### Overview

This is a PhotoFeed app that captures images from gallery 


## Build the demo using Android Studio

### Prerequisites

*   The **[Android Studio](https://developer.android.com/studio/index.html)**
    IDE. This sample has been build and tested on Android Studio Koala.

### Building

*   Open Android Studio. From the Welcome screen, select Open an existing
    Android Studio project.

*   From the Open File or Project window that appears, navigate to and select
    the FaceDetectinSample. Click OK. You may
    be asked if you trust the project. Select Trust.

*   If it asks you to do a Gradle Sync, click OK.

*   With your Android device connected to your computer and developer mode
    enabled, click on the green Run arrow in Android Studio.

### Code Structure
    Sample contains 6 modules as Follows
    app -- main app module of the sample. Launching Activity is MainActivity
    base -- contains base classes for Activity, ViewModels, etc this could of used for any modules to extend it from base classes.
            This modules can also be used for common classes.
    domain -- this module acts as bridge between modules containing features (media-scan in this sample) and data module. This contains only interfaces 
    data -- this module contains Implementation classes for data providing, ex RepoImpl, Database , dao etc
    media-scan - this module contains main feature of this sample, PhotoFeed, it fetched Images from Gallery 
                 and interact with domain layer to save and fetch data from database. 
    media-scan-kit - this module acts as a bridge between any other featire/module and media-scan 


### Functionality
    1. Click on Grid Image item will open Image in Bigger Dialog and FaceDetection will run on it
    2. LongClick -- PopUp will open for Add Tag Task
    Unit Test written for Repository Layer only

    MainActivity in app moudle acts as a Splash/ Launching Activity.
    Feature Screen (PhotoFeedActivity) is being launched from MainActivity using media-scan-kit.
    

## Demo
[Download the Face Detection Demo](demo/face_detection_demo.mp4)

    
   