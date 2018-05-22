package com.mklenkgmail.unitytest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unity3d.player.R;
import com.unity3d.player.UnityPlayer;

public class AndroidButtonFragment extends Fragment {
    private UnityPlayer mUnityPlayer;
    Button buttonUnity;
    public AndroidButtonFragment(){}
    //AndroidButtonFragment androidBtnFragment;

    public static AndroidButtonFragment newInstance(UnityPlayer unityPlayer) {
        AndroidButtonFragment androidButtonFragment = new AndroidButtonFragment();
        androidButtonFragment.mUnityPlayer = unityPlayer;
      //  androidBtnFragment = androidButtonFragment;
        return androidButtonFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.btnfragment_layout, container, false);
        Button buttonUnity = (Button) view.findViewById(R.id.btnUnity);
        buttonUnity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnityPlayerActivity activity = (UnityPlayerActivity) getActivity();
                activity.switchFragmentToUnity();


            }
        });


        return view;
    }
}
