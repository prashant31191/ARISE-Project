package com.arise.ariseproject1;

import java.util.List;

import com.arise.ariseproject1.adapter.LazyPWLUCSAdapter;
import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.PWLUCS;
import com.arise.ariseproject1.classes.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class RadarFragment extends Fragment{
 
	// UI elements
	private Context context;
	private Spinner s_pwlucs;
	private GoogleMap map;
	private SessionManager session;
	private LazyPWLUCSAdapter peopleAdapter;
	private DisplayImageOptions dioptions;


	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	//since Fragment is Activity dependent you need Activity context in various cases
    @Override
    public void onAttach(Activity activity){
      super.onAttach(activity);
      context = getActivity();
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_radar, container, false);
        
        s_pwlucs = (Spinner)rootView.findViewById(R.id.spinner_fragment_radar_pwlucs);
        map = ((MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();

        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		init();
	}

	private void init() {
		
		dioptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher)
			.imageScaleType(ImageScaleType.NONE)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.cacheOnDisc(true)
			.build();

        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
        
        if(session.getPDValue() == 1){
        	loadRadar(session.getPWLUCS());
        }
	}
	
	public void loadRadar(List<PWLUCS> list){
		final List<PWLUCS> pwlucs = list;
		peopleAdapter = new LazyPWLUCSAdapter(context,1, imageLoader, dioptions);
		peopleAdapter.addAll(pwlucs);
		s_pwlucs.setAdapter(peopleAdapter);
		
		// default show all markers
		showAllPWLUCSMarkers(pwlucs);
		
		s_pwlucs.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if(pos !=0){
					// show dialog for particular person
					PWLUCS p;
						p = pwlucs.get(pos-1);
						showDialog(p);
					
				} else{
					// show location of all the users in pwlucs
						showAllPWLUCSMarkers(pwlucs);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void showAllPWLUCSMarkers(List<PWLUCS> pwlucs){
		
		// clear the map of previous markers
    	map.clear();
    	for(int i=0; i<pwlucs.size();i++){
			PWLUCS p = pwlucs.get(i);
    		map.addMarker(new MarkerOptions()
    		.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_latitude))
            .title(p.getName())
            .snippet("At: "+p.getLocTime())
    		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
    		.position(new LatLng(p.getLocLat(), p.getLoclong())));
    	}
		
	}

	private void showDialog(PWLUCS p){ 
		final PWLUCS person = p;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Select");
		builder
            .setMessage("test");
    builder.setPositiveButton("Locate",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	map.clear();
                	map.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_latitude))
		            .title(person.getName())
		            .snippet("At: "+person.getLocTime())
					.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
					.position(new LatLng(person.getLoclong(), person.getLocLat())));
        
                    dialog.cancel();

                }
            });

    builder.setNeutralButton("Chat",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	Intent intent = new Intent(context, ChatActivity.class);
                	intent.putExtra(CommonUtilities.TAG_CHAT_CASE, 1);
                	intent.putExtra(CommonUtilities.TAG_UID, person.getUid());
                	intent.putExtra(CommonUtilities.TAG_NAME, person.getName());
                	intent.putExtra(CommonUtilities.TAG_IMAGE, person.getImage());
                    context.startActivity(intent);
                    dialog.cancel();

                }
            });

    builder.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                }
            });
		 
		// create alert dialog
		AlertDialog alertDialog = builder.create();

		// show it
		alertDialog.show();
	}

}