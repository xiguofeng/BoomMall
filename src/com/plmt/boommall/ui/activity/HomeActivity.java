package com.plmt.boommall.ui.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.plmt.boommall.R;

public class HomeActivity extends TabActivity implements
		android.view.View.OnClickListener {

	public static final String TAG = HomeActivity.class.getSimpleName();

	public static final String TAB_MAIN = "MAIN";
	public static final String TAB_HISTORY = "HISTORY";
	public static final String TAB_LOGI_CONFIRM = "LOGI_CONFIRM";
	public static final String TAB_PSW = "PSW";

	private RadioGroup mTabButtonGroup;

	private static TabHost mTabHost;
	private static RadioButton mMainRb;
	private static RadioButton mLogiConfirmRb;
	private static RadioButton mHistoryRb;
	private static RadioButton mPwdRb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		findViewById();
		initView();
		initData();
	}

	private void findViewById() {
		mTabButtonGroup = (RadioGroup) findViewById(R.id.home_radio_button_group);
		mMainRb = (RadioButton) findViewById(R.id.home_tab_main_rb);
		mLogiConfirmRb = (RadioButton) findViewById(R.id.home_tab_logistics_rb);
		mHistoryRb = (RadioButton) findViewById(R.id.home_tab_rob_rb);
		mPwdRb = (RadioButton) findViewById(R.id.home_tab_accout_rb);
	}

	private void initView() {
		mTabHost = getTabHost();

		Intent i_home = new Intent(this, MainActivity.class);
		Intent i_logi_confirm = new Intent(this, ShopCartActivity.class);
		Intent i_history = new Intent(this, CategoryAndGoodsActivity.class);
		Intent i_psw = new Intent(this, UserActivity.class);

		mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN)
				.setContent(i_home));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_LOGI_CONFIRM)
				.setIndicator(TAB_LOGI_CONFIRM).setContent(i_logi_confirm));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_HISTORY)
				.setIndicator(TAB_HISTORY).setContent(i_history));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_PSW).setIndicator(TAB_PSW)
				.setContent(i_psw));

		mTabHost.setCurrentTabByTag(TAB_MAIN);

		mTabButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.home_tab_main_rb:
							mTabHost.setCurrentTabByTag(TAB_MAIN);
							break;

						case R.id.home_tab_logistics_rb:
							mTabHost.setCurrentTabByTag(TAB_LOGI_CONFIRM);

							break;

						case R.id.home_tab_rob_rb:
							mTabHost.setCurrentTabByTag(TAB_HISTORY);

							break;

						case R.id.home_tab_accout_rb:
							mTabHost.setCurrentTabByTag(TAB_PSW);

							break;
						default:
							break;
						}
					}
				});

	}

	private void initData() {
		// mTabHost.setCurrentTabByTag(TAB_MAIN);
	}

	public static void setTab(String tab) {
		mTabHost.setCurrentTabByTag(tab);
		if (TAB_MAIN.equals(tab)) {
			mMainRb.setChecked(true);
		} else if (TAB_LOGI_CONFIRM.equals(tab)) {
			mLogiConfirmRb.setChecked(true);
		} else if (TAB_HISTORY.equals(tab)) {
			mHistoryRb.setChecked(true);
		} else if (TAB_PSW.equals(tab)) {
			mPwdRb.setChecked(true);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {

	}

	public static void showMainByOnkey() {
		// mTabHost.setCurrentTabByTag(TAB_MAIN);
		mMainRb.setChecked(true);
	}

}
