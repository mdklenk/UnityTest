package com.mklenkgmail.unitytest;

import android.app.Activity;
import android.app.Fragment;
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

    private static UnityPlayer mUnityPlayer;
    private static FrameLayout container;
    private static Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (FrameLayout) findViewById(R.id.container);

        //to grant access to UI thread in static sub-context
        thisActivity = this;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(container.getId(), new UnityFragment())
                    .commit();
        }

        mUnityPlayer = new UnityPlayer(this);
        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
        mUnityPlayer.init(glesMode, false);

        Button buttonUnity = (Button) findViewById(R.id.buttonUnity);
        buttonUnity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: when switching between Unity and Android view too quickly the mUnityPlayer
                // takes to long to resolve the pause() and resume() methods, making it possible to
                // get stuck in paused Unity screen;
                mUnityPlayer.resume();
                container.setVisibility(View.VISIBLE);

            }
        });
    }

    //TODO: Back button overriden to prevent getting stuck in Unity player
    @Override
    public void onBackPressed(){
        Log.d("Input", "Back button pressed");
        mUnityPlayer.pause();
        container.setVisibility(View.GONE);
    }

    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // Native methods that are being called from C# File in Unity scene;
    // can be called in non-static
    public void callMe(){
        Log.d("Non-Static", "Non-Static Call from Unity");
    }
    // and static context;
    public static void callMe(String s){
        Log.d("Static", "Static Call from Unity" +  s);

        thisActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.GONE);
            }
        });
        mUnityPlayer.pause();

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

    // Implementation of Unity inside a Fragment for easy management; TODO: Implementation can be changed
    // so that Unity Fragment is replaced by other fragments (although this will possibly throw Unity
    // out of the memory;
    public static class UnityFragment extends Fragment {
        //Empty default constructor
        public UnityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_layout, container, false);

            FrameLayout layout = (FrameLayout) view.findViewById( R.id.frameLayout );
            layout.addView(mUnityPlayer, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            mUnityPlayer.resume();

            return view;
        }
    }

}