package com.activities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import ws.local.LocalConstants;
import ws.remote.RemoteClientConstants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DistSummActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dist_summ);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dist_summ, menu);
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
		private Activity activity;
		private TextView symptoms;
		private ImageView picView;
		private Button vocViewBtn;
		private Button createCheckUpBtn;
		private boolean mStartPlaying = true;
		private MediaPlayer mPlayer = null;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_dist_summ,
					container, false);
			activity = getActivity();
			@SuppressWarnings("unchecked")
			final HashMap<String, Object> m = 
					(HashMap<String, Object>) activity.getIntent().getSerializableExtra("data");
			
			symptoms = (TextView) rootView.findViewById(R.id.symptom_summ);
			picView = (ImageView) rootView.findViewById(R.id.pic_view);
			vocViewBtn = (Button) rootView.findViewById(R.id.voc_view);
			createCheckUpBtn = (Button) rootView.findViewById(R.id.create_checkups);
			createCheckUpBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					Intent ccu = new Intent(activity, CreateCheckUpActivity.class);
					// Need to pass patient id to create checkups.
					String patient_id = (String) m.get(RemoteClientConstants.DIST_PATIENT_ID);
					ccu.putExtra("patient_id", patient_id);
					startActivity(ccu);
					activity.finish();
				}
			});
			symptoms.setText("Symptoms: " +(String) m.get(RemoteClientConstants.DIST_SYMPTOM));
			byte[] imageByte = (byte[]) m.get(RemoteClientConstants.DIST_PIC_FILE);
			convertByteToFile(LocalConstants.PIC_FILE_LOC+"test.jpg", imageByte);
			File imgFile = new  File(LocalConstants.PIC_FILE_LOC+"test.jpg");
			if(imgFile.exists()){
			    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			    picView.setImageBitmap(myBitmap);
			}
			byte[] voiceByte = (byte[]) m.get(RemoteClientConstants.DIST_VOC_FILE);
			convertByteToFile(LocalConstants.PIC_FILE_LOC+"test.3pg", voiceByte);
			File vocFile = new File(LocalConstants.PIC_FILE_LOC+"test.3pg");
			if(vocFile.exists()){
			    vocViewBtn.setOnClickListener(new OnClickListener(){
			    	public void onClick(View v){
			    		onPlay(mStartPlaying);
						if (mStartPlaying) {
							vocViewBtn.setText("Stop playing");
						} else {
							vocViewBtn.setText("Start playing");
						}
						mStartPlaying = !mStartPlaying;
			    	}
			    });
			}
			
			return rootView;
		}
		
		private void onPlay(boolean start) {
			if (start) {
				startPlaying();
			} else {
				stopPlaying();
			}
		}
		
		private void startPlaying() {
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(LocalConstants.PIC_FILE_LOC+"test.3pg");
				mPlayer.prepare();
				mPlayer.start();
			} catch (IOException e) {
				System.err.print("prepare() failed");
			}
		}

		private void stopPlaying() {
			mPlayer.release();
			mPlayer = null;
		}
		
		private void convertByteToFile(String filename, byte[] b){
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(filename);
				bos = new BufferedOutputStream(fos);
				bos.write(b);
			} catch (FileNotFoundException fnfe) {
				System.err.println("File not found" + fnfe);
				fnfe.printStackTrace();
			} catch (IOException e){
				System.err.println("Error while writing to file" + e);
				e.printStackTrace();
			} finally {
				try {
					if (bos != null) {
						bos.flush();
						bos.close();
					}
				} catch(IOException e){
					System.err.println("Error while closing streams" + e);
					e.printStackTrace();
				}

			}
		}
	}

}
