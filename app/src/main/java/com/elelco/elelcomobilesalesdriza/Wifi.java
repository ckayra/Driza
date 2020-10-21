package com.elelco.elelcomobilesalesdriza;
import android.content.*;
import android.net.*;
import android.net.wifi.*;
import android.text.*;
import java.util.*;

public final class Wifi
{
	
	
	public static Boolean wifiEnabled(Context context){
		WifiManager wifi=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if (!wifi.isWifiEnabled()) wifi.setWifiEnabled(true);
		return wifi.isWifiEnabled();
	}
	
	
	public static String getCurrentWifi(Context context)
	{
		String ssid=null;
		ConnectivityManager connManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo=connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (netinfo.isConnected())
		{
			final WifiManager wifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connInfo=wifiManager.getConnectionInfo();
			if (connInfo != null && !TextUtils.isEmpty(connInfo.getSSID()))
				ssid = connInfo.getSSID();
		}
		return ssid;
	}

	private static WifiConfiguration getConfiguredNetwork(Context context,String networkSSID){
		final WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		
		List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration w : list)
		{
			if (w.SSID != null && w.SSID.indexOf(networkSSID) > -1)
			{
				return w;
			}           
		}
		return null;
   }

	public static WifiConfiguration getWifiConfig(Context context,String networkSSID,String password){
		final WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> scanList;
		scanList = wifiManager.getScanResults();
		String id="";
		for (int i=0;i < scanList.size();i++)
		{
			if (scanList.get(i).SSID!=null && scanList.get(i).SSID.toString().indexOf(networkSSID) > -1)
			{
				WifiConfiguration w;
				w=getConfiguredNetwork(context,networkSSID);
				
				//add connection
				if (w==null){
				w=new WifiConfiguration();
				w.SSID=scanList.get(i).SSID;
				w.preSharedKey="\""+ password +"\"";
				wifiManager.addNetwork(w);
				//search again
					w=getConfiguredNetwork(context,networkSSID);
				return w;
				} else return w;
				
			}
		}
		return null;
	}

	
}
