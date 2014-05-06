package com.activities;

/**	
 * 	S14 18641
 *  Medical Attendant.
 * 	
 * 	@author Sang Rok Shin, Inho Yong
 **/

import java.io.IOException;

import ws.local.LocalConstants;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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

public class VoiceRecordActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_record);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.voice_record, menu);
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
		private String mFileName;
		private Button recordBtn;
		private Button playBtn;
		private Button doneBtn;
		private boolean mStartRecording = true;
		private boolean mStartPlaying = true;
		private MediaRecorder mRecorder = null;
		private MediaPlayer mPlayer = null;
		private Activity activity;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_voice_record,
					container, false);
			activity = getActivity();
			mFileName = LocalConstants.VOC_FILE_LOC+"temp_voc.3gp";
			recordBtn = (Button) rootView.findViewById(R.id.record_btn);
			recordBtn.setText("Start recording");
			recordBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onRecord(mStartRecording);
					if (mStartRecording) {
						recordBtn.setText("Stop recording");
					} else {
						recordBtn.setText("Start recording");
					}
					mStartRecording = !mStartRecording;
				}
			});
			playBtn = (Button) rootView.findViewById(R.id.play_btn);
			playBtn.setText("Start playing");
			playBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onPlay(mStartPlaying);
					if (mStartPlaying) {
						playBtn.setText("Stop playing");
					} else {
						playBtn.setText("Start playing");
					}
					mStartPlaying = !mStartPlaying;
				}
			});
			doneBtn = (Button) rootView.findViewById(R.id.done_btn);
			doneBtn.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					activity.finish();
				}
			});
			return rootView;
		}

		private void onRecord(boolean start) {
			if (start) {
				startRecording();
			} else {
				stopRecording();
			}
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
				mPlayer.setDataSource(mFileName);
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

		private void startRecording() {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setOutputFile(mFileName);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			try {
				mRecorder.prepare();
			} catch (IOException e) {
				System.err.print("prepare() failed");
			}

			mRecorder.start();
		}

		private void stopRecording() {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}

		@Override
		public void onPause() {
			super.onPause();
			if (mRecorder != null) {
				mRecorder.release();
				mRecorder = null;
			}

			if (mPlayer != null) {
				mPlayer.release();
				mPlayer = null;
			}
		}
	}

}
