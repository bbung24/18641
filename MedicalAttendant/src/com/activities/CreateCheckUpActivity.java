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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class CreateCheckUpActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_check_up);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_check_up, menu);
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
		private EditText responseEdit;
        private ListView medicationList;
        private Button submitBtn;
        private Activity activity;
        
        public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_check_up,
					container, false);
			activity = getActivity();
			responseEdit = (EditText) rootView.findViewById(R.id.response_edit);
			medicationList = (ListView) rootView.findViewById(R.id.medication_list);
			submitBtn = (Button) rootView.findViewById(R.id.submit_btn);
			submitBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					// TODO: when submit button is clicked, 
					// create new Check Up data for the related patient
					// Then go back to Main Menu Activity.
					
					Intent mainMenuIntent = new Intent(activity, DoctorMenuActivity.class);
					startActivity(mainMenuIntent);
					activity.finish();
				}
			});
			
			return rootView;
		}
	}

}
