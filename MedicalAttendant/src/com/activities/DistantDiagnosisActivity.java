package com.activities;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import ws.local.LocalConstants;
import ws.remote.Message;
import ws.remote.RemoteClient;
import ws.remote.RemoteClientConstants;
import ws.remote.RemoteClientService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class DistantDiagnosisActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_distant_diagnosis);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.distant_diagnosis, menu);
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

		private Button submitBtn;
		private Button addPicBtn;
		private Button addVocBtn;
		private Button medicalDevBtn;
		private EditText symptomText;
		private ListView doctorsList;
		private Activity activity;
		private ArrayAdapter<String> adapter;
		private Uri fileUri;
		private ImageView imageView;
		private Bitmap photo;
		private Intent mServiceIntent;
		private ArrayList<String> doctors;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_distant_diagnosis, container, false);
			activity = getActivity();
			imageView = (ImageView) rootView.findViewById(R.id.imageView1);

			SharedPreferences settings = activity.getSharedPreferences(LocalConstants.PREFS_NAME,0);
			String id = settings.getString("user_id", "none");
			if(id.equals("none")){
				System.out.println("User ID not passed here : " + id);
			} else {
				System.out.println("User ID is here :" + id);
			}

			//Set listView for doctors.
			requestDocList();
			setupResponseReceiver();
			
			doctorsList = (ListView) rootView.findViewById(R.id.doctor_listview);
			doctorsList.setAdapter(adapter);
			//Request a list of all doctor in DB

			symptomText = (EditText) rootView.findViewById(R.id.symptoms_edit);
			addPicBtn = (Button) rootView.findViewById(R.id.picture_btn);
			addPicBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
					
					fileUri = getOutputMediaFileUri(LocalConstants.MEDIA_TYPE_IMAGE); // create a file to save the image
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
				    
					startActivityForResult(cameraIntent, LocalConstants.CAMERA_REQUEST);
				}
			});
			addVocBtn = (Button) rootView.findViewById(R.id.voice_btn);
			addVocBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					// TODO: add voice intent also with specification where to
					// save file.
					// Also, this needs to be included with the report into the
					// database.
					Intent vocRecIntent = new Intent(activity, VoiceRecordActivity.class);
					startActivityForResult(vocRecIntent, LocalConstants.VOICE_REQUEST);
				}
			});
			medicalDevBtn = (Button) rootView
					.findViewById(R.id.medical_device_btn);
			medicalDevBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					// TODO: add medical device bluetooth connection.
				}
			});
			submitBtn = (Button) rootView.findViewById(R.id.submit_btn);
			submitBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					// TODO: submit form will add this report into the database.
					// then this will move to Main Menu.

					// Dialog that report was successfully sent.

					// then move to Main Menu
					Intent mainMenuIntent = new Intent(activity,
							MainMenuActivity.class);
					startActivity(mainMenuIntent);
					activity.finish();
				}
			});

			return rootView;
		}

		/** Request list of all doctors through service */
		private void requestDocList() {
			HashMap<String, Object> mapEmpty = new HashMap<String, Object>();
			Message msgReqDocList = new Message("Client",
					RemoteClientConstants.REQUEST_LIST_DOC_ADD, mapEmpty);

			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msgReqDocList);
			activity.startService(mServiceIntent);
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

				// Hear from Server.
				Message msgIdIn = (Message) intent
						.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);

				// null case -> notify user.
				if (msgIdIn == null) {
					Toast.makeText(activity, "internal error", Toast.LENGTH_LONG)
							.show();
				} else if (msgIdIn.getCommand().equals(
						RemoteClientConstants.REQUEST_LIST_DOC_ADD)) {
					doctors = new ArrayList<String>(msgIdIn.getMap().keySet());
					adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,doctors);
					doctorsList.setAdapter(adapter);
				}
			}
		}
		/** Create a file Uri for saving an image or video */
		private Uri getOutputMediaFileUri(int type){
		      return Uri.fromFile(getOutputMediaFile(type));
		}

		/** Create a File for saving an image or video */
		private File getOutputMediaFile(int type){
		    // To be safe, you should check that the SDCard is mounted
		    // using Environment.getExternalStorageState() before doing this.

		    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "MyCameraApp");
		    // This location works best if you want the created images to be shared
		    // between applications and persist after your app has been uninstalled.

		    // Create the storage directory if it does not exist
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            System.err.print("failed to create directory");
		            return null;
		        }
		    }

		    // Create a media file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		    File mediaFile;
		    if (type == LocalConstants.MEDIA_TYPE_IMAGE){
		        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		        "IMG_"+ timeStamp + ".jpg");
		    } else {
		        return null;
		    }

		    return mediaFile;
		}
		
		public void onActivityResult(int requestCode, int resultCode, Intent data) {  
			if (requestCode == LocalConstants.CAMERA_REQUEST && resultCode == RESULT_OK) {  
				photo = (Bitmap) data.getExtras().get("data"); 
				imageView.setImageBitmap(photo);
				Toast.makeText(activity, "Photo Successfully added to Diagnosis", 
						Toast.LENGTH_SHORT).show();
			}  
		} 
	}

}
