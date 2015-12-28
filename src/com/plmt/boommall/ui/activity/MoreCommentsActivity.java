package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Comment;
import com.plmt.boommall.network.logic.CommentLogic;
import com.plmt.boommall.ui.adapter.CommentsAdapter;
import com.plmt.boommall.ui.view.CustomProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MoreCommentsActivity extends Activity implements OnClickListener {

	private final Context mContext = MoreCommentsActivity.this;

	private ImageView mBackIv;

	private ListView mCommentsLv;
	private CommentsAdapter mCommentAdapter;
	private ArrayList<Comment> mCommentList = new ArrayList<Comment>();

	private String mId;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CommentLogic.COMMENT_LIST_GET_SUC: {
				if (null != msg.obj) {
					mCommentList.clear();
					mCommentList
							.addAll((Collection<? extends Comment>) msg.obj);
					mCommentAdapter.notifyDataSetChanged();
				}
				break;
			}
			case CommentLogic.COMMENT_LIST_GET_FAIL: {
				break;
			}
			case CommentLogic.COMMENT_LIST_GET_EXCEPTION: {
				break;
			}

			default:
				break;
			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_comments);
		mProgressDialog = new CustomProgressDialog(mContext);
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
		mCommentAdapter = new CommentsAdapter(mContext, mCommentList);
		mCommentsLv.setAdapter(mCommentAdapter);

		mCommentsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}

	private void initData() {
		mId = getIntent().getStringExtra("id");

		mCommentList.clear();
		getData();
	}

	private void getData() {
		mProgressDialog.show();
		if (!TextUtils.isEmpty(mId)) {
			mId = "22266";
			CommentLogic.getList(mContext, mHandler, mId);
		}
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
