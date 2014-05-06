package com.activities;

/**	
 * 	S14 18641
 *  Medical Attendant.
 * 	
 * 	@author Sang Rok Shin, Inho Yong
 **/

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CreateCheckUpActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_check_up);

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
		getMenuInflater().inflate(R.menu.create_check_up, menu);
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
		private EditText resultEdit;
		private ListView medLV;
		private Button submitBtn;
		private Activity activity;
		private Intent mServiceIntent;
		private HashMap<String, Object> map_create_checkup;
		private ArrayList<HashMap<String, Object>> medTable;
		private ArrayList<String> medSels;
		private ArrayList<Integer> medSelIDs;
		private HashMap<String, Integer> medMap;
		private ArrayList<String> medAll;
		private MultiCheckBoxAdapter mAdapter;
		private String docID, patID; // UserID

		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{

			View rootView = inflater.inflate(R.layout.fragment_create_check_up,
					container, false);
			activity = getActivity();
			resultEdit = (EditText) rootView
					.findViewById(R.id.create_checkup_result_edit);
			medLV = (ListView) rootView
					.findViewById(R.id.create_checkup_med_lv);
			submitBtn = (Button) rootView
					.findViewById(R.id.create_checkup_submit_btn);

			SharedPreferences settings = activity.getSharedPreferences(
					LocalConstants.PREFS_NAME, 0);
			docID = settings.getString("user_id", "none");

			patID = getActivity().getIntent().getExtras()
					.getString(RemoteClientConstants.CHECKUP_PATIENT_ID);

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
		private void sendMedlistRequest()
		{
			// 1-1 Create Msg.
			Message msg_req_med_list = new Message("Client",
					RemoteClientConstants.REQUEST_MED_LIST, null);

			// 1-2 Send Message
			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msg_req_med_list);
			activity.startService(mServiceIntent);

		}

		/** Send a request with necessary info for creating checkup request. */
		private void sendCreateCheckUpRequest()
		{
			//	1.Put checkup information into amap
			map_create_checkup = new HashMap<String, Object>();

			//	1-1 Add Check Up result
			map_create_checkup.put(RemoteClientConstants.CHECKUP_RESULT,
					resultEdit.getText().toString());

			//	1-2 Add Check Up Medication list.
			// Convert Names in ID for DB add purpose.
			medSels = mAdapter.getSelected();
			
			
			medSelIDs = new ArrayList<Integer>();
			for (String s : medSels)
			{
				medSelIDs.add(medMap.get(s));
			}
			map_create_checkup.put(RemoteClientConstants.MED_CHECKUP_SELECTED,
					medSelIDs);

			//	1-3 Add Doctor_ID
			map_create_checkup.put(RemoteClientConstants.CHECKUP_DOCTOR_ID,
					docID);

			//	1-4 Add Patient_ID
			map_create_checkup.put(RemoteClientConstants.CHECKUP_PATIENT_ID,
					patID);

			String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
					Locale.US).format(new Date());

			//	1-5 Add Date
			map_create_checkup.put(RemoteClientConstants.CHECKUP_DATE,
					timeStamp);

			//	2.Create message.
			Message msg_checkUp = new Message("Client",
					RemoteClientConstants.REQUEST_CREATE_CHECKUP,
					map_create_checkup);

			//	3 Send Message
			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msg_checkUp);
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

		// Broadcast receiver for receiving status updates from the
		// IntentService
		private class ResponseReceiver extends BroadcastReceiver
		{
			// Prevents instantiation
			private ResponseReceiver()
			{
			}

			// Called when the BroadcastReceiver gets an Intent it's registered
			// to receive
			@SuppressWarnings("unchecked")
			@Override
			public void onReceive(Context context, Intent intent)
			{
				if (isAdded())
				{
					// Hear from Server.
					Message msg_id_in = (Message) intent
							.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);

					// null case -> notify user.
					if (msg_id_in == null)
					{
						Toast.makeText(activity, "internal error",
								Toast.LENGTH_LONG).show();
					}
					// med_list request -> populate list view
					else if (msg_id_in.getCommand().equals(
							RemoteClientConstants.REQUEST_MED_LIST))
					{
						medTable = (ArrayList<HashMap<String, Object>>) msg_id_in
								.getMap().get(
										RemoteClientConstants.REQUEST_MED_LIST);

						// medMap <medID, medName>
						medMap = new HashMap<String, Integer>();
						medAll = new ArrayList<String>();

						for (HashMap<String, Object> row : medTable)
						{
							Integer medID = (Integer) row
									.get(RemoteClientConstants.MED_ID);
							String medName = (String) row
									.get(RemoteClientConstants.MED_NAME);
							medAll.add(medName);
							medMap.put(medName, medID);
						}

						mAdapter = new MultiCheckBoxAdapter(activity, medAll);

						medLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
						medLV.setAdapter(mAdapter);
						medLV.setOnItemClickListener((new OnItemClickListener()
						{
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3)
							{
								mAdapter.setChecked(position);
								mAdapter.notifyDataSetChanged();
							}
						}));
					}
					// create_checkup request -> toast msg -> back to
					// doctor_main_menu.
					else if (msg_id_in.getCommand().equals(
							RemoteClientConstants.REQUEST_CREATE_CHECKUP))
					{
						Intent mainMenuIntent = new Intent(activity,
								DoctorMenuActivity.class);
						startActivity(mainMenuIntent);
						activity.finish();
					}
				}
			}
		}

		/** Custom Adapter to show list with checkbox and allow multiple selection. */
		private class MultiCheckBoxAdapter extends BaseAdapter
		{
			private ViewHolder viewHolder = null;
			private LayoutInflater inflater = null;
			private ArrayList<String> list = new ArrayList<String>();
			private boolean[] isCheckedConfirm;// Keep track of checked box

			public MultiCheckBoxAdapter(Context c, ArrayList<String> list)
			{
				inflater = LayoutInflater.from(c);
				this.list = list;
				this.isCheckedConfirm = new boolean[list.size()];
			}

			public void setChecked(int position)
			{
				isCheckedConfirm[position] = !isCheckedConfirm[position];
			}

			public ArrayList<String> getSelected()
			{
				ArrayList<String> selection = new ArrayList<String>();
				for (int i = 0; i < isCheckedConfirm.length; i++)
				{
					if (isCheckedConfirm[i])
						selection.add(list.get(i));
				}
				return selection;
			}

			@Override
			public int getCount()
			{
				return list.size();
			}

			@Override
			public Object getItem(int arg0)
			{
				return null;
			}

			@Override
			public long getItemId(int arg0)
			{
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View v = convertView;
				if (v == null)
				{
					viewHolder = new ViewHolder();
					// inflate the row view.
					v = inflater.inflate(R.layout.row, null);
					viewHolder.cBox = (CheckBox) v
							.findViewById(R.id.main_check_box);
					v.setTag(viewHolder);
				} else
					viewHolder = (ViewHolder) v.getTag();

				// Disable Checkbox event listener
				viewHolder.cBox.setClickable(false);
				viewHolder.cBox.setFocusable(false);

				// Set text to be input list, and initialize to false.
				viewHolder.cBox.setText(list.get(position));
				viewHolder.cBox.setChecked(isCheckedConfirm[position]);
				return v;
			}
		}

		private class ViewHolder
		{
			private CheckBox cBox = null;
		}
	}
	
}
