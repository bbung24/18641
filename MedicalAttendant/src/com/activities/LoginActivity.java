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
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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

		private Button loginBtn;
		private Button registerBtn;
		private Activity activity;
		private EditText id;
		private EditText pwd;
		private Intent mServiceIntent;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login,
					container, false);
			activity = getActivity();
			id = (EditText) rootView.findViewById(R.id.login_id_edit);
			pwd = (EditText) rootView.findViewById(R.id.login_pwd_edit);

			loginBtn = (Button) rootView.findViewById(R.id.login_btn);
			loginBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					//					Intent mainMenuIntent = new Intent(activity, MainMenuActivity.class);
					//					startActivity(mainMenuIntent);
					//					activity.finish();
					checkIdPwd(id.getText().toString(), pwd.getText().toString());
				}});
			registerBtn = (Button) rootView.findViewById(R.id.register_btn);
			registerBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					Intent registerIntent = new Intent(activity, RegisterActivity.class);
					startActivity(registerIntent);
					activity.finish();
				}
			});

			return rootView;
		}

		/**
		 * 	The method check if id and password combination is registered on db.
		 */
		public void checkIdPwd(String id, String pwd){
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put(RemoteClientConstants.LOGIN_ID, id);
			data.put(RemoteClientConstants.LOGIN_PW, pwd);

			Message msg = new Message("Client", RemoteClientConstants.LOGIN, data);

			mServiceIntent = new Intent(getActivity(), RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msg);
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
					Message msg_in = (Message) intent.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);
					if(msg_in == null) {
						Toast.makeText(activity, "internal error", Toast.LENGTH_LONG).show();
					} else {
						// Valid ID and pwd --> login with that user id.
						if (msg_in.getCommand().equals(
								RemoteClientConstants.LOGIN_SUCCESS)) {
							Toast.makeText(activity, "Login Success", 
									Toast.LENGTH_LONG).show();
							String userId = (String) msg_in.getMap().get(RemoteClientConstants.LOGIN_ID);
							String job = (String) msg_in.getMap().get(RemoteClientConstants.LOGIN_SUCCESS);
							int id = (Integer) msg_in.getMap().get(RemoteClientConstants.LOGIN);
							SharedPreferences settings = activity.getSharedPreferences(LocalConstants.PREFS_NAME,0);
							SharedPreferences.Editor editor = settings.edit();
							editor.putString(LocalConstants.USER_ID, userId);
							editor.putString(LocalConstants.JOB, job);
							editor.putInt(LocalConstants.ID, id);
							editor.commit();
							Intent mainMenuIntent;
							if(job.equals(LocalConstants.PATIENT)){
								mainMenuIntent = new Intent(activity,
										MainMenuActivity.class);
								
							} else {
								mainMenuIntent = new Intent(activity,
										DoctorMenuActivity.class);
							}
							startActivity(mainMenuIntent);
							activity.finish();
						}else if (msg_in.getCommand().equals(
								RemoteClientConstants.INTERNAL_FAIL)) {
							Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show();
						}
						// Invalid ID -> Notify using Toast
						else {
							Toast.makeText(activity, "ID and password does not match",
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		}
	}
}
