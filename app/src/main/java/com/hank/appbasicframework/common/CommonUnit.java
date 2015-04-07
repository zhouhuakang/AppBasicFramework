package com.hank.appbasicframework.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CommonUnit {

	public static void toast(Context context, int id) {
		Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
	}

	public static void toast(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Check for the equality for the two array
	 * 
	 * @param bt1
	 * @param bt2
	 * @return
	 */
	public static boolean checkByteArrayEqual(byte[] bt1, byte[] bt2) {
		int length = bt1.length;
		boolean b = true;
		for (int i = 0; i < length; i++) {
			if (bt1[i] != bt2[i]) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * Copy the whole file folder
	 * 
	 * @param oldPath
	 *            type:String
     *            note:original file path
	 * @param newPath
	 *            type:String
     *            note:target file path
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {
		try {
            (new File(newPath)).mkdirs(); // If the folder does not exit,then create new one
			File oldFile = new File(oldPath);
			String[] oldFileList = oldFile.list();
			File tempFile = null;
			for (int i = 0; i < oldFileList.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					tempFile = new File(oldPath + oldFileList[i]);
				} else {
					tempFile = new File(oldPath + File.separator + oldFileList[i]);
				}
				if (tempFile.isFile()) {
					FileInputStream input = new FileInputStream(tempFile);
					FileOutputStream output = new FileOutputStream(newPath + File.separator + (tempFile.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					// 5 Bytes per read
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (tempFile.isDirectory()) {// if it has the child folder,the copy it recursively
					copyFolder(oldPath + File.separator + oldFileList[i], newPath + File.separator + oldFileList[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteFolder(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						deleteFolder(files[i]);
					}
				}

			}
			file.delete();
		} else {

		}
	}

	public static boolean isWifiConnect(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

	/**
	 * byte[] to String
	 *
	 */
	public static String parseData(byte[] receiverDate) {
		StringBuffer re = new StringBuffer();
		for (int i = 0; i < receiverDate.length; i++) {
			re.append(to16(receiverDate[i]));
		}

		return re.toString();
	}

    /**
     *
     * binary to hexadecimal
     */
	public static String to16(int b) {
		String s = Integer.toHexString(b);
		int length = s.length();
		if (length == 1) {
			s = "0" + s;
		}
		if (length > 2) {
			s = s.substring(length - 2, length);
		}
		return s.toString();
	}
}
