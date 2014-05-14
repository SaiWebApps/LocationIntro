package edu.cmu.hw4;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

/**
 * Tracks the user's current location and sends it as an SMS upon button press.
 * Also displays user's current location whenever it changes.
 * @author Sairam Krishnan (sbkrishn)
 */
public class MainActivity extends ActionBarActivity {
	private LocationManager lm;
	private LocationListener lc;
	private double latitude;
	private double longitude;
	private final String latitudeHeader = "Latitude: ";
	private final String longitudeHeader = "Longitude: ";
	private TextView longitudeOutput;
	private TextView latitudeOutput;
	private final String PHONE_NUMBER = "12815131263";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	/**
	 * Register listeners to monitor the user's current location.
	 */
	private void createLocListener() {
		lc = new LocationListener() {

			@Override
			public void onLocationChanged(Location newLoc) {
				longitude = newLoc.getLongitude();
				latitude = newLoc.getLatitude();
				longitudeOutput.setText(longitudeHeader + longitude);
				latitudeOutput.setText(latitudeHeader + latitude);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			@Override
			public void onProviderEnabled(String provider) {}
			@Override
			public void onProviderDisabled(String provider) {}
		};
	}

	/**
	 * Initialize fields, a LocationManager, and a LocationListener. 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		longitudeOutput = (TextView) findViewById(R.id.longitudeOutput);
		latitudeOutput = (TextView) findViewById(R.id.latitudeOutput);
		createLocListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lc);
	}

	/**
	 * Send the user's current location in a SMS message.
	 */
	public void sendLocation(View v) {
		try {
			String msg = longitudeHeader + longitude + "\n" + latitudeHeader + latitude;
			SmsManager.getDefault().sendTextMessage(PHONE_NUMBER, null, msg, null, null);
			Toast.makeText(MainActivity.this, "Successfully sent SMS", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}