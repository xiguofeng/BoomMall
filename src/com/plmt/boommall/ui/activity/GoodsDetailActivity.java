package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
import com.plmt.boommall.entity.Ads;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.logic.CartLogic;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.ui.adapter.GoodsDetailBannerAdapter;
import com.plmt.boommall.ui.view.BadgeView;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.viewflow.CircleFlowIndicator;
import com.plmt.boommall.ui.view.viewflow.ViewFlow;
import com.plmt.boommall.utils.ActivitiyInfoManager;
import com.plmt.boommall.utils.CartManager;

public class GoodsDetailActivity extends Activity implements OnClickListener {

	public static final String GOODS_ID_KEY = "GoodsIdKey";

	public static final String ORIGIN_FROM_ADS_ACTION = "ADS";

	public static final String ORIGIN_FROM_MAIN_ACTION = "MAINS";

	public static final String ORIGIN_FROM_CATE_ACTION = "CATE";

	private Context mContext;

	private MultiStateView mMultiStateView;

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mIndic;
	private ArrayList<Ads> mBannerActivityList = new ArrayList<Ads>();
	private GoodsDetailBannerAdapter mBannerAdapter;
	private FrameLayout mBannerFl;

	private TextView mGoodsNameTv;

	private TextView mGoodsPriceTv;

	private TextView mGoodsOrgPriceTv;

	private TextView mGoodsProductAreaTv;

	private TextView mGoodsFactoryTv;

	private TextView mGoodsBrandTv;

	private TextView mGoodsJHLTv;

	private TextView mGoodsDegreeTv;

	private TextView mGoodsScentTv;

	private TextView mGoodsMaterialTv;

	private TextView mGoodsBriefTv;

	public EditText mNum;

	private LinearLayout mCollectionLl;

	private LinearLayout mCartLl;

	private LinearLayout mBriefLl;

	public ImageButton mAddIb;

	public ImageButton mReduceIb;

	private Button mAddCartBtn;

	private ImageView mBackIv;

	private ImageView mCollectionIv;

	private ImageView mCartIv;// 购物车

	private ViewGroup anim_mask_layout;// 动画层

	private ImageView mBall;// 小圆点

	private BadgeView mBuyNumView;// 购物车上的数量标签

	private Goods mGoods;

	private String mGoodsId;

	private String mNowAction = ORIGIN_FROM_MAIN_ACTION;

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
			mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
		}

	};

	Handler mCartHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CartLogic.CART_MODIFY_SUC: {
				if (null != msg.obj) {
				}

				break;

			}
			case CartLogic.CART_MODIFY_FAIL: {
				break;
			}
			case CartLogic.CART_MODIFY_EXCEPTION: {
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
				if (null != msg.obj) {

				}

				break;
			}
			case CartLogic.CART_ADD_FAIL: {
				Toast.makeText(mContext, R.string.login_fail, Toast.LENGTH_SHORT).show();
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
			mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
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
		mMultiStateView = (MultiStateView) findViewById(R.id.goods_detail_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(), "Fetching Data", Toast.LENGTH_SHORT).show();
					}
				});
		// mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
		mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
		initCircleimage();

		mCollectionLl = (LinearLayout) findViewById(R.id.goods_detail_collection_ll);
		mCartLl = (LinearLayout) findViewById(R.id.goods_detail_cart_ll);
		mCollectionLl.setOnClickListener(this);
		mCartLl.setOnClickListener(this);

		mBackIv = (ImageView) findViewById(R.id.goods_detail_back_iv);
		mCartIv = (ImageView) findViewById(R.id.goods_detail_cart_iv);
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
		mBannerAdapter = new GoodsDetailBannerAdapter(mContext, mBannerActivityList);
		mViewFlow.setAdapter(mBannerAdapter);
		mViewFlow.setmSideBuffer(3); // 实际图片张数
		mViewFlow.setFlowIndicator(mIndic);
		mViewFlow.setTimeSpan(2000);
		mViewFlow.setSelection(3 * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		mViewFlow.requestFocus();
	}

	private void initData() {
		mGoodsId = (String) getIntent().getSerializableExtra(GoodsDetailActivity.GOODS_ID_KEY);
		mNowAction = getIntent().getAction();

		// if (null != mGoods) {
		// fillUpGoodsData();
		// }
		if (!TextUtils.isEmpty(mGoodsId)) {
			GoodsLogic.getGoodsById(mContext, mHandler, mGoodsId);
		}

	}

	private void fillUpGoodsData() {

		mGoodsNameTv.setText(!TextUtils.isEmpty(mGoods.getName()) ? mGoods.getName().trim() : "");
		mGoodsPriceTv.setText(!TextUtils.isEmpty(mGoods.getFinalPrice()) ? "¥" + mGoods.getFinalPrice().trim() : "¥");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_detail_cart_ll: {
			if (!TextUtils.isEmpty(mNowAction) && ORIGIN_FROM_ADS_ACTION.equals(mNowAction)) {
				// ActivitiyInfoManager
				// .finishActivity("com.xgf.winecome.ui.activity.SpecialEventsActivity");
			}
			finish();
			break;
		}
		case R.id.goods_detail_add_cart_btn: {

			mAddCartBtn.setClickable(false);
			if (TextUtils.isEmpty(mGoods.getNum()) || "null".equals(mGoods.getNum())) {
				mGoods.setNum("0");
			}
			mAddCartBtn.setClickable(true);
			mBuyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			mBuyNumView.setText("1");
			mBuyNumView.show();

			// CartLogic.add(mContext, mCartHandler, mGoods.getId(), "1");
			//
			// int addNum = 1;
			// boolean isSuc = CartManager.cartModifyByDetail(mGoods, addNum);
			break;
		}
		case R.id.goods_detail_collection_ll: {
			int addNum = Integer.parseInt(mNum.getText().toString().trim());
			boolean isSuc = CartManager.cartModifyByDetail(mGoods, addNum);

			if (!TextUtils.isEmpty(mNowAction) && ORIGIN_FROM_ADS_ACTION.equals(mNowAction)) {
				// ActivitiyInfoManager
				// .finishActivity("com.xgf.winecome.ui.activity.SpecialEventsActivity");
			}
			finish();

			break;
		}
		case R.id.goods_detail_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
