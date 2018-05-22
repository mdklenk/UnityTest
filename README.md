# Running Unity in an android.app.Fragment Demo

The purpose of this project is to demonstrate how to create an instance of UnityPlayer (the compiled code and scene from Unity) that is executed and run concurrently with native Android Java code. This is done to accelerate switching between the Unity view and the native Android application view. There are multiple methods to achieve this, the "Ultimate" branch handles it via embedding of the UnityPlayer view and the Android application in separate fragments, whereas the implementation of the "Master" brand uses the even simpler way of constraining the UnityPlayer to a specific android.view.View Object and toggling its visibility. 

The UnityPlayer itself is handled as a sort of singleton within the UnityPlayerActivity (the class that is called by the Android OS launcher, as specified in the AndroidManifest.xml). TODO here is to extract UnityPlayerActivity to its own proper singleton and update the references in the other files.

## Getting Started

The repository contains two branches which each contain a slightly different implementation of the demonstration described above. Both repository branches can be imported and compiled within an Android development environment, the required Unity3d scene is included as .jar archive.

### Prerequisites

I have successfully compiled this on Android Studio by IntelliJ and JDK 1.8, not just Java Runtime Environment JRE, to run it you need an Android Device running at least Android 4.2 or an equivalent emulator.

### Installing

First download and unpack repository (as .zip or clone it). In Android Studio (Eclipse et al. should be equivalent) go to "File -> New -> Import Project" and point it towards the folder containing the unpacked repository. This will create a new project that is ready to compile.

### Running

The rest should be straightforward compilation and deployment to run it and see how fast it switches between Unity and Android (once Unity is started for the first time).

## Detailed Description
Now for a detailed play-by-play of what is going on when the application is run. The Android OS calls UnityPlayerActivity, as specified in the AndroidManifest.xml that I took from the one automatically generated by Unity's Android export feature.

### UnityPlayerActivity
UnityPlayerActivity is a straightforward android.app.Activity. This class manages the Fragments by instantiating, running and switching them when so instructed. It also is the class that is being called by the C# script (see file in Repository for details) and the class that UnityPlayer resides in (see TODO above, this could be extracted to its own class for better organization).

The procedure goes. 1. nable main content layout 

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
