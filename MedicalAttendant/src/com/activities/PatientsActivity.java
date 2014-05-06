package com.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ws.local.LocalConstants;
import ws.remote.Message;
import ws.remote.RemoteClientConstants;
import ws.remote.RemoteClientService;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.os.Build;

public class PatientsActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patients);

		if (savedInstanceState == null)
		{
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patients, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment
	{

		private Intent mServiceIntent;
		private Activity activity;
		private ListView checkUpListView;
		// private ArrayList<String> dateList, checkUpList;
		private ArrayAdapter<String> checkUpListAdapter;
		private ArrayList<HashMap<String, Object>> checkupList;
		// <label, checkupid >
		private HashMap<String, Integer> checkUpLabelIdMap;

		// <checkUpID, entire rowcheckups>
		private HashMap<Integer, Object> checkUpMap;
		private ArrayList<String> labelList;
		private Intent patSummIntent;
		private String userID;
		
		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_patients,
					container, false);
			activity = getActivity();
			checkUpListView = (ListView) rootView
					.findViewById(R.id.patients_list);

			SharedPreferences settings = activity.getSharedPreferences(
					LocalConstants.PREFS_NAME, 0);
			userID = settings.getString("user_id", "none");
			sendPatientListRequest();
			setResponseReceiver();

			return rootView;
		}

		private void sendPatientListRequest()
		{
			// Map contains userid of doctor
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(RemoteClientConstants.CHECKUP_DOCTOR_ID, userID);

			Message msg = new Message("Client",
					RemoteClientConstants.REQUEST_CHECKUPS_DOC, map);

			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msg);
			activity.startService(mServiceIntent);

		}

		/** The method set ResponseReceiver */
		private void setResponseReceiver()
		{
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

		private class ResponseReceiver extends BroadcastReceiver
		{

			@Override
			public void onReceive(Context context, Intent intent)
			{
				if (isAdded())
				{
					// Hear from Server
					Message msgIn = (Message) intent
							.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);

					if (msgIn == null)
					{
						Toast.makeText(activity, "internal error",
								Toast.LENGTH_LONG).show();
					} else if (msgIn.getCommand().equals(
							RemoteClientConstants.REQUEST_CHECKUPS_DOC))
					{
						// ArrayList of HashMap(Rows associated with doctor)
						checkupList = (ArrayList<HashMap<String, Object>>) msgIn
								.getMap()
								.get(RemoteClientConstants.REQUEST_CHECKUPS_DOC);

						// Map to store <CHeckUPLabel, CheckUpID>
						checkUpLabelIdMap = new HashMap<String, Integer>();

						for (HashMap<String, Object> map : checkupList)
						{
							String patientID = (String) map
									.get(RemoteClientConstants.CHECKUP_PATIENT_ID);
							String checkUpDate = (String) map
									.get(RemoteClientConstants.CHECKUP_DATE);

							// KEY -> patientID + checkUpDate
							String label = "Patient: " + patientID + " Date: "
									+ checkUpDate;
							// VALUE -> id of checkUp
							Integer checkUpID = (Integer) map
									.get(RemoteClientConstants.CHECKUP_ID);

							checkUpMap.put(checkUpID, map);
							checkUpLabelIdMap.put(label, checkUpID);
						}
						labelList = new ArrayList<String>(
								checkUpLabelIdMap.keySet());

						// Set ArrayList adapter to show.
						checkUpListAdapter = new ArrayAdapter<String>(activity,
								android.R.layout.simple_list_item_1, labelList);
						checkUpListView
								.setOnItemClickListener(new OnItemClickListener()
								{

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id)
									{
										patSummIntent = new Intent(activity,
												PatientSummActivity.class);

										String label = (String) parent
												.getItemAtPosition(position);
										Integer checkUpID = checkUpLabelIdMap
												.get(label);

										HashMap<String, Object> checkUp = (HashMap<String, Object>) checkUpMap
												.get(checkUpID);

										Toast.makeText(activity, "Selected:"
												+ label, Toast.LENGTH_LONG);

										patSummIntent
												.putExtra(
														RemoteClientConstants.CHECK_UP_ROW,
														checkUp);
									}
								});
						checkUpListView.setAdapter(checkUpListAdapter);

					}

				}
			}

		}

	}

}
