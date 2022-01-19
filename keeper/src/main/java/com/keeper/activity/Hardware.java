

package com.keeper.activity;

import java.util.ArrayList;
import java.util.UUID;

import com.keeper.model.Device;
import com.keeper.activity.SampleGattAttributes;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Build;
import android.util.Log;


@TargetApi(Build.VERSION_CODES.KITKAT)
public class Hardware // extends nix, implements IO , works with BT-driver via GATT
{
private final static String TAG = Hardware.class.getSimpleName();
	
public final static UUID UUID_WRITABLE  = UUID.fromString(SampleGattAttributes.U_WRITABLE);
public final static UUID UUID_SENSOR_DATA = UUID.fromString(SampleGattAttributes.SENSOR_DATA);
public final static UUID UUID_RSSI_MEASUREMENT = UUID.fromString(SampleGattAttributes.RSSI_MEASUREMENT);
public final static UUID UUID_BATTERY_STATUS = UUID.fromString(SampleGattAttributes.BATT_ST);

private BluetoothAdapter mBluetoothAdapter;

// is equal to this constant as long as firmware keeps it the same
public final int iBattery = 0;
public final int iTouch = 0;
public final int iRssi = 1;
private final int iWrittable = 2;

	public void SetAdapter( BluetoothAdapter mVoidBluetoothAdapter )
	{
		mBluetoothAdapter = mVoidBluetoothAdapter;
	}
	
	public BluetoothAdapter GetAdapter()
	{
		return mBluetoothAdapter;
	}

	
	public void Subscribe(ArrayList<Device>  devices, int currentPosition, int A, int B)
	{
		BluetoothGattCharacteristic  characteristic ;
		
		characteristic = devices.get(currentPosition).mGattCharacteristicsX.get(A).get(B);
		
		_setCharacteristicNotification(devices.get(currentPosition).mBluetoothGattX, characteristic, true );		
	}
	
	public void Unsubscribe(ArrayList<Device>  devices, int currentPosition, int A, int B)
	{
		BluetoothGattCharacteristic  characteristic ;
		
		characteristic = devices.get(currentPosition).mGattCharacteristicsX.get(A).get(B);
		
		_unsetCharacteristicNotification(devices.get(currentPosition).mBluetoothGattX, characteristic, false );		
	}
	
    public boolean _SendVal(BluetoothGatt _BluetoothGatt, BluetoothGattCharacteristic characteristic, byte valueWrtSh)
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
				
				_BluetoothGatt.writeCharacteristic(characteristic);
				
				Log.w(TAG, " * * * * * characteristic.setValue( <" + (int)valueWrtSh + " >) " );
				
				if ( Wrt_ )
				{
					Log.w(TAG, " * * * * * characteristic.setValue( <" + (int)valueWrtSh + " >) . GOOD !  :-) " );					
				}
				else
				{					
					Log.w(TAG, " * * * * * characteristic.setValue( <" + (int)valueWrtSh + " >) . GOOD :-(" );
				}    
				return Wrt_;
			}
			catch (Exception e)
			{
				Log.w(TAG, " * * * * * >> characteristic.setValue(); - BAD. EXCEPTION during write " + e.toString() );
				return false;
			}
		}
		else
		{
			Log.w(TAG, " * * * * * >> BAD===IT's A WRONG CHARACTERISTIC TO WRITE TO=== " );			
		}
			
		return false;		
	}
    
    
    
    public void _readCharacteristic(BluetoothGatt _BluetoothGatt, BluetoothGattCharacteristic characteristic)
    {
        if (mBluetoothAdapter == null || _BluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");

            return;
        }
        _BluetoothGatt.readCharacteristic(characteristic);
    }

    
    
    public void _unsetCharacteristicNotification(BluetoothGatt _BluetoothGatt, BluetoothGattCharacteristic characteristic, boolean enabled)
    {
        if (mBluetoothAdapter == null || _BluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");

            return;
        }
        
        if ( (UUID_BATTERY_STATUS.equals(characteristic.getUuid())) || (UUID_SENSOR_DATA.equals(characteristic.getUuid()))   ||  (UUID_RSSI_MEASUREMENT.equals(characteristic.getUuid()))   )
        {        	
        	_BluetoothGatt.setCharacteristicNotification(characteristic, enabled);// new position
            
        	BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));

        	descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);            

        	_BluetoothGatt.writeDescriptor(descriptor);            
        }
    }
    
    public void _setCharacteristicNotification(BluetoothGatt _BluetoothGatt, BluetoothGattCharacteristic characteristic, boolean enabled)
    {
        if (mBluetoothAdapter == null || _BluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");

            return;
        }
        
        if ( (UUID_BATTERY_STATUS.equals(characteristic.getUuid())) || (UUID_SENSOR_DATA.equals(characteristic.getUuid()))   ||  (UUID_RSSI_MEASUREMENT.equals(characteristic.getUuid()))   )
        {        	
        	_BluetoothGatt.setCharacteristicNotification(characteristic, enabled);// new position
            
        	BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));

        	descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);            

        	_BluetoothGatt.writeDescriptor(descriptor);            
        }

    }    

	public void SendByteToWritable(ArrayList<Device>  devices, int currentPosition, byte bByte)
	{		
		if (devices.get(currentPosition).mConnectedX) 
			// TODO: resolve if we need this duplication. Done ALREADY in <onButtonXXclk> 
		{	
			BluetoothGattCharacteristic  characteristic ;
			
			characteristic = devices.get(currentPosition).mGattCharacteristicsX.get(devices.get(currentPosition).iDataGrpX).get(iWrittable);//2
	
			_SendVal(devices.get(currentPosition).mBluetoothGattX, characteristic, bByte);
		}		
	}

}
