 
 

package com.keeper.activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import java.util.UUID;







import com.keeper.activity.*;
import com.keeper.R;
import com.keeper.fragment.DeviceFragment;
import com.keeper.model.Device;
import com.keeper.model.History;
import com.keeper.view.CirclePageIndicator;
import com.keeper.*;
import com.keeper.activity.HomeActivity;
import com.keeper.activity.SampleGattAttributes;
import com.keeper.activity.BluetoothLeService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.os.Handler;
import android.os.IBinder;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.app.ListActivity;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.ComponentName;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.DataSetObserver;

@TargetApi(Build.VERSION_CODES.KITKAT)

public class HomeActivity extends Activity implements OnClickListener
{	
	private final static String TAG = HomeActivity.class.getSimpleName();
	
	private SectionsPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private CirclePageIndicator circlePageIndicator;
	
	private BluetoothAdapter mBluetoothAdapter;
	
	private static final int 		REQUEST_ENABLE_BT = 0; // 	// original value : '1'
	private static final long 	    SCAN_PERIOD = 4000;	// Stops scanning after 4 seconds.
	private static final Integer 	SENDON_SENDOFF_TMO = 1000;
	private static final Integer 	RESCAN_TMO = 5000;
	
	public LeDeviceListAdapter mLeDeviceListAdapter;
	
	private boolean mScanning;
	private Handler mHandler;
	
	public ArrayList<Device> devices ;
	
	Device ActiveDevice;
	
	int currentPosition = 0; 
	
	public static final String CapacitiveTouchSensor = "0C:84:DC:D9:43:A6";

	private TextView remRSSI, locRSSI;

	private BluetoothGattCharacteristic mNotifyCharacteristic;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";
	
	public static final String EXTRAS_DEVICE_NAME    = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";


	private TextView textOut;
	
	private String LocationText;
	
	private boolean mSleeping = false;

	private byte bState = 0;		
	
	public long iWaitTime = 0;// private
	
	private boolean mListIsComplete = false;
	
	 

	public static final String PWR_ON = "com.app.receive.message.power_on";	
	public static final String ACTIVATE = "com.app.receive.activate";
	public static final String DEACTIVATE = "com.app.receive.deactivate";
	public static final String RSSI_UNSIBSCRIBE = "com.app.receive.rssi_unsubscribe";
	public static final String PREREDRAWAL = "com.app.receive.pre_redrawal";
	public static final String REALREDRAWAL = "com.app.receive.real_redrawal";	
	
	public static final String BTRY_LVL = "com.app.receive.btry_lvl";// TODO: implement filter, and receiver
	public static final String RECEIVE_MESSAGE = "com.app.receive.receive_message"; // TODO: implement filter, and receiver
	
	public static final String SNSBLT_PERKARBRU = "com.app.receive.perkarbru";
	public static final String SNSBLT_ZADKARBRU = "com.app.receive.zadkarbru";
	public static final String SNSBLT_PERKARRUB = "com.app.receive.perkarrub";
	public static final String SNSBLT_ZADKARRUB = "com.app.receive.zadkarrub";
	
	public static final String PROX = "com.app.receive.prox";
	
	public static final String MUST_BE_RECONNECTED = "com.app.receive.mbr";
	
	private boolean SingularInsertion;
	
		
	// Thing to interact with BTLE peers via GATT  
	Hardware mHardware;
	

	public class mServiceConnectionX implements ServiceConnection 
	{
		int interX;
		
		public mServiceConnectionX(int i)
		{
			// 1. 
			interX = i;
			
			// 2. Check pointers those initialized _before, if any
			
			// 3. Normal work-cycle should not require any destructor or relative sort of things 
		}

		public void onServiceConnected(ComponentName componentName, IBinder service)
		{
			devices.get(interX).mBluetoothLeServiceX = ((BluetoothLeService.LocalBinder) service).getService();

			if (!devices.get(interX).mBluetoothLeServiceX.initialize())
			{
				// TODO: here we're going to deinitialize an entire application. Can we ? 
				finish();
			}
			
			
			devices.get(interX).mDeviceAddressX = mLeDeviceListAdapter.getDevice(interX).getAddress();
			devices.get(interX).mDeviceNameX = mLeDeviceListAdapter.getDevice(interX).getName();
			
			// TODO: find better place, if possible 			
			devices.get(interX).historiesX.add(new History(0, devices.get(interX).mDeviceNameX, Device.STATE_ON, System.currentTimeMillis())); 
			
			devices.get(interX).mBluetoothLeServiceX.connect(devices.get(interX).mDeviceAddressX);
						;
			devices.get(interX).deviceX = devices.get(interX).mBluetoothLeServiceX.deviceAA;
			devices.get(interX).mBluetoothGattX = devices.get(interX).mBluetoothLeServiceX.mBluetoothGattAA;
			
			Toast.makeText( getApplicationContext(), "ADDRESS-"+ Integer.toString(interX) + ": " + devices.get(interX).mBluetoothGattX.getDevice().getAddress().toString(), Toast.LENGTH_LONG).show();

			devices.get(interX).iBtryGrpX = 2;
			devices.get(interX).iDataGrpX = 3;

			devices.get(interX).mConnectedX = true;
		}
		
		@Override
		public void onServiceDisconnected(ComponentName componentName)
		{
			devices.get(interX).mBluetoothLeServiceX = null;
		}		
		
	} // public class mServiceConnectionX implements ServiceConnection	


	@Override
	public void onDestroy()
	{
		super.onDestroy();		
		
		unregisterReceiver(mActivateDeactivatePwron);
		
		unregisterReceiver(mMbrReceiver);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// 1. Render captions.
		ActionBar actionBar = getActionBar();
		
		actionBar.setDisplayShowTitleEnabled(false);
		
		setContentView(R.layout.activity_home);
		
		

		// 2. Determine whether BLE is supported on the device.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
		{
			Toast.makeText(this, "BTLE is not onboard. FINISHING.", Toast.LENGTH_SHORT).show();

			finish();

			return;
		}

		// 3. Get corresponding. system service. ( In API level 18 and above, get a reference to BluetoothAdapter via BluetoothManager )
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

		mBluetoothAdapter = bluetoothManager.getAdapter();

		// 3. Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null)
		{
			Toast.makeText(this, "BTA is null. FINISHING." , Toast.LENGTH_SHORT).show();

			finish();

			return;
		}
		
		// 3-a. initialize the Hardware intermediate class  
		mHardware = new Hardware();
		
		if (null == mHardware)
		{
			Toast.makeText(this, "No hardware intermedia was created. FINISHING." , Toast.LENGTH_SHORT).show();

			finish();

			return;			
		}
///////////////////////////////
/*		devices = new ArrayList<Device>();
		devices.add(new Device(0, "BT LE Device (1)", Device.STATE_ON, "00:11:00:11:00:11" ) );
		devices.add(new Device(1, "BT 4.0 Cli-1 (2)", Device.STATE_DISCONNECTED, "00:11:00:11:00:22" ) );
		devices.add(new Device(2, "BT 4.0 Cli-2 (3)", Device.STATE_DISCONNECTED, "00:11:00:11:00:33" ) );
		devices.add(new Device(2, "Unit under Test (4)", Device.STATE_ON, "00:11:00:11:00:44" ) );
		devices.add(new Device(2, "BlueGIGA dev. (5)", Device.STATE_ON, "00:11:00:11:00:55" ) );
		
		ActiveDevice = devices.get(0);

		viewPager = (ViewPager) findViewById(R.id.pager);
		circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		pagerAdapter = new SectionsPagerAdapter(getFragmentManager(), devices);
		viewPager.setAdapter(pagerAdapter);
		
		circlePageIndicator.setViewPager(viewPager);
		pagerAdapter.notifyDataSetChanged();
*/
///////////////////////////////		
		
		// 3-b. pass him a pointer to a Adapter structure
		mHardware.SetAdapter(mBluetoothAdapter);

		// 4. Enable and check is it's enabled
		try
		{
			mBluetoothAdapter.isEnabled();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Exception on BTA is Enabled. FINISHING." , Toast.LENGTH_SHORT).show();

			finish();

			return;
		}

		// 5. Still not enabled?
		if (!mBluetoothAdapter.isEnabled())
		{
			if (!mBluetoothAdapter.isEnabled())
			{
				//Toast.makeText( getApplicationContext(), " _direct2_ 1 is done", Toast.LENGTH_LONG).show();

				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
			else
				// Useful for understanding on-board BTLE hardware session
				Toast.makeText( getApplicationContext(), " _else_ 2 !_.isEnabled()", Toast.LENGTH_LONG).show();
		}

		// 6. Initializes list view adapter.  remember: class ... extends ListActivity
		mLeDeviceListAdapter = new LeDeviceListAdapter();

		// 7. Initializes Handler for BTLE-devices-detect-loop.
		mHandler = new Handler();

		// 8. Catching intent for Scanning-Of-Bluetooth-Devices
		registerReceiver(mActivateDeactivatePwron, PoweronActivateDeactivateRssiunsFlt());

		// 9. Launch BTLE-air scanning procedure
		scanLeDevice(true);

		// 10. This is to 'catch' disconnected connections. TODO: find better place, in possible
		registerReceiver(mMbrReceiver, MbrFlt());
   		
	}
	
	private final BroadcastReceiver mMbrReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();

			// MBR stands for MUST BE RECONNECTED
			if (MUST_BE_RECONNECTED.equals(action))
			{
				String MacOfDisconnected = intent.getStringExtra("MAC");
				//Toast.makeText( getApplicationContext(), " in MUST_BE_RECONNECTED " + MacOfDisconnected , Toast.LENGTH_LONG).show();

				if ( MacOfDisconnected.equalsIgnoreCase(CapacitiveTouchSensor) )
				{
					Toast.makeText( getApplicationContext(), " will reconnect OUR-FRIEND: " + MacOfDisconnected , Toast.LENGTH_LONG).show();
					scanExactLeDevice(true, MacOfDisconnected);
				}
				else
					Toast.makeText( getApplicationContext(), " will not reconnect ALIEN: " + MacOfDisconnected , Toast.LENGTH_LONG).show();
			}
		} // public void onReceive

	}; // private final BroadcastReceiver
	

	// final final final final final final final ! otherwise the brdcst Intent won't be caught !!!! 
	private final void LaunchAllHallows(Context context, int l)
	{		
		if (null != mLeDeviceListAdapter)
			
			if ( l < mLeDeviceListAdapter.getCount() )
				
					if (null != mLeDeviceListAdapter.getDevice(l))
					{
						devices.get(l).mBluetoothLeServiceX = new BluetoothLeService();
						devices.get(l).gattServiceIntentX = new Intent(getApplicationContext(), BluetoothLeService.class);
						
						mServiceConnectionX mServiceConnection_x_ = new mServiceConnectionX(l); 
						bindService(devices.get(l).gattServiceIntentX, mServiceConnection_x_, BIND_AUTO_CREATE);
						
						devices.get(l).mConnectedDiscoveredX = new mConnectedDiscoveredX(l);
						registerReceiver(devices.get(l).mConnectedDiscoveredX, mConnectedDiscoveredFilter());						
						
						devices.get(l).mDisconnectedX = new mDisconnectedX(l);
						/* 2021-12-01*/
						switch (l)
						{
						case 0:							
							registerReceiver(devices.get(l).mDisconnectedX, DisconnectedFlt1());							
							break;
						case 1:							
							registerReceiver(devices.get(l).mDisconnectedX, DisconnectedFlt2());							
							break;
						case 2:							
							registerReceiver(devices.get(l).mDisconnectedX, DisconnectedFlt3());							
							break;
						default:
							break;						
						}/* */
						
						mManipButtsReceiverX mManipButtsReceiver_x_ = new mManipButtsReceiverX(l);						
						registerReceiver(mManipButtsReceiver_x_, ManipButtsFilter());
						
						devices.get(l).historiesX = new ArrayList<History>();
						
						// Initialize RSSI for current device
						devices.get(l).mRadarX = new RadarView(context);
						try
						{
							devices.get(l).mRadarX.initialize();
						}
						catch (Exception e)
						{
							// Smth went wrong 
						}

					}
	}

	// ACTIVATE-DEACTIVATE PWR_ON receiver 
	private final BroadcastReceiver mActivateDeactivatePwron = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			
			if (action.equals(PREREDRAWAL))
			{				
		        // A time to renew the status of current device on main panel of application
		        Intent intentR = null;	        
		        intentR = new Intent(HomeActivity.REALREDRAWAL);
		        
		        // Lifehack "SHA": a real work behind variable 'device'. 
		        intentR.putExtra("STATE", devices.get(currentPosition).state );
		        intentR.putExtra("ADDRESS", devices.get(currentPosition).mDeviceAddressX );
				sendBroadcast(intentR);				

			}
			else if (action.equals(ACTIVATE))
			{	
				SendOn();
				
				devices.get(currentPosition).state=Device.STATE_ON;				
				
				devices.get(currentPosition).historiesX.add(new History(0, devices.get(currentPosition).mDeviceNameX, Device.STATE_ACTIVTED, System.currentTimeMillis()));
				
				devices.get(currentPosition).mPoweredX = true;
				
			}
			else if (action.equals(DEACTIVATE))
			{
				SendOff();// remember  where the SendOn() is done !
				
				devices.get(currentPosition).state=Device.STATE_PAUSE;
				
				devices.get(currentPosition).historiesX.add(new History(0, devices.get(currentPosition).mDeviceNameX, Device.STATE_DEACTIVTED, System.currentTimeMillis()));
				
				devices.get(currentPosition).mPoweredX = false;
			}
			else if (action.equals(RSSI_UNSIBSCRIBE))
			{				 
				// TODO: re-work parameter list !
				mHardware.Unsubscribe(devices, currentPosition, devices.get(currentPosition).iDataGrpX, mHardware.iRssi);
			}
			else if (action.equals(PWR_ON))
			{
				devices = new ArrayList<Device>();
	
				if (null != mLeDeviceListAdapter)
				{
					int AmountOfDevsFound=  mLeDeviceListAdapter.getCount();
					
					if (AmountOfDevsFound == 0)
					{						
						// purpose of this sleep is to wait a little before next scan      		
						try
						{		
							// Wait-3
							Thread.sleep(RESCAN_TMO, 0);
						}
						catch (Exception e)
						{
							Toast.makeText( getApplicationContext(), " some problem on Wait-3", Toast.LENGTH_LONG).show();
						}
						
						Toast.makeText( getApplicationContext(), "No BTLE devices found on air. Re-scaning...", Toast.LENGTH_LONG).show();
					
						// xxx. Launch BTLE-air scanning procedure  
						scanLeDevice(true);
					}
					else
					{
						// Broadband insertion
						SingularInsertion = false;
						
				//+++		for (int k = 0; k < Math.min(3, AmountOfDevsFound); k++)
						for (int k = 0; k < AmountOfDevsFound; k++)
						{
							if (null != mLeDeviceListAdapter.getDevice(k))
							{
									if (mLeDeviceListAdapter.mLeDevices.get(k).getAddress().equalsIgnoreCase(CapacitiveTouchSensor))

										devices.add(new Device(k, mLeDeviceListAdapter.mLeDevices.get(k).getName(), Device.STATE_DISCONNECTED, mLeDeviceListAdapter.mLeDevices.get(k).getAddress()) );

									else

										devices.add(new Device(k, mLeDeviceListAdapter.mLeDevices.get(k).getName() + " (xxx:" + mLeDeviceListAdapter.mLeDevices.get(k).getAddress().substring(mLeDeviceListAdapter.mLeDevices.get(k).getAddress().length() - 5) + ")", Device.STATE_DISCONNECTED, mLeDeviceListAdapter.mLeDevices.get(k).getAddress()));

									/*if (0 == k)*/
									if (mLeDeviceListAdapter.mLeDevices.get(k).getAddress().equalsIgnoreCase(CapacitiveTouchSensor))
									{
										LaunchAllHallows(context, k);

										ActiveDevice = devices.get(k);
									}
							}
							else
								devices.add(new Device(k, "Устройство _x_", Device.STATE_DISCONNECTED, mLeDeviceListAdapter.mLeDevices.get(k).getAddress()));
						}
					}
					
					viewPager = (ViewPager) findViewById(R.id.pager);
					circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
					pagerAdapter = new SectionsPagerAdapter(getFragmentManager(), devices);
					viewPager.setAdapter(pagerAdapter);
					circlePageIndicator.setViewPager(viewPager);
					pagerAdapter.notifyDataSetChanged();
				}
				
			} // action = PWR_ON
			else
			{
					Toast.makeText( getApplicationContext(), "intent NOT CAUGHT ", Toast.LENGTH_LONG).show();
			}			
		} // public void onReceive

	}; // private final BroadcastReceiver mGattUpdateReceiver

	@Override
	public void onClick(View v)
	{
		currentPosition = viewPager.getCurrentItem();
		
		switch (v.getId())
		{
		case R.id.buttonLeft:
			{
				if (currentPosition == 0)
				{
					currentPosition = pagerAdapter.getCount() - 1;
				}
				else
				{
					currentPosition--;
				}
				viewPager.setCurrentItem(currentPosition, true);
				
				break;
			}
			
		case R.id.buttonRight:
			{
				if (currentPosition == pagerAdapter.getCount() - 1)
				{
					currentPosition = 0;
				}
				else
				{
					currentPosition++;
				}
				viewPager.setCurrentItem(currentPosition, true);
			
				break;
			}
			
		case R.id.tabIndicatorSearch:
			{
				if (  devices.get(currentPosition).address.equalsIgnoreCase(CapacitiveTouchSensor))
				{
					// 1. Start RSSI data for giver device . The <Unsubscribe()> invocation performed via <SearchFragment.onDestroyView>
					// TODO: re-work parameter list
					mHardware.Subscribe(devices, currentPosition, devices.get(currentPosition).iDataGrpX, mHardware.iRssi);

					// 2. Start 'Animation' (Search Activity)
					Intent i = new Intent(getApplicationContext(), SearchActivity.class);
					i.putExtra("mIdx", currentPosition + 1);
					i.putExtra("mMAC", devices.get(currentPosition).mDeviceAddressX);
					i.putExtra("mName", devices.get(currentPosition).mDeviceNameX);
					startActivity(i);
				}
				break;
			}		
			
		case R.id.tabIndicatorNavigation:
			{
				if (  devices.get(currentPosition).address.equalsIgnoreCase(CapacitiveTouchSensor))
				{
					startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
				}
				break;
			}
			
		case R.id.tabIndicatorHistory:// TODO: this is just to to get off: 				setContentView(R.layout.fragment_log);//2014-07-04 
			{
				if (  devices.get(currentPosition).address.equalsIgnoreCase(CapacitiveTouchSensor))
				{
					Intent i = new Intent(getApplicationContext(), HistoryActivity.class );
					Bundle bundle = new Bundle();

					bundle.putSerializable("com.keeper.device", devices.get(currentPosition).historiesX );
					i.putExtras(bundle);

					startActivity(i);
				}
				break;
			}
			
		case R.id.tabIndicatorSettings:
			{
				if (  devices.get(currentPosition).address.equalsIgnoreCase(CapacitiveTouchSensor)) {
					Intent i = new Intent(getApplicationContext(), DeviceAddActivity.class);
					i.putExtra("mPowered", devices.get(currentPosition).mPoweredX);

					startActivity(i);
					break;
				}
			}			
		}
		
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		private ArrayList<Device> devices;

		public SectionsPagerAdapter(FragmentManager fm, ArrayList<Device> devices)
		{
			super(fm);
			this.devices = devices;
		}

		@Override
		public Fragment getItem(int position)
		{
			return DeviceFragment.newInstance(devices.get(position));
		}

		@Override
		public int getCount()
		{			
			return devices.size();
		}
	}
	
	
	@Override
	protected void onStart()
	{
		// TODO: 05-08-2014 : check if REALLY needed ????? 
		super.onStart();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();		
	}
	
	private void scanExactLeDevice(final boolean enable, final String Addr)
	{		
		if (enable)
		{
			// Stops scanning after a predefined scan period.
			mHandler.postDelayed(new Runnable()
			{				
				@Override
				public void run()
				{
					mScanning = false;

					mBluetoothAdapter.stopLeScan(mLeScanCallback);

					invalidateOptionsMenu();
					
					for (int k = 0; k < mLeDeviceListAdapter.getCount(); k++)
					{
						if ( null != devices.get(k) )// TODO: resolve this piece of sh..! 
						if (devices.get(k).mDeviceAddressX.matches(Addr))
						{
							// Kill old disconnect-catcher
							unregisterReceiver(devices.get(k).mDisconnectedX);
							
							// Kill old Connection+Discovery catcher  
							unregisterReceiver(devices.get(k).mConnectedDiscoveredX);// kill old catcher
							
							// ATTENTION: before we do the <new device() > we should finish all de-initialization for broken device.
							devices.set(k, new Device(k, mLeDeviceListAdapter.mLeDevices.get(k).getName(), Device.STATE_DISCONNECTED, mLeDeviceListAdapter.mLeDevices.get(k).getAddress() ));

							
							// Update/redraw status
							Intent intentR = new Intent(HomeActivity.REALREDRAWAL); 
					        intentR.putExtra("STATE", devices.get(k).state );
					        intentR.putExtra("ADDRESS", devices.get(k).address );
							sendBroadcast(intentR);
							

							// Let's start singular initialization from scratch.
							Context contextCrnt = getApplicationContext();														
							
							if (null != mLeDeviceListAdapter)
								
								if (null != mLeDeviceListAdapter.getDevice(k))
								{
									// Insertion of single element
									SingularInsertion = true;
									
									devices.get(k).mBluetoothLeServiceX = new BluetoothLeService();
									devices.get(k).gattServiceIntentX = new Intent(getApplicationContext(), BluetoothLeService.class);
									
									mServiceConnectionX mServiceConnection_x_ = new mServiceConnectionX(k); 
									bindService(devices.get(k).gattServiceIntentX, mServiceConnection_x_, BIND_AUTO_CREATE);
									
									devices.get(k).mConnectedDiscoveredX = new mConnectedDiscoveredX(k);
									registerReceiver(devices.get(k).mConnectedDiscoveredX, mConnectedDiscoveredFilter());
									
									devices.get(k).mDisconnectedX = new mDisconnectedX(k);
									switch (k)
									{
										case 0:							
											registerReceiver(devices.get(k).mDisconnectedX, DisconnectedFlt1());							
											break;
										case 1:							
											registerReceiver(devices.get(k).mDisconnectedX, DisconnectedFlt2());							
											break;
										case 2:							
											registerReceiver(devices.get(k).mDisconnectedX, DisconnectedFlt3());							
											break;
										default:
											break;									
									}
									
									mManipButtsReceiverX mManipButtsReceiver_x_ = new mManipButtsReceiverX(k);						
									registerReceiver(mManipButtsReceiver_x_, ManipButtsFilter());
									
									devices.get(k).historiesX = new ArrayList<History>();
									
									// Initialize RSSI for current device
									devices.get(k).mRadarX = new RadarView(contextCrnt);
									try
									{
										devices.get(k).mRadarX.initialize();
									}
									catch (Exception e)
									{
										// 'Radar' for this peer not initialized, we'll get the exception on attempt to open the radar applet  
									}
								}							
							
								pagerAdapter.notifyDataSetChanged(); // TODO : really needed ?							
						}						
					}						
				}
			}, SCAN_PERIOD);

			mScanning = true;

			mBluetoothAdapter.startLeScan(mLeScanCallback);
		}
		else
		{			
			mScanning = false;

			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		
		invalidateOptionsMenu();
	}
	
	private void scanLeDevice(final boolean enable)
	{
		if (enable)
		{
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable()
			{				
				@Override
				public void run()
				{
					mScanning = false;

					mBluetoothAdapter.stopLeScan(mLeScanCallback);

					invalidateOptionsMenu();

					// Air scanning finished. From 0 up to MAX_DEV devices found. Let's process first 3 of those found. 
					Intent intent2 = new Intent(PWR_ON);
					
					sendBroadcast(intent2);					
					
				}
			}, SCAN_PERIOD);

			mScanning = true;
			
			// Scan period has expired
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		}
		else
		{			
			mScanning = false;

			// Disable scanning BTLE air
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		
		invalidateOptionsMenu();		
	}


	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
	{
		@Override
		public void onLeScan(final  BluetoothDevice device, int rssi, byte[] scanRecord)
		{
			// Main scan-time loop
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					mLeDeviceListAdapter.addDevice(device);

					mLeDeviceListAdapter.notifyDataSetChanged();
				}
			});
		}
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.home, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_add:
				startActivity(new Intent(getApplicationContext(), DeviceAddActivity.class));
				return true;
				
			case R.id.action_settings:
				startActivity(new Intent(getApplicationContext(), AppSettingsActivity.class));
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	// Adapter for holding devices found through scanning.
	private class LeDeviceListAdapter extends BaseAdapter
	{
		private ArrayList<BluetoothDevice> mLeDevices;

		private LayoutInflater mInflator;

		public LeDeviceListAdapter()
		{
			super();

			mLeDevices = new ArrayList<BluetoothDevice>();

			mInflator = HomeActivity.this.getLayoutInflater();
			
		}

		public void addDevice(BluetoothDevice device)
		{
			if (!mLeDevices.contains(device))
			{
				mLeDevices.add(device);
			}
		}

		public BluetoothDevice getDevice(int position)
		{
			return mLeDevices.get(position);
		}

		public void clear()
		{
			mLeDevices.clear();
		}

		@Override
		public int getCount()
		{
			return mLeDevices.size();
		}

		@Override
		public Object getItem(int i)
		{
			return mLeDevices.get(i);
		}

		@Override
		public long getItemId(int i)
		{
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup)
		{
			ViewHolder viewHolder;

			// General ListView optimization code.
			if (view == null)
			{
				view = mInflator.inflate(R.layout.fragment_device, null);
				
				viewHolder = new ViewHolder();
				
				view.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) view.getTag();
			}

			BluetoothDevice device = mLeDevices.get(i);

			final String deviceName = device.getName();

			if (deviceName != null && deviceName.length() > 0)

				viewHolder.deviceName.setText(deviceName);

			else

				viewHolder.deviceAddress.setText(device.getAddress());

			return view;
		}
	}
	
	static class ViewHolder
	{
		TextView deviceName;

		TextView deviceAddress;
	}		
	

	public class mConnectedDiscoveredX extends BroadcastReceiver 
	{
		private int interX;
		
		public mConnectedDiscoveredX(int i)
		{
			// 1. 
			interX = i;
			
			// 2. etc
			
			// 3. etc
		}

		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();

			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action) && (  devices.get(interX).mDeviceAddressX.equalsIgnoreCase(intent.getStringExtra("ADDRESS")) ) )
			{
				Toast.makeText( getApplicationContext(), "CONNECTED-" + Integer.toString(interX) + " :"+intent.getStringExtra("ADDRESS"), Toast.LENGTH_LONG).show();

				devices.get(interX).deviceX 			= devices.get(interX).mBluetoothLeServiceX.deviceAA;
				devices.get(interX).mBluetoothGattX 	= devices.get(interX).mBluetoothLeServiceX.mBluetoothGattAA; 					
				
				devices.get(interX).mConnectedX  = true;				
//+++ 03-12-2021 what for? 				CoordsRedraw(1);//++++
			}
			else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action) && ( devices.get(interX).mDeviceAddressX.equalsIgnoreCase(intent.getStringExtra("ADDRESS")) )  )
			{
				Toast.makeText( getApplicationContext(), "DISCOVERED-" + Integer.toString(interX) + " :"+intent.getStringExtra("ADDRESS"), Toast.LENGTH_LONG).show();
				
				// Put discovered services&characteristics into  'gattCharacteristicData[]'.
				displayGattServicesX(devices.get(interX).mBluetoothLeServiceX.getSupportedGattServices(), interX + 1, context);					
				
				devices.get(interX).mAdoptedX = true;
			}

		} // public void onReceive
		
	} // public class . . . 
	
	
	public class mDisconnectedX extends BroadcastReceiver 
	{
		private int interX;
		
		public mDisconnectedX(int i)
		{
			// 1. 
			interX = i;
			
			// 2. etc
			
			// 3. etc
		}

		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();

			if ( BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)   && ( devices.get(interX).mDeviceAddressX.equalsIgnoreCase(intent.getStringExtra("ADDRESS"))  )   )
			{
				devices.get(interX).mConnectedX  = false;
//+++ 03-12-2021 what for? 				CoordsRedraw(0);//++++

				Toast.makeText( getApplicationContext(), "DISCONNECTED-" + Integer.toString(interX) + " :"+intent.getStringExtra("ADDRESS"), Toast.LENGTH_LONG).show();

				Toast.makeText( getApplicationContext(), "trying to automatically reconnect", Toast.LENGTH_LONG).show();				
				Intent intent4 = new Intent(MUST_BE_RECONNECTED);
				intent4.putExtra("MAC", intent.getStringExtra("ADDRESS") );// TODO: it's a smart reconnection
				sendBroadcast(intent4);				
			}			

		} // public void onReceive
		
	} // public class . . .
	
	
	
	public class mManipButtsReceiverX extends BroadcastReceiver 
	{
		private int interX;
		
		public mManipButtsReceiverX(int i)
		{
			// 1. 
			interX = i;
			
			// 2. etc
			
			// 3. etc
		}

		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			
			if (action.equals(SNSBLT_PERKARBRU))
			{
				onButton41Clk();					
			}
			else if (action.equals(SNSBLT_ZADKARBRU))					
			{
				onButton42Clk();
			}
			else if (SNSBLT_PERKARRUB.equals(action))
			{
				onButton43Clk();
			}
			else if(SNSBLT_ZADKARRUB.equals(action))
			{
				onButton44Clk();
			} 
			else if(PROX.equals(action))
			{
				String ActualCase = intent.getStringExtra("PROX");
				
				if (ActualCase.equals("Good, target located")) // TODO: remove this sch.
				{
					devices.get(currentPosition).historiesX.add(new History(0, devices.get(currentPosition).mDeviceNameX, Device.STATE_APPEARED, System.currentTimeMillis()));
//+++ 03-12-2021 what for? 					CoordsRedraw(1);
					
				}					
				else if (ActualCase.equals("Alert: out of reach")) // TODO: remove this sch. 
				{
					devices.get(currentPosition).historiesX.add(new History(0, devices.get(currentPosition).mDeviceNameX, Device.STATE_DISAPPEARED, System.currentTimeMillis()));
					CoordsRedraw(0);
				}				
			}

		} // public void onReceive
		
	} // public class mServsReceiverX extends BroadcastReceiver
		
		
	
	private void displayGattServicesX(List<BluetoothGattService> gattServices, int i, Context context)
	{
		if (gattServices == null)
			return;

		String uuid = null;

		String unknownServiceString = "F. O. 1";

		String unknownCharaString = "F. O. 2";

		ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
		
		switch (i)
		{
		default:
		case 1:
			//1. Create list to hold GATT-characteristics
			devices.get(0).mGattCharacteristicsX = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
			Toast.makeText( getApplicationContext(), "ArrayList-0 done", Toast.LENGTH_LONG).show();			
			//2. Start LE service and connect filters for given device
			if (! SingularInsertion ) LaunchAllHallows(context, 1);			
			break;
			
		case 2:			
			// 1. Create list to hold GATT-characteristics
			devices.get(1).mGattCharacteristicsX = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
			Toast.makeText( getApplicationContext(), "ArrayList-1 done", Toast.LENGTH_LONG).show();			
			// 2. Start LE service and connect filters for given device
			if (! SingularInsertion ) LaunchAllHallows(context, 2);
			break;
			
		case 3:			
			// 1. Create list to hold GATT-characteristics
			devices.get(2).mGattCharacteristicsX = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();			
			Toast.makeText( getApplicationContext(), "ArrayList-2 done", Toast.LENGTH_LONG).show();			
			break;
		}

		mListIsComplete =  true;
		

		// Loops through available GATT Services.
		for (BluetoothGattService gattService : gattServices)
		{
			HashMap<String, String> currentServiceData = new HashMap<String, String>();

			uuid = gattService.getUuid().toString();

			currentServiceData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));

			currentServiceData.put(LIST_UUID, uuid);

			gattServiceData.add(currentServiceData);

			ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

			ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

			// Loops through available Characteristics.
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics)
			{
				charas.add(gattCharacteristic);

				HashMap<String, String> currentCharaData = new HashMap<String, String>();

				uuid = gattCharacteristic.getUuid().toString();

				currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));

				currentCharaData.put(LIST_UUID, uuid);

				gattCharacteristicGroupData.add(currentCharaData);
			}
			
			devices.get(i-1).mGattCharacteristicsX.add(charas);

			gattCharacteristicData.add(gattCharacteristicGroupData);
		}

		// TODO: you can put here workarounds, such as Switch on <Touch> function, of <RSSI>, or <Battery>. Use SendOn()  Subscribe(iBtryGrp, iBattery)  Subscribe(iDataGrp, iRssi)

	}
	
	
	public void SendOn()
	{
		bState |= 0x01;// Byte: Touch ON		
		mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);
		
		// purpose of this sleep is to let the device (DD3, DD4, and DD5) swallow the byte issued via BTLE chnl. during <SendByteToWritable>    		
		try
		{		
			// Wait-1
			Thread.sleep(SENDON_SENDOFF_TMO, 0);
		}
		catch (Exception e)
		{
			Toast.makeText( getApplicationContext(), " some problem on Wait-1", Toast.LENGTH_LONG).show();
		}

		//TODO: rework parameter list !
		mHardware.Subscribe(devices, currentPosition, devices.get(currentPosition).iDataGrpX, mHardware.iTouch);
	}
	
	public void SendOff()
	{			

		bState &= ~0x01;// Byte: Touch ON		
		mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);		
		
		// purpose of this sleep is to let the device (DD3, DD4, and DD5) swallow the byte issued via BTLE chnl. during <SendByteToWritable>    		
		try
		{		
			// Wait-2
			Thread.sleep(SENDON_SENDOFF_TMO, 0);
		}
		catch (Exception e)
		{
			Toast.makeText( getApplicationContext(), " some problem on Wait-2", Toast.LENGTH_LONG).show();
		}

		// TODO: rework parameter list ! 
		mHardware.Unsubscribe(devices, currentPosition, devices.get(currentPosition).iDataGrpX, mHardware.iTouch);
	}
	
	
	private void clearUI()
	{		
		Toast.makeText( getApplicationContext(), " clearUI() happened", Toast.LENGTH_LONG).show();
	}
	
	
	
	public void onButton41Clk()
	{		
		if (devices.get(currentPosition).mConnectedX)
		{
			Toast.makeText( getApplicationContext(), "Sending <bState &= ~0x18>  to Device", Toast.LENGTH_SHORT).show();
			
			bState &= ~0x18;
			
			mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);		
		}
	}

	public void onButton42Clk()
	{	
		if (devices.get(currentPosition).mConnectedX)
		{
			Toast.makeText( getApplicationContext(), "Sending <bState |= 0x08>  to Device", Toast.LENGTH_SHORT).show();
			
			bState &= ~0x18;
			bState |= 0x08;
			
			mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);	
		}
	}
	
	public void onButton43Clk()
	{	
		if (devices.get(currentPosition).mConnectedX)
		{
			Toast.makeText( getApplicationContext(), "Sending <bState |= 0x10>  to Device", Toast.LENGTH_SHORT).show();
			
			bState &= ~0x18;
			bState |= 0x10;
			
			mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);		
		}
	}
	
	public void onButton44Clk()
	{	
		if (devices.get(currentPosition).mConnectedX)
		{
			Toast.makeText( getApplicationContext(), "Sending <bState |= 0x18>  to Device", Toast.LENGTH_SHORT).show();
			
			bState |= 0x18;
			
			mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);
		}
	}
	
	public void onButton60Clk(View view)
	{	
		if (devices.get(currentPosition).mConnectedX)
		{
			Toast.makeText( getApplicationContext(), "Sending <bState ^= 0x20>  to Device", Toast.LENGTH_SHORT).show();		
			
			bState ^= 0x20;
			
			mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);
		}
	}
	
	
	public void onButton51Clk(View view)
	{	
		if (devices.get(currentPosition).mConnectedX)
		{
			Toast.makeText( getApplicationContext(), "Sending <bState &= ~0x06>  to Device", Toast.LENGTH_SHORT).show();			
			
			bState &= ~0x06;			
			
			mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);
		}
	}
	
	public void onButton52Clk(View view)
	{	
		if (devices.get(currentPosition).mConnectedX)
		{
			Toast.makeText( getApplicationContext(), "Sending <bState |= 0x02>  to Device", Toast.LENGTH_SHORT).show();			
			
			bState &= ~0x06;			
			bState |= 0x02;
			
			mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);
		}
	}
	
	public void onButton53Clk(View view)
	{	
		if (devices.get(currentPosition).mConnectedX)
		{
			Toast.makeText( getApplicationContext(), "Sending <bState |= 0x04>  to Device", Toast.LENGTH_SHORT).show();			
			
			bState &= ~0x06;			
			bState |= 0x04;
			
			mHardware.SendByteToWritable(devices, currentPosition, (byte) bState);
		}
	}
	
	public void onButton54Clk(View view)
	{	
		if (devices.get(currentPosition).mConnectedX) 
		{
			Toast.makeText( getApplicationContext(), "Sending <bState |= 0x06>  to Device", Toast.LENGTH_SHORT).show();			
			
			bState |= 0x06;
			
			mHardware.SendByteToWritable(devices, currentPosition,  (byte) bState);
		}
	}
	
	
	
	private static IntentFilter mConnectedDiscoveredFilter()
	{
		final IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
	
		return intentFilter;
	}
	

	private static IntentFilter ManipButtsFilter()
	{
		final IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(SNSBLT_PERKARBRU);
		intentFilter.addAction(SNSBLT_ZADKARBRU);
		intentFilter.addAction(SNSBLT_PERKARRUB);
		intentFilter.addAction(SNSBLT_ZADKARRUB);
		
		intentFilter.addAction(PROX);
	
		return intentFilter;
	}

	
	private static IntentFilter MbrFlt()
	{
		final IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(MUST_BE_RECONNECTED);
	
		return intentFilter;
	}
	
	
	private static IntentFilter PoweronActivateDeactivateRssiunsFlt ()
	{
		final IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(PWR_ON);
		intentFilter.addAction(ACTIVATE);
		intentFilter.addAction(DEACTIVATE);
		intentFilter.addAction(RSSI_UNSIBSCRIBE);		
		intentFilter.addAction(PREREDRAWAL);

		return intentFilter;
	}
	
	private static IntentFilter DisconnectedFlt1()
	{
		final IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);

		return intentFilter;
	}
	private static IntentFilter DisconnectedFlt2()
	{
		final IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);

		return intentFilter;
	}
	private static IntentFilter DisconnectedFlt3()
	{
		final IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);

		return intentFilter;
	}
	
	private  void CoordsRedraw(int iWhich )
	{
//		yandex2.setGeoPoint(new GeoPoint(Lat, Lon));
//		balloonYandex2.setGeoPoint(new GeoPoint(Lat, Lon));
//
//		if (0 == iWhich)
//		{
//			yandex2.setDrawable(res.getDrawable(R.drawable.ic_handcuffs));
//			balloonYandex2.setText("Здесь сеанс связи с охраняемым предметом был прерван");
//		}
//		else
//		{
//			yandex2.setDrawable(res.getDrawable(R.drawable.ic_greenflg));
//			balloonYandex2.setText("Здесь сеанс связи с охраняемым предметом был установлен");
//		}
//
//		yandex2.setBalloonItem(balloonYandex2);
//		overlay2.addOverlayItem(yandex2);
//		mOverlayManager2.addOverlay(overlay2);
//		mMapController2.showBalloon(balloonYandex2);
		
	}
	
	
	
	private void StoreCoords()
	{
	FileOutputStream outputStream;
	
		try
		{
//		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//		  byte LatDATA[] = new byte[64], LonDATA[] = new byte[64];
//		  
//		  LatDATA = (Double.toString(Lat) + "\n").getBytes();
//		  LonDATA = (Double.toString(Lon) + "\n").getBytes();
//		  
//		  outputStream.write(LatDATA);
//		  outputStream.write(LonDATA);
//		  			
//		  outputStream.close();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		}
		
		
	
	}
	
	private void PickupCoords()
	{
	FileInputStream inputStream;
	
		try
		{
//		  inputStream = openFileInput(filename);			  
//		  
//		  if ( inputStream != null )
//		  {
//		  	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//		  	
//		  	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//		  	
//		  	String receiveString = "";
//		  	
////		  	StringBuilder stringBuilder = new StringBuilder();
//
//		  	if ( (receiveString = bufferedReader.readLine()) != null )
//		  	{		
//		  		LatRestore = Lat = Double.parseDouble(receiveString);
//		  		
//		  		Log.e(TAG, "Lattitide = " + LatRestore );
//		  	}
//		  	
//		  	if ( (receiveString = bufferedReader.readLine()) != null )
//		  	{
//		  		LonRestore = Lon = Double.parseDouble(receiveString);
//		  		
//		  		Log.e(TAG, "Longtitude = " + LonRestore );
//		  	}
//
//		  }
//
//		  inputStream.close();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		}
	
	}
	
}
