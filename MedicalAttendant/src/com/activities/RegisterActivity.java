package com.activities;

import java.util.HashMap;

import ws.remote.Message;
import ws.remote.RemoteClient;
import ws.remote.RemoteClientConstants;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
		RemoteClient rc;
		private EditText id;
		private EditText pwd;
		private EditText pwdConfirm;
		private EditText age;
		private EditText zip;
		private RadioGroup job;
		private RadioButton job_patient, job_doctor;
		private Button registerBtn;
		private Activity activity;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rc = new RemoteClient();

			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			activity = getActivity();
			id = (EditText) rootView.findViewById(R.id.register_id_edit);
			pwd = (EditText) rootView.findViewById(R.id.register_pwd_edit);
			pwdConfirm = (EditText) rootView
					.findViewById(R.id.register_confirm_edit);
			age = (EditText) rootView.findViewById(R.id.register_age_edit);
			zip = (EditText) rootView.findViewById(R.id.register_zip_edit);
			job = (RadioGroup) rootView.findViewById(R.id.register_job_radioBT);
			job_patient = (RadioButton) rootView
					.findViewById(R.id.register_radio_patient);
			job_doctor = (RadioButton) rootView
					.findViewById(R.id.register_radio_doctor);

			registerBtn = (Button) rootView.findViewById(R.id.register_btn);

			registerBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					// TODO: check if that id exists within the database.
					if (pwd.getText().toString()
							.equals(pwdConfirm.getText().toString())) {

						HashMap<String, String> reg_map = new HashMap<String, String>();
						reg_map.put(RemoteClientConstants.REGISTSER_INFO_ID, id
								.getText().toString());
						reg_map.put(RemoteClientConstants.REGISTSER_INFO_PW,
								pwd.getText().toString());
						reg_map.put(RemoteClientConstants.REGISTSER_INFO_AGE,
								pwd.getText().toString());
						reg_map.put(RemoteClientConstants.REGISTSER_INFO_ZIP,
								zip.getText().toString());

						if (job.getCheckedRadioButtonId() == R.id.register_radio_doctor)
							reg_map.put(
									RemoteClientConstants.REGISTSER_INFO_JOB,
									RemoteClientConstants.REGISTER_JOB_DOCTOR);
						else if (job.getCheckedRadioButtonId() == R.id.register_radio_doctor)
							reg_map.put(
									RemoteClientConstants.REGISTSER_INFO_JOB,
									RemoteClientConstants.REGISTER_JOB_PATIENT);
						else {
							Toast.makeText(activity, "Please Select a Job",
									Toast.LENGTH_LONG);
							return;
						}

						Message msg_id = new Message(null,
								RemoteClientConstants.REGISTER_CHECK_ID,
								reg_map);

						// Send information to Database.
						rc.sendOutput(msg_id);

						// Hear from Server.
						Message msg_id_in = rc.readInput();

						// Valid ID -> Register on DB.
						if (msg_id.getCommand().equals(
								RemoteClientConstants.REGISTER_CHECK_ID_SUCCESS)) {
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
						Toast.makeText(activity,
								"Password and Confirm Password don't match",
								Toast.LENGTH_LONG).show();
					}
				}
			});
			return rootView;
		}
	}

}
