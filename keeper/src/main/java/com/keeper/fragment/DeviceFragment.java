

package com.keeper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.BroadcastReceiver;






import android.content.IntentFilter;

import com.keeper.R;
import com.keeper.activity.HomeActivity;
import com.keeper.activity.SuspensionDeviceActivity;
import com.keeper.activity.HomeActivity.SectionsPagerAdapter;
import com.keeper.model.Device;

public class DeviceFragment extends Fragment
{
	private Device device;
	
	private String ActualState;// Lifehack "SHA"
	private TextView _deviceState;// Lifehack "SHA"
	private TextView _deviceName;// Lifehack "SHA"
	private TextView _deviceAddress;// Lifehack "SHA"
	private TextView _deviceTime;// Lifehack "SHA"
	private ImageButton _deviceStateButton;// Lifehack "SHA"


	public static DeviceFragment newInstance(Device device)
	{
		DeviceFragment fragment = new DeviceFragment();
		fragment.device = device;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null)
		{
			device = (Device) savedInstanceState.getSerializable("device");
		}
		
		// Lifehack "SHA"
		ActualState = Device.STATE_DISCONNECTED;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		//super.onSaveInstanceState(outState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_device, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		TextView deviceName = (TextView) view.findViewById(R.id.textDeviceName);
		TextView deviceAddress = (TextView) view.findViewById(R.id.textDeviceAddress);
		TextView deviceState = (TextView) view.findViewById(R.id.textDeviceState);
		TextView deviceTime = (TextView) view.findViewById(R.id.textDeviceTime);
		ImageButton deviceStateButton = (ImageButton) view.findViewById(R.id.buttonDeviceState);

		deviceName.setText(device.name);
		deviceAddress.setText(device.address);
		
		_deviceState = deviceState;// Lifehack "SHA". only once needed. actually rework it!
		_deviceStateButton = deviceStateButton;// Lifehack "SHA". only once needed. actually rework it!
		_deviceName = deviceName; 
		_deviceAddress = deviceAddress;
		
		
		switch (device.state)
		{
		case Device.STATE_ON:			
			deviceState.setText(Device.STATE_ON);		// Lh "SHA"	
			deviceState.setTextColor(getResources().getColor(R.color.device_state_on));
			deviceStateButton.setImageResource(R.drawable.button_pause);
			deviceStateButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					startActivity(new Intent(getActivity(), SuspensionDeviceActivity.class));
				}
			});			
			break;
		case Device.STATE_PAUSE:			
			deviceState.setText(Device.STATE_PAUSE);	//Lh "SHA"		
			deviceState.setTextColor(getResources().getColor(R.color.device_state_paused));
			deviceStateButton.setImageResource(R.drawable.button_add);
			break;
		default:
		case Device.STATE_DISCONNECTED:			
			deviceState.setText(Device.STATE_DISCONNECTED); // Lh "SHA"			
			deviceState.setTextColor(getResources().getColor(R.color.device_state_disconnected));
			deviceStateButton.setImageResource(R.drawable.button_save);
			break;
////////////////////////////////////////////////
			
			
		}
		deviceTime.setText("20:00");
		
		// 8. Catching text for renewal
		getActivity().registerReceiver(mTextRenew, RdrwlFlt());

	};
	
	// Lifehack "SHA" . Text renewal intents 
	private final BroadcastReceiver mTextRenew = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			
			if (action.equals(HomeActivity.REALREDRAWAL))
			{
				ActualState = intent.getStringExtra("STATE");
				
				String ActualAddress = intent.getStringExtra("ADDRESS");
						
				if ( ( (String) _deviceAddress.getText()).matches(ActualAddress) )
				{			
					_deviceState.setText(ActualState);
					
					switch (ActualState)
					{
					case Device.STATE_ON:
						_deviceState.setTextColor(getResources().getColor(R.color.device_state_on));
						_deviceStateButton.setImageResource(R.drawable.button_pause);
						break;
					case Device.STATE_PAUSE:
						_deviceState.setTextColor(getResources().getColor(R.color.device_state_paused));
						_deviceStateButton.setImageResource(R.drawable.button_add);
						break;
					// no <default> ! at this moment we have certain <State>, otherwise the whole thing is buggy!  
					case Device.STATE_DISCONNECTED:
						_deviceState.setTextColor(getResources().getColor(R.color.device_state_disconnected));
						_deviceStateButton.setImageResource(R.drawable.button_save);
						break;
					}
				}
						
			}
		}
	};
	
	public void onViewCreated(View view)
	{
		;	
	}
	
	
	// Lifehack "SHA"
	private static IntentFilter RdrwlFlt ()
	{
		final IntentFilter intentFilter = new IntentFilter();		
		
		intentFilter.addAction(HomeActivity.REALREDRAWAL);

		return intentFilter;
	}


}
