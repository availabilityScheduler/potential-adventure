package com.example.scheduler.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scheduler.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AboutFragment extends Fragment {

    private AdView adview1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

//        adview1 = v.findViewById(R.id.adView);
//        MobileAds.initialize(getContext());
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adview1.loadAd(adRequest);
        return inflater.inflate(R.layout.fragment_about, container, false);
    }


}