package com.activities;

/**	
 * 	S14 18641
 *  Medical Attendant.
 * 	
 * 	@author Sang Rok Shin, Inho Yong
 **/

import ws.local.LocalConstants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

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
		private Button logoutBtn;
		private Activity activity;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_menu,
					container, false);
			activity = getActivity();
			SharedPreferences settings = activity.getSharedPreferences(LocalConstants.PREFS_NAME,0);
			String id = settings.getString("user_id", "none");
			if(id.equals("none")){
				System.out.println("User ID not passed here : " + id);
			} else {
				System.out.println("User ID is here :" + id);
			}
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
			logoutBtn = (Button) rootView.findViewById(R.id.logout_btn);
			logoutBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					SharedPreferences settings = activity.getSharedPreferences(LocalConstants.PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.remove("user_id");
					editor.remove("job");
					editor.commit();
					Toast.makeText(activity, "Successfully logout", Toast.LENGTH_SHORT).show();
					Intent startIntent = new Intent(activity, StartActivity.class);
					startActivity(startIntent);
					activity.finish();
				}
			});
			return rootView;
		}
	}

}
