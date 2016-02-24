package com.appvirality.appviralitytestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.appvirality.AppviralityUI;
import com.appvirality.android.AppviralityAPI;

public class MainActivity extends Activity {

	private final String LAUNCHCODE = "appvirality.sampleapp.launchmode";
	private final int REQUEST_CODE = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Show personalized welcome screen for new users
		AppviralityUI.showWelcomeScreen(MainActivity.this, REQUEST_CODE);
		// Get GCM Registration key to enable push notifications.
		//GCMRegistration.registerGCM(getApplicationContext());
        AppviralityAPI.getInstance(getApplicationContext(), new AppviralityAPI.UserInstance() {
			@Override
			public void onInstance(AppviralityAPI instance) {
				String userkey = instance.getUserKey();
				boolean isReferrer = instance.hasReferrer();
				String refcode = instance.getFriendReferralCode();
				Log.i("Userkey: ", userkey + " & HasReferrer: " + isReferrer + " & FriendRefcode: " + refcode);
				// The status of isExistingUser will not be changed until user un-install and install the App again
				boolean isExistingUser = instance.isExistingUser();
				Log.i("AV isExisting User: ",""+ isExistingUser);

			}
		});



		//Get emailid used to distribute the rewards
		Log.i("User EmailID: ", AppviralityAPI.getEmailIdFromAccounts(getApplicationContext()));
		//Option:1 - Launch from custom button i.e from "Invite Friends" or "Refer & Earn" button on your App menu
		findViewById(R.id.growthhack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppviralityUI.showGrowthHack(MainActivity.this, AppviralityUI.GH.Word_of_Mouth);
				//Use LogOut if your App has Login & Logout functionality
				//AppviralityAPI.LogOut(getApplicationContext());
			}
		});

		//Option:1_2 - show custom button if campaign Exists i.e from "Invite Friends" or "Refer & Earn" button on your App menu if campaign exists
		findViewById(R.id.btnGH_IfCampaignExists).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, InviteButtonOnCampaignAvailability.class);
				intent.putExtra(LAUNCHCODE, true);
				startActivity(intent);
			}
		});


		//Option - 2 : Launch Bar (i.e Growth Hack will be launched from a Mini notification)
		findViewById(R.id.launchbar).setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
				intent.putExtra(LAUNCHCODE, true);
				startActivity(intent);
			}
		});

		//Option - 3 : Launch Popup (i.e Growth Hack will be launched from a Popup notification)
		findViewById(R.id.launchpopup).setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
				intent.putExtra(LAUNCHCODE, false);
				startActivity(intent);
			}
		});

		//Registration page
		findViewById(R.id.btnRegister).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Registration.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == REQUEST_CODE) && (resultCode == -1))
		{
			Intent intent = new Intent(MainActivity.this, Registration.class);
			startActivity(intent);

		}


	}

	@Override
	protected void onPause() {
		super.onPause();
		//Little Clean up : use this when you are invoking Growth Hack using
		// "AppviralityUI.showGrowthHack(MainActivity.this, AppviralityUI.GH.Word_of_Mouth);" call
		AppviralityUI.onStop();
	}



}
