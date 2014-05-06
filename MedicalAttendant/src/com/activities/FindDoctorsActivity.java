package com.activities;

/**	
 * 	S14 18641
 *  Medical Attendant.
 * 	
 * 	@author Sang Rok Shin, Inho Yong
 **/

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ws.remote.Message;
import ws.remote.RemoteClientConstants;
import ws.remote.RemoteClientService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindDoctorsActivity extends FragmentActivity implements
		LocationListener {

	private GoogleMap googleMap;
	private ArrayList<String> list_doc;
	private HashMap<String, Object> map_doc_add;
	private Geocoder coder;
	private Location myLoc;
	private LocationManager locationManager;
	private String provider;
	private Criteria criteria;
	private SupportMapFragment googleMapFrag;
	private Intent mServiceIntent;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_doctors);

		googleMapFrag = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_doctors);
		googleMap = googleMapFrag.getMap();
		
		if (googleMap == null) {
			Toast.makeText(getApplicationContext(),
					"Sorry! unable to create a map", Toast.LENGTH_SHORT)
					.show();
		}

		googleMap.setMyLocationEnabled(true);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		criteria = new Criteria();

		provider = locationManager.getBestProvider(criteria, true);

		myLoc = locationManager.getLastKnownLocation(provider);

		coder = new Geocoder(this);

		// Request list of all doctors.
		requestDocList();
		setupResponseReceiver();

	
		if (myLoc != null) {
			onLocationChanged(myLoc);
		}

	}

	/** Request list of all doctors through service */
	private void requestDocList() {
		HashMap<String, Object> map_empty = new HashMap<String, Object>();
		Message msg_req_docList = new Message("Client",
				RemoteClientConstants.REQUEST_LIST_DOC_ADD, map_empty);

		mServiceIntent = new Intent(this, RemoteClientService.class);
		mServiceIntent.putExtra("message", (Serializable) msg_req_docList);
		this.startService(mServiceIntent);
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

			googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

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

			// Hear from Server.
			Message msg_id_in = (Message) intent
					.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);

			// null case -> notify user.
			if (msg_id_in == null) {
				Toast.makeText(activity, "internal error", Toast.LENGTH_LONG)
						.show();
			}

			else if (msg_id_in.getCommand().equals(
					RemoteClientConstants.REQUEST_LIST_DOC_ADD)) {
				map_doc_add = msg_id_in.getMap();
				list_doc = new ArrayList<String>(map_doc_add.keySet());

				for (String doc : list_doc) {
					String str_add = (String) map_doc_add.get(doc);
					try {
						List<Address> list_add_latlng = coder
								.getFromLocationName(str_add, 1);
						googleMap.addMarker(new MarkerOptions()
								.position(new LatLng(list_add_latlng.get(0)
										.getLatitude(), list_add_latlng.get(0)
										.getLongitude())).title(doc));
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(),
								"Could not locate: " + str_add,
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}
}
