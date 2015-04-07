package com.hank.appbasicframework.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hank.appbasicframework.R;


public class TitleActivity extends BaseActivity {

	private FrameLayout mBody;
	private ImageButton mBackButton;
	private Button mUploadButton;
	private RelativeLayout mTitleLyaout;

	private TextView mTitle;

	private static Animation mRollUpAnimation;
	private static Animation mRollDownAnimation;

	private TextView mTvWifiTimerSetTimeConfirm;
	private TextView mTvWifiTimerSetCancle;
	private TextView mTvWifiTimerDaySetConfirm;
	private TextView mTvWifiTimerSave;

	protected RelativeLayout mRlWirelessBirdge;

	private Button mBtnRightTextButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_title);
		findView();

		setListener();

		loadAnim();
	}

	@Override
	public void setContentView(int layoutResID) {
		getLayoutInflater().inflate(layoutResID, mBody, true);
	}

	private void findView() {
		mTitleLyaout = (RelativeLayout) findViewById(R.id.title_layout);
		mBody = (FrameLayout) findViewById(R.id.body);

		// mBackButton = (ImageButton) findViewById(R.id.btn_back_to_parent);
		// mUploadButton = (Button) findViewById(R.id.btn_upload);
		//
		mTitle = (TextView) findViewById(R.id.tv_title);
		//
		// mTvWifiTimerSetTimeConfirm = (TextView)
		// findViewById(R.id.tv_wifi_timer_set_time_confirm);
		// mTvWifiTimerSetCancle = (TextView)
		// findViewById(R.id.tv_wifi_timer_set_time_cancle);
		// mTvWifiTimerDaySetConfirm = (TextView)
		// findViewById(R.id.tv_wifi_timer_set_day_confirm);
		// mTvWifiTimerSave = (TextView)
		// findViewById(R.id.tv_wifi_timer_set_save);
		//
		// mRlWirelessBirdge = (RelativeLayout)
		// findViewById(R.id.rl_wireless_bridge_menu);
		//
		// mBtnRightTextButton = (Button)
		// findViewById(R.id.btn_right_text_button);

	}

	public void setTitle(int resId) {
		mTitle.setText(resId);
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}

	public void setBackVisible() {
		mBackButton.setVisibility(View.VISIBLE);
	}

	// public void setUploadButtonListener(OnSingleClickListener
	// onSingleClickListener) {
	// mUploadButton.setVisibility(View.VISIBLE);
	// mUploadButton.setOnClickListener(onSingleClickListener);
	// }
	//
	// public void setWifiTimerSetTimeConfirmListener(OnSingleClickListener
	// onSingleClickListener) {
	// mTvWifiTimerSetTimeConfirm.setVisibility(View.VISIBLE);
	// mTvWifiTimerSetTimeConfirm.setOnClickListener(onSingleClickListener);
	// }
	//
	// public void setWifiTimerSetDayConfirmListener(OnSingleClickListener
	// onSingleClickListener) {
	// mTvWifiTimerDaySetConfirm.setVisibility(View.VISIBLE);
	// mTvWifiTimerDaySetConfirm.setOnClickListener(onSingleClickListener);
	// }
	//
	// public void setWifiTimerSetCancleListener(OnSingleClickListener
	// onSingleClickListener) {
	// mTvWifiTimerSetCancle.setVisibility(View.VISIBLE);
	// mTvWifiTimerSetCancle.setOnClickListener(onSingleClickListener);
	// }
	//
	// public void setWifiTimeSaveListener(OnSingleClickListener
	// onSingleClickListener) {
	// mTvWifiTimerSave.setVisibility(View.VISIBLE);
	// mTvWifiTimerSave.setOnClickListener(onSingleClickListener);
	// }
	//
	// public void setWirelessBridgeMenuListener(OnSingleClickListener
	// onSingleClickListener) {
	// mRlWirelessBirdge.setVisibility(View.VISIBLE);
	// mRlWirelessBirdge.setOnClickListener(onSingleClickListener);
	// }
	//
	// public void setRightTextButtonClickListener(int resid,
	// OnSingleClickListener onSingleClickListener) {
	// mBtnRightTextButton.setVisibility(View.VISIBLE);
	// mBtnRightTextButton.setText(resid);
	// mBtnRightTextButton.setOnClickListener(onSingleClickListener);
	// }
	//
	private void setListener() {
		// mBackButton.setOnClickListener(new OnSingleClickListener() {
		//
		// @Override
		// public void doOnClick(View v) {
		// back();
		// }
		// });
		// }
		//
		// public void back() {
		// finish();
		// overridePendingTransition(R.anim.roll, R.anim.roll_right_out);
	}

	@Override
	public void onBackPressed() {
		back();
	}

	// private void timerFinish() {
	// back();
	// Timer timer = new Timer();
	// timer.schedule(new TimerTask() {
	//
	// @Override
	// public void run() {
	// finish();
	// overridePendingTransition(R.anim.roll, R.anim.roll);
	// }
	// }, 300);
	// }

	private void loadAnim() {
		// mRollUpAnimation = AnimationUtils.loadAnimation(TitleActivity.this,
		// R.anim.title_roll_up);
		// mRollDownAnimation = AnimationUtils.loadAnimation(TitleActivity.this,
		// R.anim.roll_up);
		// mRollDownAnimation.setAnimationListener(new AnimationListener() {
		//
		// @Override
		// public void onAnimationStart(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// mTitleLyaout.setVisibility(View.GONE);
		// }
		// });

		// mRollUpAnimation.setAnimationListener(new AnimationListener() {
		//
		// @Override
		// public void onAnimationStart(Animation animation) {
		// mTitleLyaout.setVisibility(View.VISIBLE);
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// }
		// });
	}

	public void startTitleRollUpAnimation() {
		mTitleLyaout.startAnimation(mRollUpAnimation);
	}

	public void startTitleRollDownAnimation() {
		mTitleLyaout.startAnimation(mRollDownAnimation);
	}
}
