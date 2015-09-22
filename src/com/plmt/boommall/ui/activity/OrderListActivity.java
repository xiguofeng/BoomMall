package com.plmt.boommall.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.plmt.boommall.R;
import com.plmt.boommall.ui.fragment.OrdersAllFragment;
import com.plmt.boommall.ui.fragment.OrdersUnPayFragment;
import com.plmt.boommall.ui.view.pageindicator.TabPageIndicator;

public class OrderListActivity extends FragmentActivity implements
		OnClickListener {

	private static final String[] TRANSACTIONNEWSTITLE = new String[] { "1",
			"2", "3" };

	private ImageView mBackIv;

	private ViewPager mPager;// 页卡内容

	private TabPageIndicator mIndicator;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		findViewById();
		initView();
	}

	private void findViewById() {
		mPager = (ViewPager) findViewById(R.id.order_list_vPager);
		mBackIv = (ImageView) findViewById(R.id.order_list_back_iv);
		mIndicator = (TabPageIndicator) findViewById(R.id.order_list_indicator);
	}

	private void initView() {
		mBackIv.setOnClickListener(this);
		InitViewPager();
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		FragmentPagerAdapter adapter = new TabAdapter(
				getSupportFragmentManager());
		mPager.setAdapter(adapter);
		mIndicator.setViewPager(mPager);
	}

	class TabAdapter extends FragmentPagerAdapter {
		public TabAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			Fragment nowFragment = new OrdersAllFragment();
			switch (position) {
			case 0: {
				nowFragment = new OrdersAllFragment();
				break;
			}
			case 1: {
				nowFragment = new OrdersUnPayFragment();
				break;
			}
			case 2: {
				nowFragment = new OrdersUnPayFragment();
				break;
			}
			default:
				break;
			}

			return nowFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title = getResources().getString(R.string.order_all);
			switch (position % TRANSACTIONNEWSTITLE.length) {
			case 0: {
				title = getResources().getString(R.string.order_all);
				break;
			}
			case 1: {
				title = getResources().getString(R.string.order_unpay);
				break;
			}
			case 2: {
				title = getResources().getString(R.string.order_uncomment);
				break;
			}
			}
			return title;
		}

		@Override
		public int getCount() {
			return TRANSACTIONNEWSTITLE.length;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_list_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
