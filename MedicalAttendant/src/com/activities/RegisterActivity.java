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
		private Button registerBtn;
		private Activity activity;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			activity = getActivity();
			id = (EditText) rootView.findViewById(R.id.register_id_edit);
			pwd = (EditText) rootView.findViewById(R.id.register_pwd_edit);
			pwdConfirm = (EditText) rootView.findViewById(R.id.register_confirm_edit);
			age = (EditText) rootView.findViewById(R.id.register_age_edit);
			registerBtn = (Button) rootView.findViewById(R.id.register_btn);
			registerBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					// TODO: check if that id exists within the database.
					if(pwd.getText().toString().equals(pwdConfirm.getText().toString())){
						// TODO: show dialog that register was successful
						// and implement registering into database method.
						
						// After successful registration.
						Intent mainMenuIntent = new Intent(activity, MainMenuActivity.class);
						startActivity(mainMenuIntent);
						activity.finish();
					}
				}
			});
			return rootView;
		}
	}

}
