package com.example.scheduler.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    private Button themeButton;

    private void onCreate(final Bundle savedInstanceState, final View view){
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);

        themeButton = view.findViewById(R.id.ModeSwitch);
        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });
    }
    /*
    private void onCreate(final Bundle savedInstanceState, final View view){
        super.onCreate(savedInstanceState);
        themeButton = view.findViewById(R.id.ModeSwitch);
        themeButton.setOnClickListener(new View.OnClickListener() {
            val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    when (isNightTheme) {
                Configuration.UI_MODE_NIGHT_YES ->
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    Configuration.UI_MODE_NIGHT_NO ->
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
        }
    }
*/
}