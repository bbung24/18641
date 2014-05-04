package com.activities;

import java.io.Serializable;
import java.util.ArrayList;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CheckUpActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_up);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_up, menu);
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
		private Activity activity;
		private ListView examinationList;
		private ArrayAdapter<String> adapter;
		private ArrayList<String> examList;
		private Intent mServiceIntent;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_check_up,
					container, false);

			activity = getActivity();

			examinationList = (ListView) rootView
					.findViewById(R.id.list_of_medical_examination);

			requestDocList();
			setupResponseReceiver();

			examinationList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String selection = (String) parent
							.getItemAtPosition(position);
					Intent examIntent = new Intent(activity,
							ExaminationActivity.class);
					examIntent.putExtra(RemoteClientConstants.EXAM_NAME,
							selection);
					startActivity(examIntent);
					activity.finish();
				}
			});

			return rootView;
		}

		/** Request list of all doctors through service */
		private void requestDocList() {
			HashMap<String, Object> data = new HashMap<String, Object>();
			SharedPreferences settings = activity.getSharedPreferences(LocalConstants.PREFS_NAME, 0);
			int id = settings.getInt(LocalConstants.USER_ID, -1);
			if(id == -1) {
				System.err.print("Internal Error");
			} else {
				data.put(RemoteClientConstants.CHECKUP_PATIENT_ID, id);
				Message msgReqDocList = new Message("Client",
						RemoteClientConstants.REQUEST_CHECKUPS, data);

				mServiceIntent = new Intent(activity, RemoteClientService.class);
				mServiceIntent.putExtra("message", (Serializable) msgReqDocList);
				activity.startService(mServiceIntent);
			}
		}

		private void setupResponseReceiver() {
			// The filter's action is BROADCAST_ACTION
			IntentFilter mStatusIntentFilter = new IntentFilter(
					RemoteClientConstants.BROADCAST_ACTION);

			// Adds a data filter for the HTTP scheme
			mStatusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

			// Instantiates a new DownloadStateReceiver
			ResponseReceiver mResponseReceiver = new ResponseReceiver();
			// Registers the DownloadStateReceiver and its intent filters
			LocalBroadcastManager.getInstance(activity).registerReceiver(
					mResponseReceiver, mStatusIntentFilter);

		}

		// Broadcast receiver for receiving status updates from the
		// IntentService
		private class ResponseReceiver extends BroadcastReceiver {
			// Prevents instantiation
			private ResponseReceiver() {
			}

			// Called when the BroadcastReceiver gets an Intent it's registered
			// to receive
			@Override
			public void onReceive(Context context, Intent intent) {
				if(isAdded()){
					// Hear from Server.
					Message msgIdIn = (Message) intent
							.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);

					// null case -> notify user.
					if (msgIdIn == null) {
						Toast.makeText(activity, "internal error", Toast.LENGTH_LONG)
						.show();
					} else if (msgIdIn.getCommand().equals(
							RemoteClientConstants.REQUEST_CHECKUPS)) {
						ArrayList<HashMap<String, Object>> data = 
								(ArrayList<HashMap<String, Object>>) msgIdIn.getMap().get(RemoteClientConstants.REQUEST_CHECKUPS);

						examList = new ArrayList<String>();
						for(int i = 0; i < data.size(); i++){
							examList.add("Examination of Distant Diagnosis of " + data.get(i).get(RemoteClientConstants.CHECKUP_DATE));
						}
						adapter = new ArrayAdapter<String>(getActivity(),
								android.R.layout.simple_list_item_1, examList);
						examinationList.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								String selection = (String) parent
										.getItemAtPosition(position);
								Intent examIntent = new Intent(activity,
										ExaminationActivity.class);
								examIntent.putExtra(RemoteClientConstants.EXAM_NAME,
										selection);
								startActivity(examIntent);
								activity.finish();
							}
						});

						examinationList.setAdapter(adapter);
					}
				}
			}
		}
	}

}
