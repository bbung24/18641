package com.activities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import ws.remote.Message;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ExaminationActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examination);

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
		getMenuInflater().inflate(R.menu.examination, menu);
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
		private ListView checkUpLV;
		private ListView medSugLV;
		private ArrayList<String> examList, takenList, medSugList;
		ArrayList<Integer> medSugIdList;
		private ListCheckAdapter medAdapter;
		private Button submitBtn;
		private Integer checkUpID;
		private Intent mServiceIntent;
		private HashMap<Integer, String> medMap;
		private ArrayList<HashMap<String, Object>> medTable;

		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_examination,
					container, false);
			checkUpLV = (ListView) rootView.findViewById(R.id.check_up_list);
			activity = getActivity();

			Intent intent = activity.getIntent();
			checkUpID = Integer.parseInt((String) intent.getExtras().get(
					RemoteClientConstants.CHECKUP_ID));

			// TODO: update this list with checkUpList associated with clicked
			// examination.
			// require checkupID, taken_relationship

			medSugLV = (ListView) rootView.findViewById(R.id.medication_list);
			requestMedSug();
			setResponseReceiver();
			// TODO: update this list with medication that doctor put in for
			// clicked examination.
			// Require -> Checkup ID in examination relationship.

			submitBtn = (Button) rootView.findViewById(R.id.submit_btn);
			submitBtn.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					sendTakenMed();
				}

			});
			// TODO: put into checkUpList database for checked medications and
			// dates.
			// then update checkUpList.
			

			return rootView;
		}

		private void sendTakenMed()
		{
			takenList = medAdapter.getSelected();
			String date = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
					.format(new Date());
			ArrayList<HashMap<String, Object>> takenRows = new ArrayList<HashMap<String, Object>>();

			for (String medTaken : takenList)
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(RemoteClientConstants.TAKEN_CHECKUP_ID, checkUpID);
				map.put(RemoteClientConstants.TAKEN_MED_ID, medTaken);
				map.put(RemoteClientConstants.TAKEN_DATE, date);
			}
			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put(RemoteClientConstants.TABLE_TAKEN, takenRows);
			Message msg = new Message("Client",
					RemoteClientConstants.REQUEST_ADD_TAKEN, response);
		}

		private void requestMedSug()
		{
			HashMap<String, Object> req = new HashMap<String, Object>();
			req.put(RemoteClientConstants.CHECKUP_ID, checkUpID);

			Message msg = new Message("Client",
					RemoteClientConstants.REQUEST_MED_SUG, req);

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

		// Broadcast receiver for receiving status updates from the
		// IntentService
		private class ResponseReceiver extends BroadcastReceiver
		{

			@SuppressWarnings("unchecked")
			@Override
			public void onReceive(Context context, Intent intent)
			{
				if (isAdded())
				{
					Message msg = (Message) intent
							.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);

					if (msg == null)
					{
						Toast.makeText(activity, "internal error",
								Toast.LENGTH_LONG).show();
					}

					else if (msg.getCommand().equals(
							RemoteClientConstants.REQUEST_MED_SUG))
					{
						medTable = (ArrayList<HashMap<String, Object>>) msg
								.getMap().get(RemoteClientConstants.TABLE_MED);
						medMap = new HashMap<Integer, String>();

						for (HashMap<String, Object> map : medTable)
						{
							Integer medID = (Integer) map
									.get(RemoteClientConstants.MED_ID);
							String medName = (String) map
									.get(RemoteClientConstants.MED_NAME);
							medMap.put(medID, medName);
						}

						medSugIdList = (ArrayList<Integer>) msg.getMap().get(
								RemoteClientConstants.REQUEST_MED_SUG);
						//List of medicine name
						medSugList = new ArrayList<String>();

						for (Integer i : medSugIdList)
						{
							medSugList.add(medMap.get(i));
						}

						medAdapter = new ListCheckAdapter(activity, medSugList);

						medSugLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
						medSugLV.setAdapter(medAdapter);
						medSugLV.setOnItemClickListener(new OnItemClickListener()
						{

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id)
							{
								medAdapter.setChecked(position);
								medAdapter.notifyDataSetChanged();
							}

						});
					}
				}

			}

		}

		/**
		 * Custom Adapter to show list with checkbox and allow multiple
		 * selection.
		 */
		private class ListCheckAdapter extends BaseAdapter
		{
			private ViewHolder viewHolder = null;
			private LayoutInflater inflater = null;
			private ArrayList<String> list = new ArrayList<String>();
			private boolean[] isCheckedConfirm;// Keep track of checked box

			public ListCheckAdapter(Context c, ArrayList<String> list)
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
