package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.User;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.network.logic.UserLogic;
import com.plmt.boommall.ui.view.MultiStateView;

public class MainActivity extends Activity implements OnClickListener {

	private Context mContext;

	private long exitTime = 0;

	private MultiStateView mMultiStateView;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = MainActivity.this;

		initView();
		// initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mContext = MainActivity.this;

		mMultiStateView = (MultiStateView) findViewById(R.id.main_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR)
				.findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView
								.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(),
								"Fetching Data", Toast.LENGTH_SHORT).show();
					}
				});
		mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
	}

	private void initData() {
		GoodsLogic.getGoodsListByCategory(mContext, mHandler, "1", 1, 1);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), R.string.exit,
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
