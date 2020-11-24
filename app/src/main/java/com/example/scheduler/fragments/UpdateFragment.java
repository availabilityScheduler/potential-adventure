package com.example.scheduler.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scheduler.R;
import com.example.scheduler.mainActivities.UpdateNotification;

import static androidx.core.content.ContextCompat.getSystemService;

public class UpdateFragment extends Fragment {

	private String notifTitle;
	private String notifText;
	private NotificationManager mNotificationManager;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		notifTitle = "Schedule changes!";
		notifText = "Your group schedule has updated";

		checkBuildVersion();
		Intent intent= new Intent(getContext(), UpdateNotification.class);
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("Matched!", notifText);
		PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "updateChannel")
				.setSmallIcon(R.drawable.refresh)
				.setContentTitle(notifTitle)
				.setContentText(notifText)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				// Set the intent that will fire when the user taps the notification
				.setContentIntent(pendingIntent)
				.setAutoCancel(true);

		NotificationManager managerCompat = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		managerCompat.notify(1, builder.build());


		return inflater.inflate(R.layout.fragment_update, container, false);
	}

	private void checkBuildVersion() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { /// only newer versions of android have channels
			NotificationChannel update_channel = new NotificationChannel("updateChannel", "Update Chanel 1", NotificationManager.IMPORTANCE_DEFAULT);
			mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.createNotificationChannel(update_channel);

		}
	}
}