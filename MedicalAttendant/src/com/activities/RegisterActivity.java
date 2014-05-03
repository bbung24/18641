package com.activities;

import java.util.HashMap;

import ws.remote.Message;
import ws.remote.RemoteClient;
import ws.remote.RemoteClientConstants;
import ws.remote.RemoteClientService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
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
import android.widget.RadioButton;
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
		private RemoteClientService rcs;

		public PlaceholderFragment() {
		}

		private ServiceConnection mConnection = new ServiceConnection() {

			public void onServiceConnected(ComponentName className, 
					IBinder binder) {
				//RemoteClientService.MyBinder b = (RemoteClientService.MyBinder) binder;
				//rcs = b.getService();
			}

			public void onServiceDisconnected(ComponentName className) {
				rcs = null;
			}
		};

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
			if (reg_map.get(RemoteClientConstants.REGISTSER_INFO_JOB) == null){
				Toast.makeText(activity, "Please Select a Job",
						Toast.LENGTH_LONG).show();
			}

			if (pwd.getText().toString()
					.equals(pwdConfirm.getText().toString())) {


				reg_map.put(RemoteClientConstants.REGISTSER_INFO_ID, id
						.getText().toString());
				reg_map.put(RemoteClientConstants.REGISTSER_INFO_PW,
						pwd.getText().toString());
				reg_map.put(RemoteClientConstants.REGISTSER_INFO_AGE,
						age.getText().toString());
				reg_map.put(RemoteClientConstants.REGISTSER_INFO_ZIP,
						zip.getText().toString());

				Message msg_id = new Message("Client",
						RemoteClientConstants.REGISTER,
						reg_map);

				if (rcs != null) {
					rcs.sendOutput(msg_id);

					// Hear from Server.
					Message msg_id_in = rcs.readInput();

					// Valid ID -> Register on DB.
					if (msg_id_in.getCommand().equals(
							RemoteClientConstants.REGISTER_SUCCESS)) {
						Intent mainMenuIntent = new Intent(activity,
								MainMenuActivity.class);
						startActivity(mainMenuIntent);
						activity.finish();
					}
					// Invalid ID -> Notify using Toast
					else {
						Toast.makeText(getActivity(), "ID already exists",
								Toast.LENGTH_LONG);
					}
				} else {
					Toast.makeText(activity, "Connection to database lost", 
							Toast.LENGTH_LONG).show();
				}
				// Send information to Database.
				//				rc.sendOutput(msg_id);
			} else {
				Toast.makeText(activity,
						"Password and Confirm Password don't match",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
