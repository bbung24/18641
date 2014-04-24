package com.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainMenuActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
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

		private Button distantDiagnosisBtn;
		private Button checkUpBtn;
		private Button findDoctorsBtn;
		private Activity activity;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_menu,
					container, false);
			activity = getActivity();
			distantDiagnosisBtn = (Button) rootView.findViewById(R.id.distant_diagnosis_btn);
			distantDiagnosisBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					Intent distantDiagnosisIntent = new Intent(activity, DistantDiagnosisActivity.class);
					startActivity(distantDiagnosisIntent);
				}
			});
			checkUpBtn = (Button) rootView.findViewById(R.id.check_up_btn);
			checkUpBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					Intent checkUpIntent = new Intent(activity, CheckUpActivity.class);
					startActivity(checkUpIntent);
				}
			});
			findDoctorsBtn = (Button) rootView.findViewById(R.id.find_doctor_btn);
			findDoctorsBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					Intent findDoctorsIntent = new Intent(activity, FindDoctorsActivity.class);
					startActivity(findDoctorsIntent);
				}
			});
			return rootView;
		}
	}

}
