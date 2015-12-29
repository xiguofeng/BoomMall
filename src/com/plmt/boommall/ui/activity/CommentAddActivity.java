package com.plmt.boommall.ui.activity;

import com.plmt.boommall.R;
import com.plmt.boommall.network.logic.CommentLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class CommentAddActivity extends Activity implements OnClickListener, OnRatingBarChangeListener {

	private Context mContext;

	private RatingBar mPriceRatingBar;
	private RatingBar mExpressRatingBar;
	private RatingBar mQualityRtingBar;

	private ImageView mBackIv;
	private TextView mSubmitTv;

	private EditText mDetailEt;

	protected CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CommentLogic.COMMENT_ADD_SUC: {
				Toast.makeText(mContext, "评价成功！", Toast.LENGTH_SHORT).show();
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

		mDetailEt = (EditText) findViewById(R.id.comment_detail_et);

		mBackIv = (ImageView) findViewById(R.id.comment_back_iv);
		mBackIv.setOnClickListener(this);
		mSubmitTv = (TextView) findViewById(R.id.comment_submit_tv);
		mSubmitTv.setOnClickListener(this);
	}

	private void initData() {
	}

	private void addComment() {
		if (mPriceRatingBar.getNumStars() == 0
				|| mExpressRatingBar.getNumStars() == 0 && mQualityRtingBar.getNumStars() == 0) {
			Toast.makeText(mContext, "请打分！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(mDetailEt.getText().toString().trim())) {
			Toast.makeText(mContext, "亲，您的评价对我们很重要哦！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		mProgressDialog.show();
		CommentLogic.addComment(mContext, mHandler, "goodsId", String.valueOf(mPriceRatingBar.getNumStars()),
				String.valueOf(5+mExpressRatingBar.getNumStars()), String.valueOf(10+mQualityRtingBar.getNumStars()),
				mDetailEt.getText().toString().trim(), "nickname");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_back_iv: {
			CommentAddActivity.this.finish();
			break;
		}
		case R.id.comment_submit_tv: {
			addComment();
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
