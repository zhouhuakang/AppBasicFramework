package com.hank.appbasicframework.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hank.appbasicframework.R;
import com.hank.appbasicframework.application.BasicApplication;
import com.hank.appbasicframework.common.CommonUnit;
import com.hank.appbasicframework.common.Constants;
import com.hank.appbasicframework.common.CrashHandler;
import com.hank.appbasicframework.common.NetworkUtil;
import com.hank.appbasicframework.common.Settings;
import com.hank.appbasicframework.json.data.UpdateInfo;

import org.apache.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class LoadingActivity extends BaseActivity {

    private BasicApplication mApplication;
    private Context mContext = LoadingActivity.this;
    private Timer timer;

    private String mBaseFileName = "BaseFileName";
    private String mTempFileName = "Temp";

    private CheckUpdateTask checkUpdateTask;
    private boolean stay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        timer = new Timer();
        createDirectory();
        mApplication = BasicApplication.getInstance();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.setLogSavePath(Settings.mTempFilePathFinal);
        crashHandler.init(this);

        //Check the network
        if (NetworkUtil.isNetConneted(mContext)) {
            NetworkUtil.getLocalMAC(mContext);
        } else {
            Toast.makeText(mContext, R.string.error_no_net, Toast.LENGTH_LONG).show();
        }

        // Check for update
        checkUpdateTask = new CheckUpdateTask();
//        checkUpdateTask.execute();
        if (!stay) {
            timerSwitchToHome(3 * 1000);
        }

    }

    private void toHomePageActivity() {
        Intent intent = new Intent();
        intent.setClass(mContext, HomePageActivity.class);
        startActivity(intent);

        finish();
    }

    private void timerSwitchToHome(int second) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (null != checkUpdateTask) {
                    checkUpdateTask.cancel(true);
                }
                toHomePageActivity();
            }
        }, second);
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Create the directory for the app
     */
    private void createDirectory() {
        String mExternalStoragePath = Environment.getExternalStorageDirectory().getPath();
        String mInternalStoragePath = this.getFilesDir().getPath();
        // Base File
        Settings.mBaseFilePathExternal = mExternalStoragePath + File.separator + mBaseFileName;
        Settings.mBaseFilePathInternal = mInternalStoragePath + File.separator + mBaseFileName;

        Settings.mBaseFilePathFinal = Settings.mBaseFilePathExternal;
        // Temp File
        Settings.mTempFilePathExternal = Settings.mBaseFilePathExternal + File.separator + mTempFileName;
        Settings.mTempFilePathInternal = Settings.mBaseFilePathInternal + File.separator + mTempFileName;

        Settings.mTempFilePathFinal = Settings.mTempFilePathExternal;

        /**
         * 1.SD card exist:
         *      If SD card exist,then check for the application files;
         * if application files exist in SD card,then go  to the 1.1
         *      1.1 Files exist in the SD card:
         *
         *      1.2 Files do not exit in the SD card:
         *         if application files exist in ROM,go to 1.2.1;else go to 1.2.2
         *         1.2.1 Files exist in the rom,then ask the user whether to copy the files to the SD card
         *          yes: copy the application files to the SD card
         *          n o: just set the final path for BaseFiles,TempFiles and so on
         *         1.2.2 Files do not exist in the rom,then create the files in the SD card
         *
         * 2.SD card not exist:
         *       2.1 Files exist in Rom card:
         *          do nothing
         *       2.2 Files do not exit in Rom card:
         *          create the application files in rom
         *
         *
         *
         */
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // If SD card exists,then check for the files;
            // 1.1 Files exist in SD card
            if (new File(Settings.mBaseFilePathExternal).exists() &&
                    new File(Settings.mTempFilePathExternal).exists()) {
                Log.e("1.1", "Files exist in SD card");

                Settings.mBaseFilePathFinal = Settings.mBaseFilePathExternal;
                Settings.mTempFilePathFinal = Settings.mTempFilePathExternal;

                BasicApplication.fileLocation = Constants.FILE_LOCATION_SD;

            } else {   // 1.2 Files do not exit in the SD card
                if (new File(Settings.mBaseFilePathInternal).exists()) {
                    Log.e("1.2.1", "Files exist in ROM");
                    stay = true;
                    new AlertDialog.Builder(LoadingActivity.this)
                            .setTitle(R.string.alert_title_choose_file_path)
                            .setMessage(R.string.message_choose_file_path)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e("1.2.1.yes", "yes:copy files to the SD card from ROM");

                                    CommonUnit.copyFolder(Settings.mBaseFilePathInternal, Settings.mBaseFilePathExternal);
                                    CommonUnit.deleteFolder(new File(Settings.mBaseFilePathInternal));

                                    Settings.mBaseFilePathFinal = Settings.mBaseFilePathExternal;
                                    Settings.mTempFilePathFinal = Settings.mTempFilePathExternal;


                                    BasicApplication.fileLocation = Constants.FILE_LOCATION_SD;

                                }
                            }).setNegativeButton("No", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.mBaseFilePathFinal = Settings.mBaseFilePathInternal;
                            Settings.mTempFilePathFinal = Settings.mTempFilePathInternal;

                            BasicApplication.fileLocation = Constants.FILE_LOCATION_ROM;
                        }
                    }).show();

                } else {// 1.2.2 Files do not exist in the rom,then create the files in the SD card
                    Log.e("1.2.2", "Files do not exist in the ROM");
                    if (!new File(Settings.mBaseFilePathExternal).mkdirs() ||
                            !new File(Settings.mTempFilePathExternal).mkdirs()) {

                    } else {
                        Settings.mBaseFilePathFinal = Settings.mBaseFilePathExternal;
                        Settings.mTempFilePathFinal = Settings.mTempFilePathExternal;

                        BasicApplication.fileLocation = Constants.FILE_LOCATION_SD;
                    }
                }
            }

        } else {
            Log.e("2", "No SD card");
            if (new File(Settings.mBaseFilePathInternal).exists() &&
                    new File(Settings.mTempFilePathInternal).exists()) {
                BasicApplication.fileLocation = Constants.FILE_LOCATION_ROM;
            } else {
                Settings.mBaseFilePathFinal = Settings.mBaseFilePathInternal;
                Settings.mTempFilePathFinal = Settings.mTempFilePathInternal;
            }

        }
        timerSwitchToHome(3 * 1000);
    }


    /**
     * Check for update
     */
    private class CheckUpdateTask extends AsyncTask<Void, Void, UpdateInfo> {

        @Override
        protected UpdateInfo doInBackground(Void... params) {
            InputStream inputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;

            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                URL url = new URL(Constants.VERSION_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(4000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpStatus.SC_OK) {
                    inputStream = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    String result = new String(byteArrayOutputStream.toByteArray());
                    Gson mJson = new Gson();
                    return mJson.fromJson(result, UpdateInfo.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final UpdateInfo result) {
            if (!isCancelled()) {
                if (result != null) {
                    if (Constants.VERSION != result.getVersion()) {
                        new AlertDialog.Builder(LoadingActivity.this)
                                .setTitle(R.string.alert_app_new_version_found)
                                .setPositiveButton(R.string.btn_yes, new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mApplication.updateApk(result.getUrl());
                                        finish();
                                    }
                                }).setNegativeButton(R.string.btn_no, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timerSwitchToHome(0 * 1000);
                            }
                        }).setCancelable(false).show();

                    } else {
                        timerSwitchToHome(3 * 1000);
                    }
                }
            }
        }
    }

}
