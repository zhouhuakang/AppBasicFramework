package com.hank.appbasicframework.udp;

import android.content.Context;
import android.widget.Toast;


import com.hank.appbasicframework.R;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

/**
 * Apk下载类
 */
public class ApkAccessor extends DownLoadAccesser {

    private boolean mToastError = true;

    public ApkAccessor(Context context) {
        super(context);
    }


    @Override
    protected void onException(Exception e) {
        super.onException(e);

        if (!mToastError)
            return;

        final int msgId;
        if (e instanceof SocketException
                || e instanceof InterruptedIOException || e instanceof UnknownHostException
                || e instanceof UnknownServiceException) {
            msgId = R.string.error_network;
        } else {
            msgId = R.string.error_system;
        }

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(mContext, msgId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isToastError() {
        return mToastError;
    }

    public void setToastError(boolean toastError) {
        this.mToastError = toastError;
    }
}