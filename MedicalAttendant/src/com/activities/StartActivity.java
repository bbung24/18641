package com.activities;

import ws.local.LocalConstants;
import ws.remote.RemoteClient;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class StartActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment())
			.commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_start, container, false);
			if(isAdded()){
				SharedPreferences settings = getActivity().getSharedPreferences(LocalConstants.PREFS_NAME,0);
				String id = settings.getString(LocalConstants.USER_ID, "none");
				String job = settings.getString(LocalConstants.JOB, "none");
				if(id.equals("none")){
					System.out.println("User ID not passed here : " + id);
					Intent mainIntent = new Intent(getActivity(), LoginActivity.class);
					createTimer(mainIntent);
				} else {
					System.out.println("User ID is here :" + id);
					Intent mainIntent;
					if(job.equals(LocalConstants.PATIENT)){
						mainIntent = new Intent(getActivity(), MainMenuActivity.class);
					} else {
						mainIntent = new Intent(getActivity(), DoctorMenuActivity.class);
					}
					createTimer(mainIntent);
				}
			}
			return rootView;
		}

		public void createTimer(final Intent intent){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(intent);
					getActivity().finish();
				}
			}, 3000);
		}
	}

}
