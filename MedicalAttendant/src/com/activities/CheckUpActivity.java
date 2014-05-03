package com.activities;

import java.util.ArrayList;
import java.util.HashMap;

import ws.remote.Message;
import ws.remote.RemoteClient;
import ws.remote.RemoteClientConstants;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

		private ListView examinationList;
		private RemoteClient rc;
		private ArrayAdapter<String> adapter;
		private ArrayList<String> examList;
		private HashMap<String, Object> map;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_check_up,
					container, false);
			examinationList = (ListView) rootView
					.findViewById(R.id.list_of_medical_examination);

			examinationList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				
				}
			});

			map = new HashMap<String, Object>();
			rc = new RemoteClient();
			// User ID
			// map.put(RemoteClientConstants.REGISTSER_INFO_ID, userID);
			// rc.sendOutput( new Message(null,
			// RemoteClientConstants.REQUEST_LIST_EXAM,map) );

			// Message msg = rc.readInput();
			// examList = new ArrayList<String>(msg.getMap().keySet());
			examList = new ArrayList<String>();
			examList.add("1");
			examList.add("2");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");
			examList.add("3");

			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, examList);
			examinationList.setAdapter(adapter);

			// TODO: pull Medical Examination lists from
			// database and put into the ListView.

			return rootView;
		}
	}

}
