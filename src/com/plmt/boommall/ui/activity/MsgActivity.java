package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Msg;
import com.plmt.boommall.ui.adapter.MsgAdapter;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.utils.CacheManager;

public class MsgActivity extends Activity implements OnClickListener {

	private final Context mContext = MsgActivity.this;

	private ListView mMsgLv;
	private MsgAdapter mMsgAdapter;
	private ArrayList<Msg> mMsgList = new ArrayList<Msg>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg);
		initView();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void initView() {
		mMsgLv = (ListView) findViewById(R.id.msg_lv);
		mMsgAdapter = new MsgAdapter(mContext, mMsgList);
		mMsgLv.setAdapter(mMsgAdapter);
	}

	private void initData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
