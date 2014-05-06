package com.activities;

import java.io.File;
import java.io.FileInputStream;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class DistantDiagnosisActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_distant_diagnosis);

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
		getMenuInflater().inflate(R.menu.distant_diagnosis, menu);
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

		private Button submitBtn;
		private Button addPicBtn;
		private Button addVocBtn;
		private EditText symptomText;
		private ListView docLV;
		private Activity activity;
		private Uri fileUri;
		private Intent mServiceIntent;
		private ArrayList<String> doctors;
		private String id;
		private CheckBoxListViewAdapter docListAdapter;
		private String docSelected;

		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(
					R.layout.fragment_distant_diagnosis, container, false);
			activity = getActivity();
			docLV = (ListView) rootView.findViewById(R.id.doctor_listview);
			SharedPreferences settings = activity.getSharedPreferences(
					LocalConstants.PREFS_NAME, 0);
			id = settings.getString("user_id", "none");
			if (id.equals("none"))
			{
				System.out.println("User ID not passed here : " + id);
			} else
			{
				System.out.println("User ID is here :" + id);
			}

			// Set listView for doctors.
			requestDocList();
			setupResponseReceiver();

			symptomText = (EditText) rootView.findViewById(R.id.symptoms_edit);
			addPicBtn = (Button) rootView.findViewById(R.id.picture_btn);
			addPicBtn.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
					Intent cameraIntent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

					fileUri = getOutputMediaFileUri(LocalConstants.MEDIA_TYPE_IMAGE);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

					startActivityForResult(cameraIntent,
							LocalConstants.CAMERA_REQUEST);
				}
			});
			addVocBtn = (Button) rootView.findViewById(R.id.voice_btn);
			addVocBtn.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
					Intent vocRecIntent = new Intent(activity,
							VoiceRecordActivity.class);
					startActivityForResult(vocRecIntent,
							LocalConstants.VOICE_REQUEST);
				}
			});
			submitBtn = (Button) rootView.findViewById(R.id.submit_btn);
			submitBtn.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
					docSelected = docListAdapter.getSelectedDoctor();
					if (docSelected == null)
						Toast.makeText(activity, "Please Selected a doctor",
								Toast.LENGTH_LONG).show();
					else
					{
						saveDist();
						activity.finish();
						Toast.makeText(activity,
								"DistantDiagnosis form was successfully sent",
								Toast.LENGTH_LONG).show();
					}
				}
			});

			return rootView;
		}

		/** Request list of all doctors through service */
		private void requestDocList()
		{
			HashMap<String, Object> mapEmpty = new HashMap<String, Object>();
			Message msgReqDocList = new Message("Client",
					RemoteClientConstants.REQUEST_LIST_DOC_ID, mapEmpty);

			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msgReqDocList);
			activity.startService(mServiceIntent);
		}

		private byte[] convertFileToByte(File file)
		{
			FileInputStream fileInputStream = null;

			byte[] bFile = new byte[(int) file.length()];

			try
			{
				// convert file into array of bytes
				fileInputStream = new FileInputStream(file);
				fileInputStream.read(bFile);
				fileInputStream.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			return bFile;
		}

		private void saveDist()
		{
			HashMap<String, Object> data = new HashMap<String, Object>();
			File image = new File(fileUri.getPath());
			byte[] imageByte = convertFileToByte(image);
			data.put(RemoteClientConstants.DIST_PIC_FILE, imageByte);
			File voice = new File(LocalConstants.VOC_FILE_LOC + "temp_voc.3gp");
			byte[] voiceByte = convertFileToByte(voice);
			data.put(RemoteClientConstants.DIST_VOC_FILE, voiceByte);
			data.put(RemoteClientConstants.DIST_SYMPTOM, symptomText.getText()
					.toString());
			if (id.equals("none"))
			{
				Toast.makeText(activity, "Error in the system",
						Toast.LENGTH_LONG).show();
				return;
			} else
			{
				data.put(RemoteClientConstants.DIST_PATIENT_ID, id);
			}
			data.put(RemoteClientConstants.DIST_DOC_ID, docSelected);
			String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
					Locale.US).format(new Date());
			data.put(RemoteClientConstants.DIST_DATE, timeStamp);
			data.put(RemoteClientConstants.DIST_PIC_LOC, "/" + id + "/IMG_"
					+ timeStamp + ".jpg");
			data.put(RemoteClientConstants.DIST_VOC_LOC, "/" + id + "/"
					+ timeStamp + "_voc.3gp");

			Message msgSaveDist = new Message("Client",
					RemoteClientConstants.SAVE_DIST, data);

			mServiceIntent = new Intent(activity, RemoteClientService.class);
			mServiceIntent.putExtra("message", (Serializable) msgSaveDist);
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

		/** Create a file Uri for saving an image or video */
		private Uri getOutputMediaFileUri(int type)
		{
			return Uri.fromFile(getOutputMediaFile(type));
		}

		/** Create a File for saving an image or video */
		private File getOutputMediaFile(int type)
		{
			// To be safe, you should check that the SDCard is mounted
			// using Environment.getExternalStorageState() before doing this.

			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"MyCameraApp");
			// This location works best if you want the created images to be
			// shared
			// between applications and persist after your app has been
			// uninstalled.

			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists())
			{
				if (!mediaStorageDir.mkdirs())
				{
					System.err.print("failed to create directory");
					return null;
				}
			}

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
					Locale.US).format(new Date());
			File mediaFile;
			if (type == LocalConstants.MEDIA_TYPE_IMAGE)
			{
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "IMG_" + timeStamp + ".jpg");
			} else
			{
				return null;
			}

			return mediaFile;
		}

		public void onActivityResult(int requestCode, int resultCode,
				Intent data)
		{
			if (requestCode == LocalConstants.CAMERA_REQUEST
					&& resultCode == RESULT_OK)
			{
				// photo = (Bitmap) data.getExtras().get("data");
				// imageView.setImageBitmap(photo);
				Toast.makeText(activity,
						"Photo Successfully added to Diagnosis",
						Toast.LENGTH_SHORT).show();
			} else if (requestCode == LocalConstants.VOICE_REQUEST
					&& resultCode == RESULT_OK)
			{
				// photo = (Bitmap) data.getExtras().get("data");
				// imageView.setImageBitmap(photo);
				Toast.makeText(activity,
						"Vocie Record Successfully added to Diagnosis",
						Toast.LENGTH_SHORT).show();
			}
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
			@Override
			public void onReceive(Context context, Intent intent)
			{
				if (isAdded())
				{
					// Hear from Server.
					Message msgIdIn = (Message) intent
							.getSerializableExtra(RemoteClientConstants.BROADCAST_RECEV);

					// null case -> notify user.
					if (msgIdIn == null)
					{
						Toast.makeText(activity, "internal error",
								Toast.LENGTH_LONG).show();
					} else if (msgIdIn.getCommand().equals(
							RemoteClientConstants.REQUEST_LIST_DOC_ID))
					{
						doctors = new ArrayList<String>(msgIdIn.getMap()
								.keySet());
						docListAdapter = new CheckBoxListViewAdapter(
								getActivity(), doctors);

						docLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
						docLV.setAdapter(docListAdapter);
						docLV.setOnItemClickListener(new OnItemClickListener()
						{
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3)
							{
								docListAdapter.setChecked(position);
								docListAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
		}

		/** Custom Adapter to show list with checkbox and allow one selection. */
		private class CheckBoxListViewAdapter extends BaseAdapter
		{
			private ViewHolder viewHolder = null;
			private LayoutInflater inflater = null;
			private ArrayList<String> list = new ArrayList<String>();
			private boolean[] isCheckedConfirm;// Keep track of checked box

			public CheckBoxListViewAdapter(Context c, ArrayList<String> list)
			{
				inflater = LayoutInflater.from(c);
				this.list = list;
				this.isCheckedConfirm = new boolean[list.size()];
			}

			public void setChecked(int position)
			{
				// turn off if already checked
				if (isCheckedConfirm[position])
					isCheckedConfirm[position] = !isCheckedConfirm[position];
				// turn off others, and turn on if not checked.
				else
				{
					for (int i = 0; i < isCheckedConfirm.length; i++)
					{
						if (i == position)
							isCheckedConfirm[i] = !isCheckedConfirm[i];
						else
							isCheckedConfirm[i] = false;
					}
				}
			}

			public String getSelectedDoctor()
			{
				int tempSize = isCheckedConfirm.length;
				for (int b = 0; b < tempSize; b++)
				{
					if (isCheckedConfirm[b])
					{
						return list.get(b);
					}
				}
				// no selection
				return null;
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
