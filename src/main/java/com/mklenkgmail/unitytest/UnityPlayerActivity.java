package com.mklenkgmail.unitytest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.unity3d.player.R;
import com.unity3d.player.UnityPlayer;

/***
 * Simple proof of concept for having a Unity Android fragment running in the background of another
 * native Android activity; Can be improved upon in numerous ways.
 */

public class UnityPlayerActivity extends Activity {

    private UnityPlayer mUnityPlayer;
    // not needed in FragmentManager implementation
    // private Activity thisActivity;
    private UnityFragment unityFragment;
    private AndroidButtonFragment androidButtonFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout container = (FrameLayout) findViewById(R.id.container);

        //to grant access to UI thread in static sub-context (not needed in this Implementation)
        //thisActivity = this;

        //Instantiate and initialize UnityPlayer
        mUnityPlayer = new UnityPlayer(this);
        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
        mUnityPlayer.init(glesMode, false);

        //On cold boot only Initialize fragments and add to UnityPlayerActivity's own FragmentManager
        if (savedInstanceState == null || getFragmentManager().findFragmentByTag("unity") == null || (getFragmentManager().findFragmentByTag("android") == null)) {
            unityFragment = UnityFragment.newInstance(mUnityPlayer);
            androidButtonFragment = AndroidButtonFragment.newInstance(mUnityPlayer);
            //mUnityPlayer.pause();
            getFragmentManager().beginTransaction()
                    .add(container.getId(), unityFragment, "unity")
                    .add(container.getId(), androidButtonFragment, "android")
                    .commit();
        }

/*  DEPRECATED: Old implementation with View being disabled;
        Button buttonUnity = (Button) findViewById(R.id.buttonUnity);
        buttonUnity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: when switching between Unity and Android view too quickly the UnityPlayer
                // takes too long to resolve the pause() and resume() methods, making it possible to
                // get stuck in paused Unity screen;
                mUnityPlayer.resume();
                getFragmentManager().beginTransaction().show(unityFragment).commit();
                //container.setVisibility(View.VISIBLE);

            }
        });*/
    }

    //Back button overridden to demonstrate transition
    @Override
    public void onBackPressed(){
        Log.d("Input", "Back button pressed");
        //mUnityPlayer.pause();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        // Warning: Breaks stuff, use at your own peril!
        //fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if(getFragmentManager().findFragmentByTag("android").isHidden())
        {
            fragmentTransaction.hide(unityFragment).show(androidButtonFragment).commit();
        } else if (getFragmentManager().findFragmentByTag("unity").isHidden())
        {
            fragmentTransaction.hide(androidButtonFragment).show(unityFragment).commit();
        }
        //container.setVisibility(View.GONE);
    }

    public void switchFragmentToUnity(){
        //for good measure, not necessary
        mUnityPlayer.resume();
        getFragmentManager().beginTransaction().hide(androidButtonFragment).show(unityFragment).commit();
    }

    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // Native methods that are being called from C# File in Unity scene;
    // can be non-static
    public void callMeNonStatic(String s){
        Log.d("Non-Static", "Non-Static Call from Unity at " + s);
    /*  Easy way
        thisActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.GONE);
            }
        });*/

    //  Harder way (possibly more powerful)
        getFragmentManager().beginTransaction().hide(unityFragment).show(androidButtonFragment).commit();
        //mUnityPlayer.pause();
    }
    // and static;
    public static void callMeStatic(String s){
        Log.d("Static", "Static Call from Unity at " +  s);


    }
    //  Methods from default generated UnityPlayerActivity

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }


    @Override protected void onStop()
    {
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }
}