

package com.keeper.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keeper.R;

public class NavigationFragment extends MapFragment implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener
{
	private GoogleMap googleMap;
	private Location myLocation;

	private LocationRequest lr;
	private LocationClient lc;

	public static NavigationFragment newInstance()
	{
		NavigationFragment fragment = new NavigationFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		lr = LocationRequest.create();
		lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		lc = new LocationClient(this.getActivity().getApplicationContext(), this, this);
	}
	
	@Override
	public void onResume()
	{
		lc.connect();
		
		super.onResume();
	}
	
	@Override
	public void onPause()
	{
		lc.disconnect();
		
		super.onPause();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		googleMap = getMap();

		googleMap.getUiSettings().setAllGesturesEnabled(true);
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.setMyLocationEnabled(true);

		googleMap.addMarker(new MarkerOptions().position(new LatLng(50.43643323, 30.49944917)).title("Nee Mazukowitz, info@embeddora.com").snippet("Embeddora Tech LLC")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_now)));

	/*	googleMap.addMarker(new MarkerOptions().position(new LatLng(50.435587, 30.500856)).title("Телеканал ТВi").snippet("правдиві новини")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_disconnect))); */
	}

	@Override
	public void onLocationChanged(Location location)
	{
		if (myLocation == null)
		{
			myLocation = location;
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 15);
			googleMap.animateCamera(cameraUpdate);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0)
	{
		
	}

	@Override
	public void onConnected(Bundle arg0)
	{
		lc.requestLocationUpdates(lr, this);
	}

	@Override
	public void onDisconnected()
	{
	}
}
