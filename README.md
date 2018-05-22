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

Member fields:
´´´
    // the Object that is the interface between Unity and Android
    private UnityPlayer mUnityPlayer;
    // not needed in FragmentManager (Ultimate) implementation
    // private Activity thisActivity;
    //the two fragments
    private UnityFragment unityFragment;
    private AndroidButtonFragment androidButtonFragment;
´´´


The procedure goes: 
  1. Enable main content layout and find container where to put Fragment(s) (depending on branch)
  ```
  setContentView(R.layout.activity_main);
  FrameLayout container = (FrameLayout) findViewById(R.id.container);
  ```
  2. Instantiate and initialize UnityPlayer
  ```
  mUnityPlayer = new UnityPlayer(this);
  int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
  mUnityPlayer.init(glesMode, false);
  ```
  3. Initialize fragments and add to UnityPlayerActivity's own FragmentManager when the UnityPlayerActivity is newly created
  ```
  if (savedInstanceState == null || getFragmentManager().findFragmentByTag("unity") == null || (getFragmentManager().findFragmentByTag("android") == null)) {
            unityFragment = UnityFragment.newInstance(mUnityPlayer);
            androidButtonFragment = AndroidButtonFragment.newInstance(mUnityPlayer);
            //mUnityPlayer.pause();
            getFragmentManager().beginTransaction()
                    .add(container.getId(), unityFragment)
                    .add(container.getId(), androidButtonFragment)
                    .commit();
        }
  ```
  4. Offer methods that can be called by Fragments and Unity itself to switch between (by hiding the other) fragments. 
  ```
  public void switchFragmentToUnity(){
    //for good measure, not necessary
    mUnityPlayer.resume();
    getFragmentManager().beginTransaction().hide(androidButtonFragment).show(unityFragment).commit();
  }
  ```
  ```
   public void callMeNonStatic(String s){
        Log.d("Non-Static", "Non-Static Call from Unity at " + s);
    /* Easy way
        thisActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.GONE);
            }
        });*/

    // Harder way (possibly more powerful)
        getFragmentManager().beginTransaction().hide(unityFragment).show(androidButtonFragment).commit();
        //mUnityPlayer.pause();
    }
    // and static;
    public static void callMeStatic(String s){
        Log.d("Static", "Static Call from Unity at " +  s);
    }
  ```
  5. Furthermore I have overridden the back-button to provide (jankily) animated transitions between the Fragments as demonstration
  ```
  @Override
    public void onBackPressed(){
        Log.d("Input", "Back button pressed");
        //mUnityPlayer.pause();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if(getFragmentManager().findFragmentByTag("android").isHidden())
        {
            fragmentTransaction.hide(unityFragment).show(androidButtonFragment).commit();
        } else
        {
            fragmentTransaction.hide(androidButtonFragment).show(unityFragment).commit();
        }
        //container.setVisibility(View.GONE);
    }
  ```
  6. Lastly UnityPlayerActivity contains overriden lifeCycle Methods in order to properly manage the UnityPlayer during different points in the apps' lifecycle, for instance when switching app.
Explain what these tests test and why

### UnityFragment

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
