package com.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ws.remote.Message;
import ws.remote.RemoteClient;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class FindDoctorsActivity extends FragmentActivity {

	private RemoteClient rc = new RemoteClient();
	private ListView doctorsList;
	private GoogleMap googleMap;
	private ArrayList<String> dArrayList;
	private HashMap<String, String> map;
	private Set<String> doctorSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_doctors);
		
		googleMap = ((SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map_doctors)).getMap();
		
//		Message input = rc.readInput();
//		map = input.getMap();
//		doctorSet = map.keySet();
		
//		for(String s :  doctorSet)
//		{
//			String docName = s;
//			int zip = Integer.parseInt(map.get(docName));
//			
//			googleMap.addMarker(new MarkerOptions().)
//			
//		}
		
		
		try {
			// Loading map
			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
//			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

}
