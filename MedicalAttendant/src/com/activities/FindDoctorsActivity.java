package com.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ws.remote.Message;
import ws.remote.RemoteClient;
import ws.remote.RemoteClientConstants;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class FindDoctorsActivity extends FragmentActivity implements
		LocationListener {

	private RemoteClient rc;
	private ListView doctorsList;
	private GoogleMap googleMap;
	private ArrayList<String> dArrayList;
	private HashMap<String, String> map;
	private Set<String> doctorSet;
	private Geocoder coder;
	private List<Address> address;
	private Location myLoc;
	private LocationManager locationManager;
	private String provider;
	private Criteria criteria;
	private SupportMapFragment googleMapFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_doctors);

		googleMapFrag = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_doctors);
		googleMap = googleMapFrag.getMap();

		googleMap.setMyLocationEnabled(true);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		criteria = new Criteria();

		provider = locationManager.getBestProvider(criteria, true);

		myLoc = locationManager.getLastKnownLocation(provider);

		coder = new Geocoder(this);

		// TEST
		String strAdd = "3 Bayard Rd Pittsburgh PA 15213";
		try {
			address = coder.getFromLocationName(strAdd, 1);

		} catch (IOException e) {
			e.printStackTrace();
		}

		//Request list of doctors to the server
		rc = new RemoteClient();
		rc.sendOutput(new Message(null,
				RemoteClientConstants.REQUEST_LIST_ALLDOC, null));
		
		
		Message msg = rc.readInput();
		Set<String> docSet = msg.getMap().keySet();
		//TODO: for loop, convert add -> LatLng addMarker.
		//TODO: Focus on current location.
		
		googleMap.addMarker(new MarkerOptions().position(new LatLng(address
				.get(0).getLatitude(), address.get(0).getLongitude())));

		if (myLoc != null) {
			onLocationChanged(myLoc);
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {

			googleMapFrag = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map_doctors);
			googleMap = googleMapFrag.getMap();
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
		locationManager.requestLocationUpdates(provider, 400, 1, this);

	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			LatLng latLng = new LatLng(latitude, longitude);

			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider" + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled new provider" + provider,
				Toast.LENGTH_SHORT).show();

	}

}
