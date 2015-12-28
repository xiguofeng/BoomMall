package com.plmt.boommall.ui.activity;

import com.plmt.boommall.R;
import com.plmt.boommall.network.logic.CommentLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.utils.OrderManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CommentAddActivity extends Activity implements OnClickListener {

	private Context mContext;

	private LinearLayout mVeryGoodLl;

	private LinearLayout mGoodLl;

	private LinearLayout mNotGoodLl;

	private ImageView mBackIv;

	protected CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CommentLogic.COMMENT_ADD_SUC: {
				/*Intent intent = new Intent(CommentAddActivity.this,
						CommentsResultActivity.class);
				startActivity(intent);
				CommentAddActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);*/

				break;
			}
			case CommentLogic.COMMENT_ADD_FAIL: {
				// Toast.makeText(mContext, R.string.login_fail,
				// Toast.LENGTH_SHORT).show();
				break;
			}
			case CommentLogic.COMMENT_ADD_EXCEPTION: {
				break;
			}
			case CommentLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
			if (null != mProgressDialog) {
				mProgressDialog.show();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);
		mContext = CommentAddActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mVeryGoodLl = (LinearLayout) findViewById(R.id.comments_very_good_ll);
		mGoodLl = (LinearLayout) findViewById(R.id.comments_good_ll);
		mNotGoodLl = (LinearLayout) findViewById(R.id.comments_not_good_ll);

		mBackIv = (ImageView) findViewById(R.id.comments_back_iv);
	}

	private void setUpListener() {
		mVeryGoodLl.setOnClickListener(this);
		mGoodLl.setOnClickListener(this);
		mNotGoodLl.setOnClickListener(this);

		mBackIv.setOnClickListener(this);
	}

	private void setUpData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comments_back_iv: {
			CommentAddActivity.this.finish();
			break;
		}
		case R.id.comments_very_good_ll: {
			if (!TextUtils.isEmpty(OrderManager.getsCurrentCommentOrderId())) {
				if (null != mProgressDialog) {
					mProgressDialog.show();
				}
				CommentLogic.addComment(CommentAddActivity.this, mHandler,
						OrderManager.getsCurrentCommentOrderId(),
						getString(R.string.comment_very_good),"id");
			} else {

			}
			break;
		}
		case R.id.comments_good_ll: {
			if (!TextUtils.isEmpty(OrderManager.getsCurrentCommentOrderId())) {
				CommentLogic.addComment(CommentAddActivity.this, mHandler,
						OrderManager.getsCurrentCommentOrderId(),
						getString(R.string.comment_good),"id");
			} else
				break;
		}
		case R.id.comments_not_good_ll: {
			if (!TextUtils.isEmpty(OrderManager.getsCurrentCommentOrderId())) {
				CommentLogic.addComment(CommentAddActivity.this, mHandler,
						OrderManager.getsCurrentCommentOrderId(),
						getString(R.string.comment_not_good),"id");
			} else {

			}
			break;
		}

		default:
			break;
		}
	}

}
