
package com.keeper.activity;

import java.util.HashMap;

public class SampleGattAttributes {
	
    private static HashMap<String, String> attributes = new HashMap();
        
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";    
    
    public static String S_SUPP_SRVC   = "00001800-0000-1000-8000-00805f9b34fb";
     public static String S_SUPP_C1    = "00002a00-0000-1000-8000-00805f9b34fb";
     public static String S_SUPP_C2    = "00002a01-0000-1000-8000-00805f9b34fb";
    
    public static String D_I_SRVC      = "0000180a-0000-1000-8000-00805f9b34fb";	 
     public static String MAN_NAME     =  "00002a29-0000-1000-8000-00805f9b34fb";
     public static String S_24         =  "00002a24-0000-1000-8000-00805f9b34fb";
    
    public static String BATT_SRVC     = "0000180f-0000-1000-8000-00805f9b34fb";     
     public static String BATT_ST      =  "00002a19-0000-1000-8000-00805f9b34fb";// 2.0

    public static String T_SENSOR_FN   = "0000180f-0000-1000-8000-00805f9b34fb";
     public static String SENSOR_DATA      = "00002a19-0000-1000-8000-00805f9b34fb"; // 3.0
     public static String RSSI_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"; // 3.1
     public static String U_WRITABLE       = "00002a19-0000-1000-8000-00805f9b34fb"; // 3.2

/*     public static String T_SENSOR_FN   = "0000180d-0000-1000-8000-00805f9b34fb";
     public static String SENSOR_DATA      = "00002a38-0000-1000-8000-00805f9b34fb"; // 3.0
     public static String RSSI_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"; // 3.1
     public static String U_WRITABLE       = "00002a39-0000-1000-8000-00805f9b34fb"; // 3.2
   */

    static
    {
	   attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Touch Sensor  Service");

       attributes.put(S_SUPP_SRVC, "System Support Service");        
        attributes.put(S_SUPP_C1, "Characteristic 1");
        attributes.put(S_SUPP_C2, "Characteristic 2");
        
       attributes.put(D_I_SRVC, "Device Information Service");        
        attributes.put(MAN_NAME, "Manufacturer Name String");
        attributes.put(S_24, "Service - 24");
        
       attributes.put(BATT_SRVC, "Battery Service");
        attributes.put(BATT_ST, "Battery Status");
        
        attributes.put(T_SENSOR_FN, "Touch Sensor Functions");
         attributes.put(SENSOR_DATA, "Get Touch Sensor Status");         
         attributes.put(RSSI_MEASUREMENT, "Get RSSI values");
         attributes.put(U_WRITABLE, "Send Byte");
        
    }

    public static String lookup(String uuid, String defaultName)
    {
        String name = attributes.get(uuid);
        
        return name == null ? defaultName : name;
    }
}
