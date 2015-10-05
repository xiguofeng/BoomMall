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

public class HomeOldActivity extends TabActivity implements
		android.view.View.OnClickListener {

	public static final String TAG = HomeOldActivity.class.getSimpleName();

	public static final String TAB_MAIN = "MAIN";
	public static final String TAB_CATEGORY = "CATEGORY";
	public static final String TAB_CART = "CART";
	public static final String TAB_PERSON = "PERSON";

	private RadioGroup mTabButtonGroup;

	private static TabHost mTabHost;
	private static RadioButton mMainRb;
	private static RadioButton mCategoryRb;
	private static RadioButton mShopCartRb;
	private static RadioButton mPersonRb;

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
		mCategoryRb = (RadioButton) findViewById(R.id.home_tab_category_rb);
		mShopCartRb = (RadioButton) findViewById(R.id.home_tab_cart_rb);
		mPersonRb = (RadioButton) findViewById(R.id.home_tab_person_rb);
	}

	private void initView() {
		mTabHost = getTabHost();

		Intent i_home = new Intent(this, MainActivity.class);
		Intent i_logi_confirm = new Intent(this, CategoryActivity.class);
		Intent i_history = new Intent(this, ShopCartActivity.class);
		Intent i_psw = new Intent(this, UserActivity.class);

		mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN)
				.setContent(i_home));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_CART).setIndicator(TAB_CART)
				.setContent(i_logi_confirm));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_CATEGORY)
				.setIndicator(TAB_CATEGORY).setContent(i_history));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_PERSON)
				.setIndicator(TAB_PERSON).setContent(i_psw));

		mTabHost.setCurrentTabByTag(TAB_MAIN);

		mTabButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.home_tab_main_rb:
							mTabHost.setCurrentTabByTag(TAB_MAIN);
							break;

						case R.id.home_tab_category_rb:
							mTabHost.setCurrentTabByTag(TAB_CART);

							break;

						case R.id.home_tab_cart_rb:
							mTabHost.setCurrentTabByTag(TAB_CATEGORY);

							break;

						case R.id.home_tab_person_rb:
							mTabHost.setCurrentTabByTag(TAB_PERSON);

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
		} else if (TAB_CART.equals(tab)) {
			mCategoryRb.setChecked(true);
		} else if (TAB_CATEGORY.equals(tab)) {
			mShopCartRb.setChecked(true);
		} else if (TAB_PERSON.equals(tab)) {
			mPersonRb.setChecked(true);
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
