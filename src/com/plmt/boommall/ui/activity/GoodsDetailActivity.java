package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.config.Constants;
import com.plmt.boommall.entity.Ads;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.config.RequestUrl.notice;
import com.plmt.boommall.network.logic.CartLogic;
import com.plmt.boommall.network.logic.CollectionLogic;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.network.logic.NoticeLogic;
import com.plmt.boommall.ui.adapter.GoodsDetailBannerAdapter;
import com.plmt.boommall.ui.view.BadgeView;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.srollview.slidingmenu.SlidingMenu;
import com.plmt.boommall.ui.view.viewflow.CircleFlowIndicator;
import com.plmt.boommall.ui.view.viewflow.ViewFlow;
import com.plmt.boommall.utils.ActivitiyInfoManager;
import com.plmt.boommall.utils.UserInfoManager;

public class GoodsDetailActivity extends Activity implements OnClickListener {

	public static final String GOODS_ID_KEY = "GoodsIdKey";

	public static final String ORIGIN_FROM_ADS_ACTION = "ADS";

	public static final String ORIGIN_FROM_MAIN_ACTION = "MAINS";

	public static final String ORIGIN_FROM_CATE_ACTION = "CATE";

	public static final String ORIGIN_FROM_SEAR_ACTION = "SEAR";

	private Context mContext;

	private SlidingMenu mSlidingMenu;

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mIndic;
	public static ArrayList<Ads> mBannerActivityList = new ArrayList<Ads>();
	private GoodsDetailBannerAdapter mBannerAdapter;
	private FrameLayout mBannerFl;

	private TextView mGoodsNameTv;

	private TextView mGoodsPriceTv;

	private TextView mCommentRatioTv;
	private TextView mCommentPersonNumTv;
	private TextView mCommentTimeTv;
	private TextView mCommentContentTv;
	private TextView mCommentNameTv;
	private ImageButton mCommentScoreFirstIb;
	private ImageButton mCommentScoreSecondIb;
	private ImageButton mCommentScoreThirdIb;
	private ImageButton mCommentScoreFourthIb;
	private ImageButton mCommentScoreFifthIb;

	private TextView mIsSaleAbleTv;
	private TextView mDeliverytimeTv;

	public EditText mNum;

	private LinearLayout mCollectionLl;
	private TextView mCollectionTv;

	private LinearLayout mCartLl;

	private LinearLayout mBriefLl;

	private RelativeLayout mMoreCommentsRl;

	public ImageButton mAddIb;

	public ImageButton mReduceIb;

	private Button mAddCartBtn;

	private Button mPriceReduceBtn;
	private Button mAOGBtn;

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
				ActivitiyInfoManager.finishActivity("com.plmt.boommall.ui.activity.CategoryActivity");
				ActivitiyInfoManager.finishActivity("com.plmt.boommall.ui.activity.GoodsListActivity");
				ActivitiyInfoManager.finishActivity("com.plmt.boommall.ui.activity.Html5Activity");

				finish();
				ShoppingCartActivity.isNeedUpdate = true;
				HomeActivity.setTab(HomeActivity.TAB_CART);
				break;
			}
			case CartLogic.CART_ADD_FAIL: {
				if (null != msg.obj) {
					String failMsg = getString(R.string.cart_add_fail) + ":" + (String) msg.obj;
					Toast.makeText(mContext, failMsg, Toast.LENGTH_SHORT).show();
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
				mGoods.setInWishList("1");
				fillUpCollection();
				Toast.makeText(mContext, "添加收藏成功！", Toast.LENGTH_SHORT).show();
				break;
			}
			case CollectionLogic.COLLECTION_ADD_FAIL: {
				if (null != msg.obj) {
					Toast.makeText(mContext, "收藏失败：" + (String) msg.obj, Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case CollectionLogic.COLLECTION_ADD_EXCEPTION: {
				break;
			}
			case CollectionLogic.COLLECTION_DEL_SUC: {
				isCollection = false;
				mGoods.setInWishList("0");
				fillUpCollection();
				Toast.makeText(mContext, "删除收藏成功！", Toast.LENGTH_SHORT).show();
				break;
			}
			case CollectionLogic.COLLECTION_DEL_FAIL: {
				if (null != msg.obj) {
					Toast.makeText(mContext, "删除收藏失败：" + (String) msg.obj, Toast.LENGTH_SHORT).show();
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

	private Handler mNoticeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NoticeLogic.PRICE_REDUCE_SET_SUC: {

				break;
			}
			case NoticeLogic.PRICE_REDUCE_SET_FAIL: {

				break;
			}
			case NoticeLogic.PRICE_REDUCE_SET_EXCEPTION: {

				break;
			}
			case NoticeLogic.AOG_SET_SUC: {
				 Toast.makeText(mContext, "添加通知成功！", Toast.LENGTH_SHORT).show();
				break;
			}
			case NoticeLogic.AOG_SET_FAIL: {
				if(null!=msg.obj){
					Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, "添加通知失败！", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case NoticeLogic.AOG_SET_EXCEPTION: {

				break;
			}
			case NoticeLogic.NET_ERROR: {

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
		if (!ActivitiyInfoManager.activitityMap.containsKey(ActivitiyInfoManager.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap.put(ActivitiyInfoManager.getCurrentActivityName(mContext), this);
		}
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.goods_detail_expanded_menu);
		mSlidingMenu.setmUrl("");

		initCircleimage();
		initComment();

		mCollectionLl = (LinearLayout) findViewById(R.id.goods_detail_collection_ll);
		mCollectionLl.setOnClickListener(this);
		mCollectionTv = (TextView) findViewById(R.id.goods_detail_collection_tv);
		mCollectionIv = (ImageView) findViewById(R.id.goods_detail_collection_iv);

		mCartLl = (LinearLayout) findViewById(R.id.goods_detail_cart_ll);
		mCartLl.setOnClickListener(this);
		mMoreCommentsRl = (RelativeLayout) findViewById(R.id.goods_detail_more_comment_rl);
		mMoreCommentsRl.setOnClickListener(this);

		mShareIv = (ImageView) findViewById(R.id.goods_detail_share_iv);
		mBackIv = (ImageView) findViewById(R.id.goods_detail_back_iv);
		mCartIv = (ImageView) findViewById(R.id.goods_detail_cart_iv);
		mShareIv.setOnClickListener(this);
		mBackIv.setOnClickListener(this);

		mBuyNumView = new BadgeView(this, mCartIv);
		// mBuyNumView.setText("0");
		// mBuyNumView.show();

		mAddCartBtn = (Button) findViewById(R.id.goods_detail_add_cart_btn);
		mAddCartBtn.setOnClickListener(this);

		mPriceReduceBtn = (Button) findViewById(R.id.goods_detail_reduced_price_btn);
		mPriceReduceBtn.setOnClickListener(this);
		mAOGBtn = (Button) findViewById(R.id.goods_detail_aog_btn);
		mAOGBtn.setOnClickListener(this);

		mGoodsNameTv = (TextView) findViewById(R.id.goods_detail_name_tv);
		mGoodsPriceTv = (TextView) findViewById(R.id.goods_detail_price_tv);

		mIsSaleAbleTv = (TextView) findViewById(R.id.goods_detail_goods_is_sale_tv);
		mDeliverytimeTv = (TextView) findViewById(R.id.goods_detail_goods_delivery_tv);
	}

	private void initComment() {
		mCommentRatioTv = (TextView) findViewById(R.id.goods_detail_comment_ratio_tv);
		mCommentPersonNumTv = (TextView) findViewById(R.id.goods_detail_comment_person_num_tv);
		mCommentTimeTv = (TextView) findViewById(R.id.goods_detail_comment_detial_time_tv);
		mCommentContentTv = (TextView) findViewById(R.id.goods_detail_comment_detial_content_tv);
		mCommentNameTv = (TextView) findViewById(R.id.goods_detail_comment_detial_name_tv);

		mCommentScoreFirstIb = (ImageButton) findViewById(R.id.goods_detail_score_first_btn);
		mCommentScoreSecondIb = (ImageButton) findViewById(R.id.goods_detail_score_second_btn);
		mCommentScoreThirdIb = (ImageButton) findViewById(R.id.goods_detail_score_third_btn);
		mCommentScoreFourthIb = (ImageButton) findViewById(R.id.goods_detail_score_fourth_btn);
		mCommentScoreFifthIb = (ImageButton) findViewById(R.id.goods_detail_score_fifth_btn);
	}

	private void initCircleimage() {
		mBannerFl = (FrameLayout) findViewById(R.id.goods_detail_framelayout);
		mBannerFl.setVisibility(View.VISIBLE);
		mViewFlow = (ViewFlow) findViewById(R.id.goods_detail_viewflow);
		mIndic = (CircleFlowIndicator) findViewById(R.id.goods_detail_viewflowindic);

		// showcircleimage();
	}

	private void showcircleimage() {
		mBannerAdapter = new GoodsDetailBannerAdapter(mContext, mBannerActivityList);
		mViewFlow.setAdapter(mBannerAdapter);
		mViewFlow.setViewGroup(mBannerFl);
		mViewFlow.setmSideBuffer(mBannerActivityList.size()); // 实际图片张数
		mViewFlow.setFlowIndicator(mIndic);
		mViewFlow.setTimeSpan(2000);
		mViewFlow.setSelection(mBannerActivityList.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		mViewFlow.requestFocus();
	}

	private void initData() {
		mGoodsId = (String) getIntent().getSerializableExtra(GoodsDetailActivity.GOODS_ID_KEY);
		if (!TextUtils.isEmpty(getIntent().getAction())) {
			mNowAction = getIntent().getAction();
		}
		if (!TextUtils.isEmpty(mGoodsId)) {
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			GoodsLogic.getGoodsById(mContext, mHandler, mGoodsId);
		}

	}

	private void fillUpGoodsData() {
		mGoodsNameTv.setText(!TextUtils.isEmpty(mGoods.getName()) ? mGoods.getName().trim() : "");
		mGoodsPriceTv.setText(!TextUtils.isEmpty(mGoods.getFinalPrice()) ? "¥" + mGoods.getFinalPrice().trim() : "¥");
		
		mAOGBtn.setVisibility(View.GONE);
		if (!"1".equals(mGoods.getIsSaleable())) {
			mIsSaleAbleTv.setText("非现货");
			mAOGBtn.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(mGoods.getDeliverytime()) && mGoods.getDeliverytime().equals("")) {
			mDeliverytimeTv.setText("下单后" + mGoods.getDeliverytime() + "天发货");
		}

		mSlidingMenu.setmUrl(mGoods.getDescription());

		fillUpBanner();
		fillUpComment();
		fillUpCollection();
	}

	private void fillUpBanner() {
		mBannerActivityList.clear();
		for (int i = 0; i < mGoods.getLitterImage().size(); i++) {
			Ads promotion = new Ads();
			promotion.setImgUrl(mGoods.getLitterImage().get(i));
			mBannerActivityList.add(promotion);
		}
		showcircleimage();
	}

	private void fillUpComment() {
		mCommentRatioTv
				.setText(!TextUtils.isEmpty(mGoods.getRating_avg()) ? mGoods.getRating_avg().trim() + "%" : "0%");
		mCommentPersonNumTv.setText(
				!TextUtils.isEmpty(mGoods.getReview_total()) ? mGoods.getReview_total().trim() + "人评论" : "0人评论");
		mCommentTimeTv.setText(!TextUtils.isEmpty(mGoods.getComment().getCreated_at())
				? mGoods.getComment().getCreated_at().trim() : "");
		mCommentContentTv.setText(
				!TextUtils.isEmpty(mGoods.getComment().getDetail()) ? mGoods.getComment().getDetail().trim() : "暂无评论");
		mCommentNameTv.setText(
				!TextUtils.isEmpty(mGoods.getComment().getNickname()) ? mGoods.getComment().getNickname().trim() : "");

		setStar(Integer.parseInt(mGoods.getComment().getStart_avg()));
	}

	private void setStar(int score) {
		switch (score) {
		case 1: {
			mCommentScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreSecondIb.setBackgroundResource(R.drawable.star_defalut);
			mCommentScoreThirdIb.setBackgroundResource(R.drawable.star_defalut);
			mCommentScoreFourthIb.setBackgroundResource(R.drawable.star_defalut);
			mCommentScoreFifthIb.setBackgroundResource(R.drawable.star_defalut);
			break;
		}
		case 2: {
			mCommentScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreSecondIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreThirdIb.setBackgroundResource(R.drawable.star_defalut);
			mCommentScoreFourthIb.setBackgroundResource(R.drawable.star_defalut);
			mCommentScoreFifthIb.setBackgroundResource(R.drawable.star_defalut);
			break;
		}
		case 3: {
			mCommentScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreSecondIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreThirdIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreFourthIb.setBackgroundResource(R.drawable.star_defalut);
			mCommentScoreFifthIb.setBackgroundResource(R.drawable.star_defalut);
			break;
		}
		case 4: {
			mCommentScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreSecondIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreThirdIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreFourthIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreFifthIb.setBackgroundResource(R.drawable.star_defalut);
			break;
		}
		case 5: {
			mCommentScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreSecondIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreThirdIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreFourthIb.setBackgroundResource(R.drawable.star_select_icon);
			mCommentScoreFifthIb.setBackgroundResource(R.drawable.star_select_icon);
			break;
		}

		default:
			break;
		}
	}

	private void fillUpCollection() {
		if (!TextUtils.isEmpty(mGoods.getInWishList()) && "1".equals(mGoods.getInWishList())) {
			mCollectionIv.setImageResource(R.drawable.collection_normal_bottom);
			mCollectionTv.setText(getString(R.string.collection_already));
		} else {
			mCollectionIv.setImageResource(R.drawable.collection_normal_bottom);
			mCollectionTv.setText(getString(R.string.collection_not));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_detail_cart_ll: {
			if (!UserInfoManager.getLoginIn(mContext)) {
				Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
				intent.setAction(LoginActivity.ORIGIN_FROM_GOODS_DETAIL_KEY);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			} else if (0 == mCartNum) {
				ActivitiyInfoManager.finishActivity("com.plmt.boommall.ui.activity.CategoryActivity");
				ActivitiyInfoManager.finishActivity("com.plmt.boommall.ui.activity.GoodsListActivity");
				ActivitiyInfoManager.finishActivity("com.plmt.boommall.ui.activity.Html5Activity");

				finish();
				ShoppingCartActivity.isNeedUpdate = true;
				HomeActivity.setTab(HomeActivity.TAB_CART);
			} else {
				mProgressDialog = new CustomProgressDialog(mContext);
				mProgressDialog.show();
				CartLogic.add(mContext, mCartHandler, mGoods.getId(), String.valueOf(mCartNum));
			}
			break;
		}
		case R.id.goods_detail_add_cart_btn: {

			mAddCartBtn.setClickable(false);
			if (TextUtils.isEmpty(mGoods.getNum()) || "null".equals(mGoods.getNum())) {
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
			if (!UserInfoManager.getLoginIn(mContext)) {
				Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
				intent.setAction(LoginActivity.ORIGIN_FROM_GOODS_DETAIL_KEY);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			} else {
				mProgressDialog = new CustomProgressDialog(mContext);
				mProgressDialog.show();
				if (!isCollection) {
					CollectionLogic.add(mContext, mCollectionHandler, mGoodsId);
				} else {
					CollectionLogic.del(mContext, mCollectionHandler, mGoodsId);
				}
			}
			break;
		}
		case R.id.goods_detail_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}
		case R.id.goods_detail_share_iv: {
			String title = "旺铺商城分享";
			if (null != mGoods) {
				title = mGoods.getName();
			}
			startActivity(Intent.createChooser(
					new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_SUBJECT, title)
							.putExtra(Intent.EXTRA_TEXT, title).setType(Constants.MIMETYPE_TEXT_PLAIN),
					getString(R.string.menu_share)));
			break;
		}
		case R.id.goods_detail_more_comment_rl: {
			Intent intent = new Intent(GoodsDetailActivity.this, MoreCommentsActivity.class);
			intent.putExtra("id", mGoodsId);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.goods_detail_reduced_price_btn: {
			if (!UserInfoManager.getLoginIn(mContext)) {
				Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
				intent.setAction(LoginActivity.ORIGIN_FROM_GOODS_DETAIL_KEY);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			} else {
				Intent intent = new Intent(GoodsDetailActivity.this, NoticePriceReduceActivity.class);
				intent.putExtra("sku", mGoods.getSku());
				intent.putExtra("price", mGoods.getFinalPrice());
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				// NoticeLogic.setPriceReduce(mContext, mNoticeHandler,
				// mGoods.getSku(), price, phone);
			}
			break;
		}

		case R.id.goods_detail_aog_btn: {
			if (!UserInfoManager.getLoginIn(mContext)) {
				Intent intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
				intent.setAction(LoginActivity.ORIGIN_FROM_GOODS_DETAIL_KEY);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			} else {
				mProgressDialog = new CustomProgressDialog(mContext);
				mProgressDialog.show();
				if (TextUtils.isEmpty(UserInfoManager.userInfo.getAccount())) {
					UserInfoManager.setUserInfo(mContext);
				}
				NoticeLogic.setAOG(mContext, mNoticeHandler, mGoods.getSku(), UserInfoManager.userInfo.getAccount());
			}
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
