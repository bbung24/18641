package com.activities;

import java.io.Serializable;
import java.util.HashMap;

import ws.local.LocalConstants;
import ws.remote.Message;
import ws.remote.RemoteClientConstants;
import ws.remote.RemoteClientService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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

		private EditText id;
		private EditText pwd;
		private EditText pwdConfirm;
		private EditText age;
		private EditText zip;
		private RadioGroup job;
		private Button registerBtn;
		private Activity activity;
		private HashMap<String, Object> reg_map;
		private Intent mServiceIntent;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);

			reg_map = new HashMap<String, Object>();
			activity = getActivity();
			id = (EditText) rootView.findViewById(R.id.register_id_edit);
			pwd = (EditText) rootView.findViewById(R.id.register_pwd_edit);
			pwdConfirm = (EditText) rootView
					.findViewById(R.id.register_confirm_edit);
			age = (EditText) rootView.findViewById(R.id.register_age_edit);
			zip = (EditText) rootView.findViewById(R.id.register_zip_edit);
			job = (RadioGroup) rootView.findViewById(R.id.register_job_radioBT);
			job.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId == R.id.register_radio_doctor)
						reg_map.put(
								RemoteClientConstants.REGISTSER_INFO_JOB,
								RemoteClientConstants.REGISTER_JOB_DOCTOR);
					else if (checkedId == R.id.register_radio_patient)
						reg_map.put(
								RemoteClientConstants.REGISTSER_INFO_JOB,
								RemoteClientConstants.REGISTER_JOB_PATIENT);
				}
			});

			registerBtn = (Button) rootView.findViewById(R.id.register);
			registerBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					registerBtnWork();
				}
			});
			return rootView;
		}

		public void registerBtnWork() {
			// TODO: check if that id exists within the database.
			System.out.println("ID: " + id.getText().toString());
			if(id.getText().toString().equals("")){
				Toast.makeText(activity, "Please write id", 
						Toast.LENGTH_SHORT).show();
				return;
			} else if(pwd.getText().toString().equals("")){
				Toast.makeText(activity, "Please write password", 
						Toast.LENGTH_SHORT).show();
				return;
			} else if (pwdConfirm.getText().toString().equals("")){
				Toast.makeText(activity, "Please write password confirm",
						Toast.LENGTH_SHORT).show();
			} else if(age.getText().toString().equals("")){
				Toast.makeText(activity, "Please write age", 
						Toast.LENGTH_SHORT).show();
				return;
			} else if(zip.getText().toString().equals("")){
				Toast.makeText(activity, "Please write zip_code", 
						Toast.LENGTH_SHORT).show();
				return;
			} else if (reg_map.get(RemoteClientConstants.REGISTSER_INFO_JOB) == null){
				Toast.makeText(activity, "Please Select a Job",
						Toast.LENGTH_SHORT).show();
				return;
			}


			if (pwd.getText().toString()
					.equals(pwdConfirm.getText().toString())) {
				reg_map.put(RemoteClientConstants.REGISTSER_INFO_ID, id
						.getText().toString());
				reg_map.put(RemoteClientConstants.REGISTSER_INFO_PW,
						pwd.getText().toString());
				reg_map.put(RemoteClientConstants.REGISTSER_INFO_AGE,
						age.getText().toString());
				reg_map.put(RemoteClientConstants.REGISTSER_INFO_ADDRESS,
						zip.getText().toString());

				Message msg_id = new Message("Client",
						RemoteClientConstants.REGISTER,
						reg_map);

				mServiceIntent = new Intent(getActivity(), RemoteClientService.class);
				mServiceIntent.putExtra("message", (Serializable) msg_id);
				activity.startService(mServiceIntent);

				// The filter's action is BROADCAST_ACTION
				IntentFilter mStatusIntentFilter = new IntentFilter(
						RemoteClientConstants.BROADCAST_ACTION);

				// Adds a data filter for the HTTP scheme
				mStatusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

				// Instantiates a new DownloadStateReceiver
				ResponseReceiver mResponseReceiver =
						new ResponseReceiver();
				// Registers the DownloadStateReceiver and its intent filters
				LocalBroadcastManager.getInstance(activity).registerReceiver(
						mResponseReceiver,
						mStatusIntentFilter);
			} else {
				Toast.makeText(activity,
						"Password and Confirm Password don't match",
						Toast.LENGTH_LONG).show();
				return;
			}
		}

		@Override
		public void onStop() {
			super.onStop();
		}

		@Override
		public void onPause() {
			super.onPause();
		}

		@Override
		public void onResume() {
			super.onResume();
		}

		// Broadcast receiver for receiving status updates from the IntentService
		private class ResponseReceiver extends BroadcastReceiver {
			// Prevents instantiation
			private ResponseReceiver() {
			}

			// Called when the BroadcastReceiver gets an Intent it's registered to receive
			@Override
			public void onReceive(Context context, Intent intent) {
				if(isAdded()){ 
					// Hear from Server.
					Message msg_id_in = (Message) intent.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);
					if(msg_id_in == null) {
						Toast.makeText(activity, "internal error", Toast.LENGTH_LONG).show();
					} else {
						// Valid ID -> Register on DB.
						if (msg_id_in.getCommand().equals(
								RemoteClientConstants.REGISTER_SUCCESS)) {
							Toast.makeText(activity, "Sucessfully Registered", 
									Toast.LENGTH_LONG).show();
							String id = (String) reg_map.get(RemoteClientConstants.REGISTSER_INFO_ID);
							String job = (String) reg_map.get(RemoteClientConstants.REGISTSER_INFO_JOB);
							
							SharedPreferences settings = activity.getSharedPreferences(LocalConstants.PREFS_NAME,0);
							SharedPreferences.Editor editor = settings.edit();
							editor.putString(LocalConstants.USER_ID, id);
							editor.putString(LocalConstants.JOB, job);
							editor.commit();

							Intent mainMenuIntent;
							if(job.equals(LocalConstants.PATIENT)){
								mainMenuIntent = new Intent(activity, MainMenuActivity.class);
							} else {
								mainMenuIntent = new Intent(activity, DoctorMenuActivity.class);
							}
							startActivity(mainMenuIntent);
							activity.finish();
						}else if (msg_id_in.getCommand().equals(
								RemoteClientConstants.INTERNAL_FAIL)) {
							Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show();
						}
						// Invalid ID -> Notify using Toast
						else {
							Toast.makeText(activity, "ID already exists",
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		}
	}
}