package com.hank.appbasicframework.activity;

import android.content.Context;
import android.os.Bundle;

import com.hank.appbasicframework.R;


public class HomePageActivity extends TitleActivity {
    private Context mContext = HomePageActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        findView();
        initView();
        setListener();


    }

    private void findView() {

    }

    private void initView() {

    }

    private void setListener() {

    }

}
