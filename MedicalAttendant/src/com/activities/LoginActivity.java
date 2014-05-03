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
					Intent mainMenuIntent = new Intent(activity, MainMenuActivity.class);
					startActivity(mainMenuIntent);
					activity.finish();
				/*	if(checkIdPwd(id.getText().toString(), pwd.getText().toString()))
					{	
						Intent mainMenuIntent = new Intent(activity, MainMenuActivity.class);
						startActivity(mainMenuIntent);
						activity.finish();
					}
					else
						Toast.makeText(getActivity().getApplicationContext(), "Invalid ID or PW", Toast.LENGTH_LONG).show();*/
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
		public boolean checkIdPwd(String id, String pwd){
			
			RemoteClient rc = new RemoteClient();
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put(RemoteClientConstants.LOGIN_ID, id);
			map.put(RemoteClientConstants.LOGIN_PW, pwd);
			
			Message msg_out = new Message("Client",RemoteClientConstants.LOGIN,map);
			rc.sendOutput(msg_out);
			
			//hear from server. 
			Message msg_in;
			msg_in = rc.readInput();
		
			if(msg_in.getCommand().equals(RemoteClientConstants.LOGIN_FAIL))
				return false;
			else
				return true;
		}
	}

}
