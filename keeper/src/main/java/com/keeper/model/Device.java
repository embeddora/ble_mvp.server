

package com.keeper.model;

import java.io.Serializable;
import java.util.ArrayList;

/*import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;*/
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.res.Resources;

import com.keeper.activity.BluetoothLeService;
import com.keeper.activity.HomeActivity;
//import com.keeper.activity.LogView;
import com.keeper.activity.RadarView;

public class Device implements Serializable  
{
	private static final long serialVersionUID = -7060210544600464481L;
	
	public int id;
	public String name;
	public String state;
	public String address;
	
	// Place them into some meta-class (of <device>)
	public static final String STATE_ON 			= "POWERED ON, ACTIVE";
	public static final String STATE_PAUSE 		= "POWERED ON, INACTIVE";	
	public static final String STATE_DISCONNECTED = "DISCONNECTED";	
	public static final String STATE_ACTIVTED 	= "ACTIVTED";
	public static final String STATE_DEACTIVTED 	= "DEACTIVTED";	
	public static final String STATE_APPEARED 	= "APPEARED";
	public static final String STATE_DISAPPEARED 	= "DISAPPEARED";	
	public static final String STATE_SEARCHED 	= "SEARCHED";	
	private final double LatDFLT = 50.43643323;
	private final double LonDFLT = 30.49944917;

	
	
	// added on 20-06-2014 
	public String mDeviceNameX;
	public String mDeviceAddressX;	
	public BluetoothLeService mBluetoothLeServiceX;
	public ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristicsX = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();	
	public   boolean mConnectedX = false;
	public   boolean mPoweredX = false;
	public   boolean mAdoptedX = false;	
	public BluetoothGatt mBluetoothGattX;
	public BluetoothDevice deviceX;	
	public   int iBtryGrpX;
	public   int iDataGrpX;	
	public Intent gattServiceIntentX;
	public HomeActivity.mDisconnectedX mDisconnectedX;	
	public HomeActivity.mConnectedDiscoveredX mConnectedDiscoveredX;
		

	public RadarView mRadarX;
	
	public ArrayList<History> historiesX;	
	
	
	
	private double Lat, Lon;
	private double LatRestore, LonRestore;
	
	private final String  filename = "ymk-coords";	
//	Resources res;
//	OverlayManager mOverlayManager2;
//	Overlay overlay2;
//	OverlayItem yandex2;
//	BalloonItem balloonYandex2;

	
	
	
	

	public Device(int id, String name, String state, String address)
	{
		this.id = id;
		this.name = name;
		this.state = state;
		
		// The only thing alleged to be unique across all the devices of the world, and we _do_ require to construct the <Device> instance with it.
		this.address = address;
    
	}
	
	
	public void setState(String NewState)
	{		
		this.state = NewState;
	}


}
