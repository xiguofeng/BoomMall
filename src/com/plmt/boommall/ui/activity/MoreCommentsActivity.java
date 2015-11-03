package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Comment;
import com.plmt.boommall.ui.adapter.CommentsAdapter;

public class MoreCommentsActivity extends Activity implements OnClickListener {

	public static final int SUC = 0;

	public static final int FAIL = SUC + 1;

	private final Context mContext = MoreCommentsActivity.this;

	private ImageView mBackIv;

	private ListView mCommentsLv;
	private CommentsAdapter mMsgAdapter;
	private ArrayList<Comment> mMsgList = new ArrayList<Comment>();

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case SUC: {
				break;

			}
			case FAIL: {
				break;
			}

			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_comments);
		initView();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void initView() {
		mBackIv = (ImageView) findViewById(R.id.more_comments_back_iv);
		mBackIv.setOnClickListener(this);

		mCommentsLv = (ListView) findViewById(R.id.more_comments_lv);
		mMsgAdapter = new CommentsAdapter(mContext, mMsgList);
		mCommentsLv.setAdapter(mMsgAdapter);

		mCommentsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}

	private void initData() {
		mMsgList.clear();
		getData();
	}

	private void getData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_comments_back_iv: {
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
