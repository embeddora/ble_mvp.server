

package com.keeper.activity;

import com.keeper.R;
import com.keeper.activity.RadarView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;




import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.keeper.activity.HomeActivity;
import com.keeper.activity.SampleGattAttributes;
import com.keeper.fragment.HistoryFragment;



public class BluetoothLeService extends Service implements Serializable
{
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;    
    
    private BluetoothAdapter mBluetoothAdapter;
    
    private String mBluetoothDeviceAddress;

    public String GetBtDeviceAddress()
    {
    		return mBluetoothDeviceAddress; 
    }
    
    public BluetoothGatt mBluetoothGatt;
    
    public int mConnectionState = STATE_DISCONNECTED;

    public static final int STATE_DISCONNECTED = 0;    
    public static final int STATE_CONNECTING = 1;    
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_DISCONNECTING = 3;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";    
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";    
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";    
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";    
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";    
    public final static String ACTION_READ_REMOTE_RSSI = "com.example.bluetooth.le.ACTION_READ_REMOTE_RSSI";    
    public static final String DEVICE_ADDRESS = "deviceAddress";

    public final static UUID UUID_SENSOR_DATA = UUID.fromString(SampleGattAttributes.SENSOR_DATA);    
    public final static UUID UUID_WRITABLE  = UUID.fromString(SampleGattAttributes.U_WRITABLE);    
    public final static UUID UUID_RSSI_MEASUREMENT = UUID.fromString(SampleGattAttributes.RSSI_MEASUREMENT);    
    public final static UUID UUID_BATTERY_STATUS = UUID.fromString(SampleGattAttributes.BATT_ST);       
    
	
    public SoundPool soundPool1;
	
	public int soundID_ding;
	
	private boolean bOutOfReach;
	
	private int iCurrentRSSI_older, iCurrentRSSI_old;
	
	// Current value of Signal strength. TODO: seems to be not needed. TODO: check it up and remove if not needed. 
	public  int iCurrentRSSI;

	// Upper threshold to tell if the device is still nearby. Negative, logarithmic (-dBm)
	public int iThreaholdHigh; 
	
	// Upper threshold to tell if the device is still nearby. Negative, logarithmic (-dB.)
	public int iThreaholdLow;
	
	// Arrays with input/output data for <fast average computation> 
	private double _SkInput[], _SkOutput[];
	
	// you know what  
	private double _SkInputLocal[], _SkOutputLocal[];

	// Globals for <fast average computation>
	private int _n, _i;

	// you know what
	private int _nLocal, _iLocal;
	
	// Boolean flag to notify we've passed at least 1 cycle of <fast average computation>
	public boolean _IdxPassed, _IdxPassedLocal;
	
	int iIdx, iIdxLocal ;
	
	
	// RSSI value of smartphone's antenna  
	private int iCurrentLocalRSSI;
	
	// TODO: normally dimension of Fast-Average-Arrays should be 15
	final private int iBench = 5;// TODO: normally dimension of Fast-Average-Arrays should be 15
	
	
	
    // Implements callback methods for GATT events that the app cares about.  For example, connection change and services discovered.	
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            String intentAction;            
            
            
            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
					mConnectionState = STATE_CONNECTED;
					Intent intentCONNECTED_WITH_INFO = new Intent(ACTION_GATT_CONNECTED);
					intentCONNECTED_WITH_INFO.putExtra("ADDRESS", gatt.getDevice().getAddress() );
					sendBroadcast(intentCONNECTED_WITH_INFO);
					
					// Attempts to discover services after successful connection.
					Log.i(TAG, "Connected to GATT server. Attempting to start service discovery:" +  mBluetoothGatt.discoverServices());
					
					Intent intentRSSI_CONNECTED = new Intent(RadarView.RSSI_CONNECTED);
					intentRSSI_CONNECTED.putExtra("RSSI_CONNECTED", STATE_CONNECTED);
					sendBroadcast(intentRSSI_CONNECTED);
            }
            else
              if (newState == BluetoothProfile.STATE_DISCONNECTED)
              {
					mConnectionState = STATE_DISCONNECTED;
					Intent intentDISCONNECTED_WITH_INFO = new Intent(ACTION_GATT_DISCONNECTED);
					intentDISCONNECTED_WITH_INFO.putExtra("ADDRESS", gatt.getDevice().getAddress() );
					sendBroadcast(intentDISCONNECTED_WITH_INFO);
					  
					Log.i(TAG, "Disconnected from GATT server.");
					
					Intent intentRSSI_DSCONNECTED = new Intent(RadarView.RSSI_DISCONNECTED);
					intentRSSI_DSCONNECTED.putExtra("RSSI_DISCONNECTED", STATE_DISCONNECTED);
					sendBroadcast(intentRSSI_DSCONNECTED);
              }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                Intent intentDISCOVERED_WITH_INFO = new Intent(ACTION_GATT_SERVICES_DISCOVERED);
                intentDISCOVERED_WITH_INFO.putExtra("ADDRESS", gatt.getDevice().getAddress() );
                sendBroadcast(intentDISCOVERED_WITH_INFO);
            }
            else
            {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {         
            	broadcastUpdate(gatt, ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
        {
        	broadcastUpdate(gatt, ACTION_DATA_AVAILABLE, characteristic);
            gatt.readRemoteRssi();
        }
        
        @Override
    	public void onReadRemoteRssi ( BluetoothGatt gatt, int rssi, int status)
    	{
        	// Some of them initialized earlier, since declared as <final>
            iIdxLocal = _iLocal++ % _nLocal;
            
            // Once upon a session we set it to TRUE; 
            if (iIdxLocal == _nLocal-1)  _IdxPassedLocal = true;
            
            // Actualize array cell
            _SkInputLocal[iIdxLocal] = iCurrentLocalRSSI = rssi;
            
            // Do the computation
            FastAvgLocal( iBench );
    	}        
        
    };
    
    private void broadcastUpdate(final String action)
    {
    final Intent intent = new Intent(action);
        
        sendBroadcast(intent);
    }
    
    private void broadcastUpdate( BluetoothGatt gatt, final String action, final BluetoothGattCharacteristic characteristic)
    {    	
    final Intent intent = new Intent(action);
    
    Intent intent2 = new Intent(HomeActivity.RECEIVE_MESSAGE);
    Intent intentRSSI_DATA = new Intent(RadarView.RSSI_DATA);
    Intent intentAUX = new Intent(RadarView.RSSI_AUX);
        
        if ( UUID_BATTERY_STATUS.equals(characteristic.getUuid())  ||  (UUID_SENSOR_DATA.equals(characteristic.getUuid())) || (UUID_RSSI_MEASUREMENT.equals(characteristic.getUuid()))   )
        {        	
            int format = -1;            
        	
        	if (UUID_SENSOR_DATA.equals(characteristic.getUuid()))
        	{
        		
        		format = BluetoothGattCharacteristic.FORMAT_UINT8;       			            
	                
	            final int iSensorData = characteristic.getIntValue(format, 0);// 04.02.2014

	            Log.d(TAG, String.format("Received sensor data: %d", iSensorData));
	            
		        if (1 <= iSensorData)
		        {		        	
		    		Log.w(TAG, " = = = = PRESSED = = = ="+  gatt.getDevice().getAddress() );
		    		intent.putExtra(EXTRA_DATA, "Pressed");// must be removed, here and below 
		    		intent2.putExtra("message", "Pressed ... ");
		    		sendBroadcast(intent2);
		    		
		    		soundPool1.play(soundID_ding, 0.5f, 0.5f, 1, 0, 1f);
		        }
		        else
		        {
		        	Log.w(TAG, " = = = = (    released      ) = = = = " + gatt.getDevice().getAddress() );
		        	intent.putExtra(EXTRA_DATA, "Released");
		        	intent2.putExtra("message", "Released\n");
		        	sendBroadcast(intent2);
		        }
        	}

	        if (UUID_RSSI_MEASUREMENT.equals(characteristic.getUuid())) 
	        {
	        	int flag = characteristic.getProperties();
	        	
	        	format = BluetoothGattCharacteristic.FORMAT_SINT8;	        	

	        	iCurrentRSSI = characteristic.getIntValue(format, 0);
	            
	            Log.d(TAG, String.format(" >>>  * RSSI * : %d", iCurrentRSSI));	            
	            
	            // Current iteration index
	            iIdx = _i++ % _n;
	            
	            // Once upon a session we set it to TRUE;
	            if (iIdx == _n-1)  _IdxPassed = true;
	            
	            // Actualize array cell
	            _SkInput[iIdx] = iCurrentRSSI;
	            
	            // Compute the fast Average
	            FastAvg( iBench );	            
	    		
	    		if ( ( ! _IdxPassed ) || (!  _IdxPassedLocal ) )
	    		{
	    			intentAUX.putExtra("aux", String.format(gatt.getDevice().getAddress() + " >>>> curr= %d,  local = %d,  cAVG= %d", iCurrentRSSI,  iCurrentLocalRSSI,   ArithMean(iCurrentRSSI, iCurrentLocalRSSI)   ));
		    		sendBroadcast(intentAUX);
		    		
		    		//+++ 03-12-2021 intentRSSI_DATA.putExtra("RSSI_DATA", ArithMean(iCurrentRSSI, iCurrentLocalRSSI)   );
                    intentRSSI_DATA.putExtra("RSSI_DATA",  iCurrentLocalRSSI ); //+++ 03-12--2021
		    		sendBroadcast(intentRSSI_DATA);// TODO: resolve duplication
	    		}
	    		else
	    		{
	    			intentAUX.putExtra("aux", String.format(gatt.getDevice().getAddress() + " curr= %d,  local = %d,  cAVG= %d  ***", (int)_SkOutput[iIdx],  (int)_SkOutputLocal[iIdxLocal],   ArithMean( (int)_SkOutput[iIdx], (int)_SkOutputLocal[iIdxLocal])   ));
		    		sendBroadcast(intentAUX);// new position		    		
		    		
		    		//+++ 03-12-2021 : intentRSSI_DATA.putExtra("RSSI_DATA", ArithMean( (int)_SkOutput[iIdx], (int)_SkOutputLocal[iIdxLocal])   );
                    intentRSSI_DATA.putExtra("RSSI_DATA", (int)_SkOutputLocal[iIdxLocal]  );//+++ 03-12-2021
		    		sendBroadcast(intentRSSI_DATA);// TODO: resolve duplication	    		
	    		
/*
 
The peer is NOT out of reach and previous tree values of RSSI are descending
  
RSSI.
   / \
    |
    |
    |
    |
    |
    |     (t-2)             (t-1)              (t)
    +----------------------------------------------------------------------------->
    |       *                 *                 *
    |       *                 *                 *
 Th.-  -  - *  -  -  -  -  -  *  -  -  -  -  -  *  -  -  -  -
    |                         *                 *
    |                                           *
    |    
         
*/
		            if (  (! bOutOfReach) && (iThreaholdHigh-2 >= iCurrentRSSI) && (iThreaholdHigh-1 >= iCurrentRSSI_old) && (iThreaholdHigh >= iCurrentRSSI_older) )
			        {
		            	bOutOfReach = true;
		            	
			    		Log.w(TAG, " = = = = OUT of REACH = = = =");

			    		//soundPool1.play(soundID_escaped, 1.0f, 1.0f, 1, 0, 1f);
			    		
						Intent intentPROX = new Intent(HomeActivity.PROX);			        	
						intentPROX.putExtra("PROX", "Alert: out of reach");
						sendBroadcast(intentPROX);// new position
			        }
			        else
			        {
/*
		        	 

The peer IS out of reach and previous tree values of RSSI are ascending
		        	  
RSSI.		          
   / \
    |
    |
    |
    |
    |
    |     (t-2)             (t-1)              (t)
    +----------------------------------------------------------------------------->
    |       *                 *                 *
    |       *                 *                 *
 Th.-  -  - *  -  -  -  -  -  *  -  -  -  -  -  *  -  -  -  -
    |       *                 *                 
    |       *                 
    |    
    |
		        	         
*/	
			        	if (  (bOutOfReach) && ( iThreaholdLow <= iCurrentRSSI   ) && (iThreaholdLow-1 >= iCurrentRSSI_old) && (iThreaholdLow-2 >= iCurrentRSSI_older) )
			        	{
			             	bOutOfReach = false;
			             	
			        		Log.w(TAG, " = = = = within control area = = = =");			        		
			        		
							Intent intentPROX = new Intent(HomeActivity.PROX);			        	
							intentPROX.putExtra("PROX", "Good, target located");
							sendBroadcast(intentPROX);// new position				    		
			        	}
		        	} // Idx Passed
		        }// back to area
	        	
	    		// Shift pre-last into pre-pre-last
	            iCurrentRSSI_older = iCurrentRSSI_old;
	            
	            // Shift current into pre-last
	            iCurrentRSSI_old = iCurrentRSSI;
	            		
	        } // UUID_RSSI_MEASUREMENT
	        
	        if ( UUID_BATTERY_STATUS.equals(characteristic.getUuid() ) ) 
	        {
	            format = BluetoothGattCharacteristic.FORMAT_UINT8;
		            
	            final int iCurrentBATTERY = characteristic.getIntValue(format, 0);            
	    		
	    		Intent intentBTRY_LVL = new Intent(HomeActivity.BTRY_LVL);
	    		intentBTRY_LVL.putExtra("percentage", iCurrentBATTERY);
	    		sendBroadcast(intentBTRY_LVL);
	            
	        } // UUID_BATTERY_STATUS 
	        
        }            
        else
        {        	
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            
            if (data != null && data.length > 0)
            {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }        
        
        sendBroadcast(intent);        
    }

    public class LocalBinder extends Binder
    {
        BluetoothLeService getService()
        {
        	// Possible: firstName = "mBluetoothLeService"
        	BluetoothLeService.class.getName();
        	
        	// Need ID, get it. Basically you should not 
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        close();

        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize()
    {
        // For API level 18 and above, get a reference to BluetoothAdapter through BluetoothManager.
        if (mBluetoothManager == null)
        {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            if (mBluetoothManager == null)
            {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                
                return false;
            }            
            
            // Make Sound pull, and put a <DING> sound into 
        	soundPool1 = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);        	
        	//+++soundID_ding = soundPool1.load(this, R.raw.oldbell, 1);
soundID_ding = soundPool1.load(this, R.raw.countdown, 1);
        	
        	// Boolean flag to tell if the device is within reach
        	bOutOfReach = false;

        	// Initialize pre-last and pre-prelast values of remote RSSI 
        	iCurrentRSSI_old = 0;        	
        	iCurrentRSSI_older = 0;
        	
        	// Assert thresholds. TODO; find better was of assertion, implement calibration
        	iThreaholdHigh = -59;        	
        	iThreaholdLow = -52;
        	        	
        	// Remember : 5 is iBench
        	_SkInput = new double[5];        	
        	_SkOutput = new double[5];        	
        	_SkInputLocal = new double[5];        	
        	_SkOutputLocal = new double[5];
        	
        	// Remember we're working on a scale [-95 .. -35], so the <-35> is kind of <ZERO> in this case, whilst ...        	
        	int mDb_ZERO = -35;        	
        	// whilst <-95> is kind of <INFINITY> on the same scale.
        	int mDb_INFINITY = -95;

        	// Remember : iTer->5 is iBench, and ...
        	for (int  iTer = 0; iTer< 5; iTer++)
        		
        		// Let's put <ZEROs> into input-output arrays
        		_SkInput[iTer] = _SkOutput[iTer] = _SkInputLocal[iTer] = _SkOutputLocal[iTer] = mDb_ZERO;
        	
        	// TODO: We need this workaround, since OnReadRemoteRssi is called _after we get+display first portion of RSSI data
        	// TODO; find better handling of iCurrentLocalRSSI    
        	iCurrentLocalRSSI  = mDb_ZERO; 
        	
        	// Initialize playing veriables for fast average computation <FastAvg(), FastAvgLocal()>
        	_n = _nLocal = 5; _i = _iLocal = 0;
        	
        	_IdxPassed = _IdxPassedLocal = false;
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null)
        {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");

            return false;
        }
        
        return true;
    }

    // Copy of BTLE-device instance to get it copied into array on connection
    public BluetoothDevice deviceAA; 
    
	// Copy of BTLE-protocol instance to get it copied into array on connection 
    public BluetoothGatt mBluetoothGattAA;//17-06
    
    public boolean connect(final String address)
    {
        if (mBluetoothAdapter == null || address == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");

            return false;
        }
        
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null )
        {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");

            if (mBluetoothGatt.connect())
            {
                mConnectionState = STATE_CONNECTING;

                return true;
            }
            else
            {
                return false;
            }
        }

        deviceAA = mBluetoothAdapter.getRemoteDevice(address);        

        if (deviceAA == null)
        {
            Log.w(TAG, "Device not found.  Unable to connect.");

            return false;
        }

        // We want to directly connect to the device, so we are setting the autoConnect parameter to false.
        mBluetoothGattAA = mBluetoothGatt = deviceAA.connectGatt(this, false, mGattCallback);
        
        Log.d(TAG, "Trying to create a new connection.");

        mBluetoothDeviceAddress = address;        
        mConnectionState = STATE_CONNECTING;        
               
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect()
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            
            return;
        }
        
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the App must call this method to ensure resources are
     * released properly.
     */
    public void close()
    {
        if (mBluetoothGatt == null)
        {
            return;
        }
        
        mBluetoothGatt.close();
        
        mBluetoothGatt = null;
    }
    

    private int ArithMean(int a, int b)
    {
    	return (a+b)/2;
    }

    // TODO: FastAvg and FastAvgLocal - id quite the same. Make one function of these tow functions.     
    public void FastAvg(int window)
	{
	int i,j,z=1,k1,k2,hw;
	
	double tmp;
       
		if( Math.IEEEremainder(window, 2) ==0 )
			window++;       
       
		hw = (window - 1) / 2;
       
		_SkOutput[0]=_SkInput[0];
       
		for (i = 1; i < _n; i++)
		{
		tmp = 0;
		
			if(i < hw)
			{
				k1 = 0;
				k2 = 2*i;
				z = k2+1;
			}
			else
				if((i + hw) > (_n - 1))
				{
					k1=i-_n+i+1;
					k2=_n-1;
					z=k2-k1+1;
				}
				else
				{
					k1 = i-hw;
					k2 = i+hw;
					z = window;
				}
		
			for (j = k1; j <= k2; j++)
			{
				tmp=tmp + _SkInput[j];
			}
		   
			_SkOutput[i] = tmp/z;
		}
	}
    
    // TODO: FastAvg and FastAvgLocal - id quite the same. Make one function of these tow functions.
    public void FastAvgLocal(int window)
	{
	int i,j,z=1,k1,k2,hw;
	
	double tmp;
       
		if( Math.IEEEremainder(window, 2) ==0 )
			window++;       
       
		hw = (window - 1) / 2;
       
		_SkOutputLocal[0]=_SkInputLocal[0];
       
		for (i = 1; i < _nLocal; i++)
		{
		tmp = 0;
		
			if(i < hw)
			{
				k1 = 0;
				k2 = 2*i;
				z = k2+1;
			}
			else
				if((i + hw) > (_nLocal - 1))
				{
					k1=i-_nLocal+i+1;
					k2=_nLocal-1;
					z=k2-k1+1;
				}
				else
				{
					k1 = i-hw;
					k2 = i+hw;
					z = window;
				}
		
			for (j = k1; j <= k2; j++)
			{
				tmp=tmp + _SkInputLocal[j];
			}
		   
			_SkOutputLocal[i] = tmp/z;
		}
	}   
    
    

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");

            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    
    
    public void unsetCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");

            return;
        }
        
        
        // This is specific to Data & RSSI.
        if ( (UUID_BATTERY_STATUS.equals(characteristic.getUuid())) || (UUID_SENSOR_DATA.equals(characteristic.getUuid()))   ||  (UUID_RSSI_MEASUREMENT.equals(characteristic.getUuid()))   )
        {        	
        	mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);// new position
            
        	BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));

        	descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);            

        	mBluetoothGatt.writeDescriptor(descriptor);            
        }
    }
    
    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");

            return;
        }
        
        
        // This is specific to Data & RSSI.
        if ( (UUID_BATTERY_STATUS.equals(characteristic.getUuid())) || (UUID_SENSOR_DATA.equals(characteristic.getUuid()))   ||  (UUID_RSSI_MEASUREMENT.equals(characteristic.getUuid()))   )
        {        	
        	mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
            
        	BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));

        	descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);            

        	mBluetoothGatt.writeDescriptor(descriptor);            
        }
        
		// In DD-2 the calibration is performed on-the-run, on peer's side
        boolean Autocalibration = true;

		if (! Autocalibration ) 
		{
			if ( UUID_WRITABLE.equals(characteristic.getUuid())  )
			{
				// Won't initialize it
				byte valueWrt[] = new byte[2];
				
				// Result of memory writing, no actual sending through the BTLE-air. 
				boolean Wrt_;
				
				try
				{
					Wrt_ = characteristic.setValue(valueWrt);
					
					mBluetoothGatt.writeCharacteristic(characteristic);
					
					Log.w(TAG, " = = = = characteristic.setValue(); - after " );
					
					if ( Wrt_ )
						Log.w(TAG, " = = = = characteristic.setValue(); - GOOD ");

					else
						Log.w(TAG, " = = = = characteristic.setValue(); - BAD ");
        		
				}
				catch (Exception e)
				{
					Log.w(TAG, " = = = = >> characteristic.setValue(); - BAD " + e.toString() );
				}
			}
		}        

    }    
    

    public boolean SendVal(BluetoothGattCharacteristic characteristic, byte valueWrtSh)
    {
		if ( UUID_WRITABLE.equals(characteristic.getUuid())  )
		{
			byte valueWrt[] = new byte[1];
			valueWrt[0] = valueWrtSh;
		
			// Result of local memory writing 
			boolean Wrt_;
			
			try
			{
				Wrt_ = characteristic.setValue(valueWrt);
				
				mBluetoothGatt.writeCharacteristic(characteristic);
				
				Log.w(TAG, " = = = = characteristic.setValue( <" + (int)valueWrtSh + " >) " );
				
				if ( Wrt_ )
				{
					Log.w(TAG, " = = = = characteristic.setValue( <" + (int)valueWrtSh + " >) . GOOD !  :-) " );				
					
				}
				else
				{
					Log.w(TAG, " = = = = characteristic.setValue( <" + (int)valueWrtSh + " >) . GOOD :-(" );
				}    
				return Wrt_;
			}
			catch (Exception e)
			{
				Log.w(TAG, " = = = = >> characteristic.setValue(); - BAD. EXCEPTION during write " + e.toString() );
				return false;
			}
		}
		else
		{
			Log.w(TAG, " = = = = >> BAD===IT's A WRON CHARACTERISTIC TO WRITE TO=== " );			
		}
			
		return false;
		
	}
    
    
    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices()
    {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();        
    }
    
}
