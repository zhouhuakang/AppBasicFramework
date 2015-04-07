package com.hank.appbasicframework.application;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.hank.appbasicframework.R;
import com.hank.appbasicframework.activity.HomePageActivity;
import com.hank.appbasicframework.common.Constants;
import com.hank.appbasicframework.common.Settings;
import com.hank.appbasicframework.db.data.Person;
import com.hank.appbasicframework.udp.ApkAccessor;
import com.hank.appbasicframework.udp.DownloadParameter;
import com.hank.appbasicframework.udp.DownLoadAccesser;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Stack;


public class BasicApplication extends Application {

    public static Person mPerson;
    /**
     * Application file location,in ROM or SDCard
     */
    public static String fileLocation;
    private static Stack<Activity> activityStack;
    private static BasicApplication singleton;
    public UpdateTask mUpdateTask;

    /**
     * Returns the application instance
     */
    public static BasicApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        start();
    }

    /**
     * push activity to Acitivity Stack
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * finish the current activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * finish the specified Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * finish the specified Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivity() {
        for (int size = activityStack.size(); size > 0; size--) {
            if (null != activityStack.get(size - 1)) {
                activityStack.get(size - 1).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * Exit the application
     */
    public void AppExit() {
        // You can release other sources first when you try to exit the app
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
        System.gc();
        System.exit(0);
    }

    public void start() {
        Settings.P_HEIGHT = getResources().getDisplayMetrics().heightPixels;
        Settings.P_WIDTH = getResources().getDisplayMetrics().widthPixels;
        Settings.STATUS_HEIGHT = getStatusBarHeight();
    }


    private int getStatusBarHeight() {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            Object obj = cls.newInstance();
            Field field = cls.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
        }
        return 0;
    }

    public void updateApk(String url) {
        if (mUpdateTask == null) {
            mUpdateTask = new UpdateTask();
            mUpdateTask.execute(url);
        }
    }

    /**
     * Upgrade
     */
    public class UpdateTask extends AsyncTask<String, Integer, File> {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        private Notification mNotification;
        private ApkAccessor mApkAccessor;

        public Notification getNotification() {
            return mNotification;
        }

        @Override
        protected void onPreExecute() {
            mNotification = new Notification(R.drawable.ic_launcher, getString(R.string.update_title, 0), System.currentTimeMillis());
            mNotification.flags = Notification.FLAG_NO_CLEAR;
            mNotification.contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            mNotification.contentView.setTextViewText(R.id.notify_text, getString(R.string.update_content, 0));
            mNotification.contentView.setProgressBar(R.id.notify_pb, 100, 0, false);

            Intent notificationIntent = new Intent(BasicApplication.this, HomePageActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mNotification.contentIntent = PendingIntent.getActivity(BasicApplication.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mNotificationManager.notify(Constants.NOTIFICATION_ID_UPDATE, mNotification);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mNotification.contentView.setTextViewText(R.id.notify_text, getString(R.string.update_content, values[0]));
            mNotification.contentView.setProgressBar(R.id.notify_pb, 100, values[0], false);
            mNotificationManager.notify(Constants.NOTIFICATION_ID_UPDATE, mNotification);
        }

        @Override
        protected void onPostExecute(File result) {
            mNotificationManager.cancel(Constants.NOTIFICATION_ID_UPDATE);
            mUpdateTask = null;
            if (result != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(result);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }

        @Override
        protected File doInBackground(String... params) {

            DownloadParameter downloadParam = new DownloadParameter();
            downloadParam.setTempFilePath(Settings.mTempFilePathFinal + File.separator + "update.apk.tmp");
            downloadParam.setSaveFilePath(Settings.mTempFilePathFinal + File.separator + "update.apk");

            mApkAccessor = new ApkAccessor(BasicApplication.this);
            mApkAccessor.setOnProgressListener(new DownLoadAccesser.OnProgressListener() {

                @Override
                public void onProgress(long progress, long total) {
                    publishProgress((int) (progress * 100 / total));
                }
            }, 2000);
            Boolean result = mApkAccessor.execute(params[0], downloadParam);

            publishProgress(100);

            return (result == null || result == false) ? null : new File(Settings.mTempFilePathFinal, "update.apk");
        }

        public void stop() {
            if (mApkAccessor != null) {
                mApkAccessor.stop();
            }
            mNotificationManager.cancel(Constants.NOTIFICATION_ID_UPDATE);
        }
    }

}