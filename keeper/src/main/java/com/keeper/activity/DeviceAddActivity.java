

package com.keeper.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.keeper.R;
import com.keeper.fragment.device.DeviceAddFragment;
import com.keeper.model.Device;

public class DeviceAddActivity extends BaseActivity {
	
	private boolean mPowered = false;// nasty. we initialize it in onCreate()	
	
	public void setPowState(boolean setConnState)
	{
		mPowered = setConnState;
	}
	
	public boolean getPowState()
	{
		return mPowered;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
/* 		if (savedInstanceState == null)
 		{
			FragmentManager manager = getFragmentManager();
			Fragment fragment = DeviceAddFragment.newInstance();
			manager.beginTransaction().add(android.R.id.content, fragment, DeviceAddFragment.class.getSimpleName()).commit();
		}
*/
		Intent ConnectedStateIntent = getIntent();
		
		boolean setConnState = ConnectedStateIntent.getBooleanExtra("mPowered", false);		
		setPowState(setConnState);
		
		if (savedInstanceState == null)
		{
			FragmentManager manager = getFragmentManager();
			Fragment fragment = DeviceAddFragment.newInstance();
			manager.beginTransaction().add(android.R.id.content, fragment, DeviceAddFragment.class.getSimpleName()).commit();
		}
		else
		{
			
		}
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();  // Always call the superclass method first
		
		if ( getPowState () )
		{
			((TextView) findViewById(R.id.listText81)).setText("Device Deactivation");
			((TextView) findViewById(R.id.listText82)).setText("Device is Active");
			((ImageView)findViewById(R.id.imageView81)).setImageResource(R.drawable.ico_disconnect);
			
		}
		else
		{			
			((TextView) findViewById(R.id.listText81)).setText("Device Activation");
			((TextView) findViewById(R.id.listText82)).setText("Device is Inactive");
			((ImageView)findViewById(R.id.imageView81)).setImageResource(R.drawable.ico_activation);
			
		}
	}
	

	
	public void onRadio1ButtonClicked(View view)
	{
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		
		Intent intent = null;

		// Check which radio button was clicked
		switch (view.getId())
		{
			default:
				
			case R.id.radio0:
			{
				if (checked)					
					intent = new Intent(HomeActivity.SNSBLT_PERKARBRU);
			}
			break;
	
			case R.id.radio1:
			{	
	
				if (checked)
					intent = new Intent(HomeActivity.SNSBLT_ZADKARBRU);
			}
			break;
	
			case R.id.radio2:
			{
				if (checked)
					intent = new Intent(HomeActivity.SNSBLT_PERKARRUB);
			}
			break;
			
			case R.id.radio3:
			{
				if (checked)
					intent = new Intent(HomeActivity.SNSBLT_ZADKARRUB);
			}
			break;			
		}
		
		sendBroadcast(intent);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// Lifehack "SHA". We don't do the work with variable 'device' in here, so force "HomeActivity" to do it 
	    if(keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        // A time to renew the status of current device on main panel of application
	        Intent intent = null;
	        intent = new Intent(HomeActivity.PREREDRAWAL);
			sendBroadcast(intent);
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}
	
	public void onButtonActivationClicked(View view)
	{
		
		Intent intent = null;
		
		if ( ! getPowState () )
		{
			intent = new Intent(HomeActivity.ACTIVATE);
			sendBroadcast(intent);
			
			setPowState(true);
		}
		else
		{			
			intent = new Intent(HomeActivity.DEACTIVATE);
			sendBroadcast(intent);
			
			setPowState(false);
		}

		onResume();
		
	}
	
	
}
