package com.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ws.remote.Message;
import ws.remote.RemoteClient;
import ws.remote.RemoteClientConstants;
import ws.remote.RemoteClientService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CreateCheckUpActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_check_up);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_check_up, menu);
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
		private EditText resultEdit;
		private ListView medicationList;
		private Button submitBtn;
		private Activity activity;
		private RemoteClient rc;
		private Intent mServiceIntent;
		private HashMap<String, Object> map_create_checkup;
		private ArrayList<String> list_med;
		private ArrayAdapter<String> adapter_list_med;
		private ArrayList<String> list_med_selected;
		
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_create_check_up,
					container, false);
			activity = getActivity();
			resultEdit = (EditText) rootView.findViewById(R.id.response_edit);
			medicationList = (ListView) rootView
					.findViewById(R.id.medication_list);
			submitBtn = (Button) rootView.findViewById(R.id.submit_btn);

			// Request med_list and show in ListView.
			sendMedlistRequest();

			// Set up response receiver.
			setResponseReceiver();
			// Submit to send request ->confirm -> doctor main menu.
			submitBtn.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View view) 
				{
					sendCreateCheckUpRequest();	
				}
			});

			return rootView;
		}

		/** Send a request for list of medicine to server. */
		private void sendMedlistRequest() {
			// 1-1 Create Msg.
			Message msg_req_med_list = new Message("Client",
					RemoteClientConstants.REQUEST_MED_LIST, null);

			// 1-2 Send Message
			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msg_req_med_list);
			activity.startService(mServiceIntent);

		}

		/** Send a request with necessary info for creating checkup request. */
		private void sendCreateCheckUpRequest() {
			// 1. Put checkup information into amap
			map_create_checkup = new HashMap<String, Object>();

			// 1-1 Add Check Up result
			map_create_checkup.put(RemoteClientConstants.CHECKUP_RESULT,
					resultEdit.getText().toString());

			// 1-2 Add Check Up Medication list.
			ArrayList<String> list_med = new ArrayList<String>();
			// add checked med to list_med
			map_create_checkup.put(RemoteClientConstants.CHECKUP_MED_LIST,
					list_med_selected);

			// 1-3 Create message.
			Message msg_checkUp = new Message("Client",
					RemoteClientConstants.REQUEST_CREATE_CHECKUP,
					map_create_checkup);

			// 1-4 Send Message
			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msg_checkUp);
			activity.startService(mServiceIntent);
			
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

		/** The method set ResponseReceiver */
		private void setResponseReceiver() {
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
				if (isAdded()) 
				{
					// Hear from Server.
					Message msg_id_in = (Message) intent.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);
					
					// null case -> notify user.
					if (msg_id_in == null) {
						Toast.makeText(activity, "internal error",
								Toast.LENGTH_LONG).show();
					} 
					// med_list request -> populate list view
					else if(msg_id_in.getCommand().equals(RemoteClientConstants.REQUEST_MED_LIST)) 
					{
						list_med = (ArrayList<String>)msg_id_in.getMap().get(RemoteClientConstants.CHECKUP_MED_LIST);
						adapter_list_med = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_multiple_choice,list_med);
						
						medicationList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
						medicationList.setAdapter(adapter_list_med);
						medicationList.setOnItemClickListener(new OnItemClickListener(){

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id)
							{
								SparseBooleanArray a = medicationList.getCheckedItemPositions();
								
								for(int i = 0; i < a.size(); i++)
								{
									if(a.valueAt(i))
										list_med_selected.add(medicationList.getItemAtPosition(i).toString());
								}
							}});
					}
					//	create_checkup request -> toast msg -> back to doctor_main_menu.
					else if(msg_id_in.getCommand().equals(RemoteClientConstants.REQUEST_CREATE_CHECKUP))
					{
						Intent mainMenuIntent = new Intent(activity,DoctorMenuActivity.class);
						startActivity(mainMenuIntent);
						activity.finish();
					}
				}
			}
		}

	}

}
