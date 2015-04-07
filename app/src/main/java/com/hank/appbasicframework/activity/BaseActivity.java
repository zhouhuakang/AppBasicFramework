package com.hank.appbasicframework.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.hank.appbasicframework.application.BasicApplication;
import com.hank.appbasicframework.db.dao.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class BaseActivity extends FragmentActivity {

    protected BasicApplication mApplication = BasicApplication.getInstance();
    private volatile DatabaseHelper mHelper;

    public DatabaseHelper getHelper() {
        if (mHelper == null) {
            mHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return mHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Add the activity to the Activity Stack
        BasicApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
    }

    protected void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        // Remove the activity from the Activity Stack
        BasicApplication.getInstance().finishActivity(this);
        super.onDestroy();
        if (mHelper != null) {
            OpenHelperManager.releaseHelper();
            mHelper = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_SEARCH:
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}