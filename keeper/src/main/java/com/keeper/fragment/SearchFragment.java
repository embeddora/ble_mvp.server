


package com.keeper.fragment;

import java.util.zip.Inflater;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.keeper.R;
import com.keeper.activity.BaseActivity;
import com.keeper.activity.BluetoothLeService;
import com.keeper.activity.RadarView;
import com.keeper.activity.HomeActivity;
import com.keeper.activity.SearchActivity;
import com.keeper.fragment.device.DeviceAddFragment;
import com.keeper.model.Device;

public class SearchFragment extends Fragment
{
	
	// Init'g is nasty. we initialize it in onResume()
	private int mIdx = 0;
	
	// Since we use in 2 methods at least let's have it global across current class
	private RadarView mRadar;
	
	public void setIndex(int iIdx)
	{
		mIdx = iIdx;
	}
	
	public int getIndex()
	{
		return mIdx;
	}
	
	public static SearchFragment newInstance()
	{
		SearchFragment fragment = new SearchFragment();
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.radar, container, false);
	}
	
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		
		// Stop RSSI-data-catching filter in <RadarView>
		mRadar.stopOwnRadar(getIndex());
		
		// Intent to stop RSSI Data propagation 
		Intent intentRSSI_UNSIBSCRIBE = new Intent(HomeActivity.RSSI_UNSIBSCRIBE);
		
		// Launch the above intent, which Will be caught&processed inside HomeActivity.mDevsReceiver.onReceive() 
		getActivity().sendBroadcast(intentRSSI_UNSIBSCRIBE);		
		
		return;
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		Intent IdxIntent = getActivity().getIntent();
		
		int iIdx = IdxIntent.getIntExtra("iIdx", 0);
		String sAddr= IdxIntent.getStringExtra("mMAC");
		String sName = IdxIntent.getStringExtra("mName");

		setIndex(iIdx);		
		
		mRadar = (RadarView) getActivity().findViewById(R.id.radarViewDebug);
		
		((TextView) (getActivity().findViewById(R.id.MacAddress)) ) .setText(sAddr);		
		((TextView) (getActivity().findViewById(R.id.HR_name)) ) .setText(sName);
		
		try
		{
			mRadar.startOwnNamedRadar(getIndex(), sName, sAddr); // start dedicated radar for device number <getIndex>
		}
		catch (Exception e)
		{
			// describe yourself 
		}		
	}
}

