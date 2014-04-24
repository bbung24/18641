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
import android.widget.EditText;
import android.widget.ListView;

public class DistantDiagnosisActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_distant_diagnosis);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.distant_diagnosis, menu);
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

		private Button submitBtn;
		private Button addPicBtn;
		private Button addVocBtn;
		private Button medicalDevBtn;
		private EditText symptomText;
		private ListView doctorsList;
		private Activity activity;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_distant_diagnosis, container, false);
			activity = getActivity();
			doctorsList = (ListView) rootView.findViewById(R.id.doctors_layout);
			symptomText = (EditText) rootView.findViewById(R.id.symptoms_edit);
			addPicBtn = (Button) rootView.findViewById(R.id.picture_btn);
			addPicBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					//TODO: add picture intent but with specification where to save picture.
					// Also, need to include this saved picture to be saved with this report
					// into the database.
				}
			});
			addVocBtn = (Button) rootView.findViewById(R.id.voice_btn);
			addVocBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					// TODO: add voice intent also with specification where to save file.
					// Also, this needs to be included with the report into the database.
				}
			});
			medicalDevBtn = (Button) rootView.findViewById(R.id.medical_device_btn);
			medicalDevBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					// TODO: add medical device bluetooth connection.
				}
			});
			submitBtn = (Button) rootView.findViewById(R.id.submit_btn);
			submitBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					// TODO: submit form will add this report into the database.
					// then this will move to Main Menu.
					
					// Dialog that report was successfully sent.
					
					// then move to Main Menu
					Intent mainMenuIntent = new Intent(activity, MainMenuActivity.class);
					startActivity(mainMenuIntent);
					activity.finish();
				}
			});
			
			return rootView;
		}
	}

}
