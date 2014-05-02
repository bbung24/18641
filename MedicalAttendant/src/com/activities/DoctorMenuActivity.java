package com.activities;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class DoctorMenuActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_menu);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctor_menu, menu);
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
		private Button distantBtn;
		private Button patientsBtn;
		private Activity activity;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_doctor_menu,
					container, false);
			activity = getActivity();
			
			distantBtn = (Button) rootView.findViewById(R.id.distant_diagnosis_report_btn);
			distantBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					Intent distIntent = new Intent(activity, DoctorDistActivity.class);
					startActivity(distIntent);
				}
			});
			patientsBtn = (Button) rootView.findViewById(R.id.patient_statistics_btn);
			patientsBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					Intent patientsIntent = new Intent(activity, PatientsActivity.class);
					startActivity(patientsIntent);
				}
			});
			return rootView;
		}
	}

}
