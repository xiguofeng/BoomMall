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
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class CommentAddActivity extends Activity implements OnClickListener, OnRatingBarChangeListener {

	private Context mContext;

	private RatingBar mPriceRatingBar;
	private RatingBar mExpressRatingBar;
	private RatingBar mQualityRtingBar;

	private ImageView mBackIv;

	protected CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CommentLogic.COMMENT_ADD_SUC: {
				/*
				 * Intent intent = new Intent(CommentAddActivity.this,
				 * CommentsResultActivity.class); startActivity(intent);
				 * CommentAddActivity.this.finish();
				 * overridePendingTransition(R.anim.push_left_in,
				 * R.anim.push_left_out);
				 */

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
		initViews();
		initData();
	}

	private void initViews() {

		mPriceRatingBar = (RatingBar) findViewById(R.id.comment_price_ratingbar);
		mExpressRatingBar = (RatingBar) findViewById(R.id.comment_express_ratingbar);
		mQualityRtingBar = (RatingBar) findViewById(R.id.comment_quality_ratingbar);

		mBackIv = (ImageView) findViewById(R.id.comment_back_iv);
		mBackIv.setOnClickListener(this);
	}

	private void initData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_back_iv: {
			CommentAddActivity.this.finish();
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		final int numStars = ratingBar.getNumStars();
		ratingBar.getId();
		switch (ratingBar.getId()) {
		case R.id.comment_price_ratingbar: {
			break;
		}
		case R.id.comment_express_ratingbar: {
			break;
		}
		case R.id.comment_quality_ratingbar: {
			break;
		}

		default:
			break;
		}
		
	}

}
