package com.activities;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_dist_summ,
					container, false);
			return rootView;
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
