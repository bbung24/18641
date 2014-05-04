package com.activities;

import java.util.ArrayList;

import ws.remote.RemoteClientConstants;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class DoctorDistActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_dist);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctor_dist, menu);
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
		private ListView diagnosisList;
		private Activity activity;
		private ArrayAdapter<String> adapter;
		private ArrayList<String> distList;
		private Intent mServiceIntent;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_doctor_dist,
					container, false);
			activity = getActivity();
			diagnosisList = (ListView) rootView.findViewById(R.id.diagnosis_list);
			
//			requestDocList();
//			setupResponseReceiver();

			diagnosisList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
//					String selection = (String) parent
//							.getItemAtPosition(position);
//					Intent examIntent = new Intent(activity,
//							.class);
//					examIntent.putExtra(RemoteClientConstants.EXAM_NAME,
//							selection);
//					startActivity(examIntent);
//					activity.finish();
				}
			});
			// TODO: add diagnosis data that was received for this current doctor user.
			// TODO: create simple ListView with the patient's name & date(?).
			
			// TODO: when each patient's diagnosis is clicked, it will move to 
			// CreateCheckUpActivity with that patient's id. 
			
			return rootView;
		}
	}

}
