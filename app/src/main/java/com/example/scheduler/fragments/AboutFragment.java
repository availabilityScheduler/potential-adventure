package com.example.scheduler.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scheduler.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.BuildConfig;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {
    private Button themeButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

//        AppCompatDelegate.setDefaultNightMode(
//                AppCompatDelegate.MODE_NIGHT_YES);
//
//        themeButton = v.findViewById(R.id.ModeSwitch);
//        themeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }
//        });
//
//        Fragment duedateFrag = new Fragment();
//        FragmentTransaction ft  = getFragmentManager().beginTransaction();
//        ft.replace(R.id.fragment_container, duedateFrag);
//        ft.addToBackStack(null);
//        ft.commit();
        Element legalElement = new Element();
        legalElement.setTitle("Legal");
        legalElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://uschedule.flycricket.io/privacy.html"));
                startActivity(viewIntent);
            }
        });

        Element developersElement = new Element();
        developersElement.setTitle("UScheduleDevs");

        Element shareElement = new Element();
        shareElement.setTitle("Share");
        shareElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "USchedule");
                    String shareMessage= "\nShare USchedule!\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Sharing options"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });

        Element thirdPartyLicenses = new Element();
        thirdPartyLicenses.setTitle("us");
        AboutPage aboutPage = new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.web_hi_res_512)
                .setDescription("A Weekly calendar matching app!")
                .addItem(new Element("Version " + "1.0.0", R.drawable.image_logo))
                .addGroup("Connect with us")
                .addEmail("tjuscheduler@gmail.com")
                .addPlayStore(getContext().getPackageName())
                .addItem(developersElement)
                .addItem(legalElement)
                .addItem(shareElement);

        if (BuildConfig.FLAVOR.equals("playStore")) {
            aboutPage.addItem(thirdPartyLicenses);
        }

        View aboutPageView = aboutPage.create();
        return aboutPageView;
    }
}

