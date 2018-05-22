package com.mklenkgmail.unitytest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.unity3d.player.R;
import com.unity3d.player.UnityPlayer;

// Implementation of Unity inside a Fragment for easy management; TODO: Implementation can be changed
// so that Unity Fragment is replaced by other fragments (although this will possibly throw Unity
// out of the memory, depending how Fragment methods are then implemented);
public class UnityFragment extends Fragment {
    //own Pointer to mUnityPlayer in main activity
    private UnityPlayer mUnityPlayer;

    //empty default constructor for fragment best practices
    public UnityFragment() {
    }

    //initialize once when Fragment is built for the first time in activity
    public static UnityFragment newInstance(UnityPlayer unityPlayer){
        UnityFragment unityFragment = new UnityFragment();
        unityFragment.mUnityPlayer = unityPlayer;

        return unityFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        FrameLayout layout = (FrameLayout) view.findViewById( R.id.frameLayout );
        layout.addView(mUnityPlayer, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        mUnityPlayer.resume();

        return view;
    }
}