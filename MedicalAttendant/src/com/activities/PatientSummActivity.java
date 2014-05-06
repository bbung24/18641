package com.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import ws.remote.Message;
import ws.remote.RemoteClientConstants;
import ws.remote.RemoteClientService;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.TextView;
import android.widget.Toast;

public class PatientSummActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_summ);

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
		getMenuInflater().inflate(R.menu.patient_summ, menu);
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
		private Activity activity;
		private ListView medSugLV, dateLV;
		private TextView resultTV;
		private ArrayList<String> medSugList, histDateList;
		private Intent mIntent;
		private Intent mServiceIntent;
		private HashMap<String, Object> checkUp;
		private HashMap<String, Object> reqMap;
		private HashMap<String, ArrayList<String>> dateMedMap;
		private String result;
		private Integer checkUpID;
		private ArrayAdapter<String> medSugAdapter, dateListAdapter;
		private ArrayList<HashMap<String, Object>> medHistMap;
		private ArrayList<String> histLabel;

		// TODO: suggested medicine list
		// 1. Request and Receive suggested medicine list
		// 2. set up listview

		// TODO: medicine list history
		// 1. Request and Receive medicine dosage history
		// 2. group by date.
		// 3. set up listview

		public PlaceholderFragment()
		{
		}

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_patient_summ,
					container, false);
			activity = getActivity();
			medSugLV = (ListView) activity
					.findViewById(R.id.lv_patient_summ_med_sugg);
			dateLV = (ListView) activity
					.findViewById(R.id.lv_patient_summ_med_history);
			resultTV = (TextView) activity
					.findViewById(R.id.tv_patient_summ_result);

			// get checkup, checkUpID from intent.
			mIntent = activity.getIntent();
			checkUp = (HashMap<String, Object>) mIntent
					.getSerializableExtra(RemoteClientConstants.CHECK_UP_ROW);

			checkUpID = (Integer) checkUp.get(RemoteClientConstants.CHECKUP_ID);
			result = (String) checkUp.get(RemoteClientConstants.CHECKUP_RESULT);

			// Set result textview
			resultTV.setText(result);

			// Request suggested medicine.
			requestMedSug();
			requestMedHist();
			setupResponseReceiver();
			
			
			// Request dosage history of medicine
			return rootView;
		}

		/**
		 * The method request suggested medicine for a given checkUpID
		 */
		private void requestMedSug()
		{
			reqMap = new HashMap<String, Object>();
			reqMap.put(RemoteClientConstants.CHECKUP_ID, checkUpID);
			Message msg = new Message("Client",
					RemoteClientConstants.REQUEST_MED_SUG, reqMap);

			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msg);
			activity.startService(mServiceIntent);
		}

		/**
		 * The method request history of medicine dosage for a given checkUpID
		 */
		private void requestMedHist()
		{
			Message msg = new Message("Client",
					RemoteClientConstants.REQUEST_MED_HIST, reqMap);

			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msg);
			activity.startService(mServiceIntent);

		}

		private void setupResponseReceiver()
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
			// Prevents instantiation
			private ResponseReceiver()
			{
			};

			@Override
			public void onReceive(Context context, Intent intent)
			{
				if (isAdded())
				{
					Message msgIn = (Message) intent
							.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);

					if (msgIn == null)
						Toast.makeText(activity, "internal error",
								Toast.LENGTH_LONG).show();
					else if (msgIn.getCommand().equals(
							RemoteClientConstants.REQUEST_MED_SUG))
					{
						medSugList = (ArrayList<String>) msgIn.getMap().get(
								RemoteClientConstants.REQUEST_MED_SUG);

						medSugAdapter = new ArrayAdapter<String>(activity,
								android.R.layout.simple_list_item_1, medSugList);

						// Set Listener to show dates for each medicine.
						medSugLV.setOnItemClickListener(new OnItemClickListener()
						{

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id)
							{
								dateLV.setEmptyView(activity
										.findViewById(android.R.id.empty));

								String med = medSugList.get(position);
								if (dateMedMap.containsKey(med))
								{
									dateListAdapter = new ArrayAdapter<String>(
											activity,
											android.R.layout.simple_list_item_1,
											dateMedMap.get(med));
									dateLV.setAdapter(dateListAdapter);
								} else
								{
									dateListAdapter = new ArrayAdapter<String>(
											activity,
											android.R.layout.simple_list_item_1,
											new ArrayList());
									dateLV.setAdapter(dateListAdapter);
								}

							}
						});
						
						medSugLV.setAdapter(medSugAdapter);


					} else if (msgIn.getCommand().equals(
							RemoteClientConstants.REQUEST_MED_HIST))
					{
						// Rows of taken_relationship
						medHistMap = (ArrayList<HashMap<String, Object>>) msgIn
								.getMap().get(
										RemoteClientConstants.REQUEST_MED_HIST);

						// HashMap<date, list of med taken>
						dateMedMap = new HashMap<String, ArrayList<String>>();

						for (HashMap<String, Object> row : medHistMap)
						{
							String med = (String) row
									.get(RemoteClientConstants.TAKEN_MED_ID);

							// If date is not added, add date and medicine
							if (!dateMedMap.keySet().contains(med))
							{
								dateMedMap
										.put(med,
												new ArrayList<String>(
														(Integer) row
																.get(RemoteClientConstants.TAKEN_DATE)));
							}
							// add medicine on date
							else if (dateMedMap.keySet().contains(med))
							{
								dateMedMap
										.get(med)
										.add((String) row
												.get(RemoteClientConstants.TAKEN_DATE));
							}
						}

					} else
					{
						Toast.makeText(activity, "internal error",
								Toast.LENGTH_LONG).show();
					}

				}
			}

		}

	}

}
