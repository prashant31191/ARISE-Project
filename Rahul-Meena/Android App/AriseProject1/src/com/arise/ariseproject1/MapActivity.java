package com.arise.ariseproject1;

import com.arise.ariseproject1.classes.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MapActivity extends Activity {

	private GoogleMap map;
	private SessionManager session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();


		LatLng myLocation = new LatLng(session.getLocLat(), session.getLocLong());

		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));

		map.addMarker(new MarkerOptions()
			.title("Me")
		    .snippet("Here I am!")
		    .position(myLocation));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
