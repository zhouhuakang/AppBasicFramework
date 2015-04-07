package com.hank.appbasicframework.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.util.List;

public class NetworkUtil {
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<ScanResult> mScanResultList;
	private List<WifiConfiguration> mWifiConfigurationList;

	public NetworkUtil(Context context) {
		// Get the instance of the WifiManager
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	public void startScan() {
		mWifiManager.startScan();
		// Get the scan result
		mScanResultList = mWifiManager.getScanResults();
		// Get the WifiInfo
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	public List<ScanResult> getWiFiList() {
		return mScanResultList;
	}

	public String getSSID() {
		return mWifiInfo.getSSID();
	}

	public String getRouterIP() {
		Log.e("RouterIP",
				Formatter.formatIpAddress(mWifiManager.getDhcpInfo().gateway));
		return Formatter.formatIpAddress(mWifiManager.getDhcpInfo().gateway);
	}

	public static void getLocalMAC(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"localMac", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		String mac = "";
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		mac = wifiInfo.getMacAddress();
		editor.putString("mac", mac);
		editor.commit();
	}

	public static boolean isNetConneted(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isWifiConnect(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

}
