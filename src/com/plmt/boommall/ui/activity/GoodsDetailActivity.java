package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.config.Constants;
import com.plmt.boommall.entity.Ads;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.logic.CartLogic;
import com.plmt.boommall.network.logic.CollectionLogic;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.ui.adapter.GoodsDetailBannerAdapter;
import com.plmt.boommall.ui.view.BadgeView;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.viewflow.CircleFlowIndicator;
import com.plmt.boommall.ui.view.viewflow.ViewFlow;
import com.plmt.boommall.utils.ActivitiyInfoManager;
import com.plmt.boommall.utils.CartManager;
import com.plmt.boommall.utils.UserInfoManager;

public class GoodsDetailActivity extends Activity implements OnClickListener {

	public static final String GOODS_ID_KEY = "GoodsIdKey";

	public static final String ORIGIN_FROM_ADS_ACTION = "ADS";

	public static final String ORIGIN_FROM_MAIN_ACTION = "MAINS";

	public static final String ORIGIN_FROM_CATE_ACTION = "CATE";

	private Context mContext;

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mIndic;
	private ArrayList<Ads> mBannerActivityList = new ArrayList<Ads>();
	private GoodsDetailBannerAdapter mBannerAdapter;
	private FrameLayout mBannerFl;

	private TextView mGoodsNameTv;

	private TextView mGoodsPriceTv;

	private TextView mCommentRatioTv;
	private TextView mCommentPersonNumTv;
	private TextView mCommentTimeTv;
	private TextView mCommentContentTv;
	private TextView mCommentNameTv;

	public EditText mNum;

	private LinearLayout mCollectionLl;

	private LinearLayout mCartLl;

	private LinearLayout mBriefLl;

	public ImageButton mAddIb;

	public ImageButton mReduceIb;

	private Button mAddCartBtn;

	private ImageView mBackIv;

	private ImageView mShareIv;

	private ImageView mCollectionIv;

	private ImageView mCartIv;// 购物车

	private ViewGroup anim_mask_layout;// 动画层

	private ImageView mBall;// 小圆点

	private BadgeView mBuyNumView;// 购物车上的数量标签

	private int mCartNum;

	private Goods mGoods;

	private String mGoodsId;

	private String mNowAction = ORIGIN_FROM_MAIN_ACTION;

	private boolean isCollection = false;

	private CustomProgressDialog mProgressDialog;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GoodsLogic.GOODS_GET_SUC: {
				if (null != msg.obj) {
					mGoods = (Goods) msg.obj;
					fillUpGoodsData();
				}

				break;
			}
			case GoodsLogic.GOODS_GET_FAIL: {

				break;
			}
			case GoodsLogic.GOODS_GET_EXCEPTION: {

				break;
			}
			case GoodsLogic.NET_ERROR: {

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

	Handler mCartHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CartLogic.CART_UPDATE_SUC: {

				break;

			}
			case CartLogic.CART_UPDATE_FAIL: {
				break;
			}
			case CartLogic.CART_UPDATE_EXCEPTION: {
				break;
			}
			case CartLogic.CART_DEL_SUC: {
				if (null != msg.obj) {
				}

				break;

			}
			case CartLogic.CART_DEL_FAIL: {
				break;
			}
			case CartLogic.CART_DEL_EXCEPTION: {
				break;
			}
			case CartLogic.CART_ADD_SUC: {
				ActivitiyInfoManager
						.finishActivity("com.plmt.boommall.ui.activity.CategoryActivity");
				ActivitiyInfoManager
						.finishActivity("com.plmt.boommall.ui.activity.GoodsListActivity");

				finish();
				HomeActivity.setTab(HomeActivity.TAB_CART);
				break;
			}
			case CartLogic.CART_ADD_FAIL: {
				if (null != msg.obj) {
					Toast.makeText(mContext, R.string.cart_add_fail,
							Toast.LENGTH_SHORT).show();
				}

				break;
			}
			case CartLogic.CART_ADD_EXCEPTION: {
				break;
			}
			case CartLogic.NET_ERROR: {
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

	Handler mCollectionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {

			case CollectionLogic.COLLECTION_ADD_SUC: {
				isCollection = true;
				Toast.makeText(mContext, "添加收藏成功！", Toast.LENGTH_SHORT).show();
				break;
			}
			case CollectionLogic.COLLECTION_ADD_FAIL: {
				if (null != msg.obj) {
					Toast.makeText(mContext, "收藏失败：" + (String) msg.obj,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case CollectionLogic.COLLECTION_ADD_EXCEPTION: {
				break;
			}
			case CollectionLogic.COLLECTION_DEL_SUC: {
				isCollection = false;
				Toast.makeText(mContext, "删除收藏成功！", Toast.LENGTH_SHORT).show();
				break;
			}
			case CollectionLogic.COLLECTION_DEL_FAIL: {
				if (null != msg.obj) {
					Toast.makeText(mContext, "删除收藏失败：" + (String) msg.obj,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case CollectionLogic.COLLECTION_DEL_EXCEPTION: {
				break;
			}
			case CollectionLogic.NET_ERROR: {
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
		setContentView(R.layout.goods_detail);
		mContext = GoodsDetailActivity.this;
		if (!ActivitiyInfoManager.activitityMap
				.containsKey(ActivitiyInfoManager
						.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap
					.put(ActivitiyInfoManager.getCurrentActivityName(mContext),
							this);
		}
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		initCircleimage();
		initComment();

		mCollectionLl = (LinearLayout) findViewById(R.id.goods_detail_collection_ll);
		mCartLl = (LinearLayout) findViewById(R.id.goods_detail_cart_ll);
		mCollectionLl.setOnClickListener(this);
		mCartLl.setOnClickListener(this);

		mShareIv = (ImageView) findViewById(R.id.goods_detail_share_iv);
		mBackIv = (ImageView) findViewById(R.id.goods_detail_back_iv);
		mCartIv = (ImageView) findViewById(R.id.goods_detail_cart_iv);
		mShareIv.setOnClickListener(this);
		mCollectionIv = (ImageView) findViewById(R.id.goods_detail_collection_iv);
		mBackIv.setOnClickListener(this);

		mBuyNumView = new BadgeView(this, mCartIv);
		// mBuyNumView.setText("0");
		// mBuyNumView.show();

		mAddCartBtn = (Button) findViewById(R.id.goods_detail_add_cart_btn);
		mAddCartBtn.setOnClickListener(this);

		mGoodsNameTv = (TextView) findViewById(R.id.goods_detail_name_tv);
		mGoodsPriceTv = (TextView) findViewById(R.id.goods_detail_price_tv);
	}

	private void initComment() {
		mCommentRatioTv = (TextView) findViewById(R.id.goods_detail_comment_ratio_tv);
		mCommentPersonNumTv = (TextView) findViewById(R.id.goods_detail_comment_person_num_tv);
		mCommentTimeTv = (TextView) findViewById(R.id.goods_detail_comment_detial_time_tv);
		mCommentContentTv = (TextView) findViewById(R.id.goods_detail_comment_detial_content_tv);
		mCommentNameTv = (TextView) findViewById(R.id.goods_detail_comment_detial_name_tv);
	}

	private void initCircleimage() {
		mBannerFl = (FrameLayout) findViewById(R.id.goods_detail_framelayout);
		mBannerFl.setVisibility(View.VISIBLE);
		mViewFlow = (ViewFlow) findViewById(R.id.goods_detail_viewflow);
		mIndic = (CircleFlowIndicator) findViewById(R.id.goods_detail_viewflowindic);
		for (int i = 0; i < 3; i++) {
			Ads promotion = new Ads();
			promotion.setImgUrl("");
			mBannerActivityList.add(promotion);
		}

		showcircleimage();
	}

	private void showcircleimage() {
		mBannerAdapter = new GoodsDetailBannerAdapter(mContext,
				mBannerActivityList);
		mViewFlow.setAdapter(mBannerAdapter);
		mViewFlow.setViewGroup(mBannerFl);
		mViewFlow.setmSideBuffer(3); // 实际图片张数
		mViewFlow.setFlowIndicator(mIndic);
		mViewFlow.setTimeSpan(2000);
		mViewFlow.setSelection(3 * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		mViewFlow.requestFocus();
	}

	private void initData() {
		mGoodsId = (String) getIntent().getSerializableExtra(
				GoodsDetailActivity.GOODS_ID_KEY);
		mNowAction = getIntent().getAction();

		// if (null != mGoods) {
		// fillUpGoodsData();
		// }
		if (!TextUtils.isEmpty(mGoodsId)) {
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			GoodsLogic.getGoodsById(mContext, mHandler, mGoodsId);
		}

	}

	private void fillUpGoodsData() {

		mGoodsNameTv.setText(!TextUtils.isEmpty(mGoods.getName()) ? mGoods
				.getName().trim() : "");
		mGoodsPriceTv.setText(!TextUtils.isEmpty(mGoods.getFinalPrice()) ? "¥"
				+ mGoods.getFinalPrice().trim() : "¥");

		//fillUpComment();
	}

	private void fillUpComment() {
		mCommentRatioTv
				.setText(!TextUtils.isEmpty(mGoods.getComment().getZl_value()) ? mGoods
						.getComment().getZl_value().trim()
						: "");
		mCommentPersonNumTv.setText(!TextUtils.isEmpty(mGoods.getComment()
				.getKd_value()) ? "¥"
				+ mGoods.getComment().getKd_value().trim() : "¥");
		mCommentTimeTv.setText(!TextUtils.isEmpty(mGoods.getComment()
				.getCustomer_id()) ? mGoods.getComment().getCustomer_id()
				.trim() : "");
		mCommentContentTv.setText(!TextUtils.isEmpty(mGoods.getComment()
				.getDetail()) ? "¥" + mGoods.getComment().getDetail().trim()
				: "¥");
		mCommentNameTv
				.setText(!TextUtils.isEmpty(mGoods.getComment().getNickname()) ? mGoods
						.getComment().getNickname().trim()
						: "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_detail_cart_ll: {
			if (!UserInfoManager.getLoginIn(mContext)) {
				Intent intent = new Intent(GoodsDetailActivity.this,
						LoginActivity.class);
				intent.setAction(LoginActivity.ORIGIN_FROM_GOODS_DETAIL_KEY);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else if (!TextUtils.isEmpty(mNowAction)
					&& ORIGIN_FROM_CATE_ACTION.equals(mNowAction)) {
				mProgressDialog = new CustomProgressDialog(mContext);
				mProgressDialog.show();
				CartLogic.add(mContext, mCartHandler, mGoods.getId(),
						String.valueOf(mCartNum));
			}
			break;
		}
		case R.id.goods_detail_add_cart_btn: {

			mAddCartBtn.setClickable(false);
			if (TextUtils.isEmpty(mGoods.getNum())
					|| "null".equals(mGoods.getNum())) {
				mGoods.setNum("0");
			}
			mCartNum++;
			mAddCartBtn.setClickable(true);
			mBuyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			mBuyNumView.setText(String.valueOf(mCartNum));
			mBuyNumView.show();

			break;
		}
		case R.id.goods_detail_collection_ll: {

			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			if (!isCollection) {
				CollectionLogic.add(mContext, mCollectionHandler, mGoodsId);
			} else {
				CollectionLogic.del(mContext, mCollectionHandler, mGoodsId);
			}

			break;
		}
		case R.id.goods_detail_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}
		case R.id.goods_detail_share_iv: {
			String title = "旺铺商城分享";
			if (null != mGoods) {
				title = mGoods.getName();
			}
			startActivity(Intent.createChooser(
					new Intent(Intent.ACTION_SEND)
							.putExtra(Intent.EXTRA_SUBJECT, title)
							.putExtra(Intent.EXTRA_TEXT, title)
							.setType(Constants.MIMETYPE_TEXT_PLAIN),
					getString(R.string.menu_share)));
			break;
		}

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
}
