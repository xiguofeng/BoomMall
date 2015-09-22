package com.plmt.boommall.ui.activity;

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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.ui.view.BadgeView;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.utils.ActivitiyInfoManager;
import com.plmt.boommall.utils.CartManager;
import com.plmt.boommall.utils.SystemUtils;

public class GoodsDetailActivity extends Activity implements OnClickListener {

	public static final String GOODS_ID_KEY = "GoodsIdKey";

	public static final String ORIGIN_FROM_ADS_ACTION = "ADS";

	public static final String ORIGIN_FROM_MAIN_ACTION = "MAINS";

	public static final String ORIGIN_FROM_CATE_ACTION = "CATE";

	private Context mContext;

	private MultiStateView mMultiStateView;

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

	private ImageView mGoodsIconIv;

	private ImageView mBackIv;

	private LinearLayout mAddCartLl;

	private LinearLayout mNowBuyLl;

	private LinearLayout mBriefLl;

	public ImageButton mAddIb;

	public ImageButton mReduceIb;

	public EditText mNum;

	private Goods mGoods;

	private String mGoodsId;

	private String mNowAction = ORIGIN_FROM_MAIN_ACTION;

	private ImageView mCartIv;// 购物车

	private ViewGroup anim_mask_layout;// 动画层

	private ImageView mBall;// 小圆点

	private BadgeView mBuyNumView;// 购物车上的数量标签

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
		mMultiStateView = (MultiStateView) findViewById(R.id.goods_detail_multiStateView);
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
		// mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
		mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

		mGoodsIconIv = (ImageView) findViewById(R.id.goods_detail_iv);

		mGoodsNameTv = (TextView) findViewById(R.id.goods_detail_name_tv);
		mGoodsPriceTv = (TextView) findViewById(R.id.goods_detail_price_tv);
		mGoodsOrgPriceTv = (TextView) findViewById(R.id.goods_detail_original_prices_tv);

		mGoodsProductAreaTv = (TextView) findViewById(R.id.goods_detail_area_tv);
		mGoodsFactoryTv = (TextView) findViewById(R.id.goods_detail_factory_tv);
		mGoodsBrandTv = (TextView) findViewById(R.id.goods_detail_brand_type_tv);
		mGoodsJHLTv = (TextView) findViewById(R.id.goods_detail_jhl_tv);
		mGoodsDegreeTv = (TextView) findViewById(R.id.goods_detail_degree_tv);
		mGoodsScentTv = (TextView) findViewById(R.id.goods_detail_scent_tv);
		mGoodsMaterialTv = (TextView) findViewById(R.id.goods_detail_material_tv);
		mGoodsBriefTv = (TextView) findViewById(R.id.goods_detail_brief_tv);

		mBackIv = (ImageView) findViewById(R.id.goods_detail_back_iv);
		mBackIv.setOnClickListener(this);
		mCartIv = (ImageView) findViewById(R.id.goods_detail_cart_iv);
		mCartIv.setOnClickListener(this);

		// mBuyNumView = new BadgeView(mContext, mCartIv);
		mBuyNumView = new BadgeView(this, mCartIv);
		mBuyNumView.setText(String.valueOf(CartManager.getAllCartNum()));
		mBuyNumView.show();

		mBriefLl = (LinearLayout) findViewById(R.id.goods_detail_content);
		mAddCartLl = (LinearLayout) findViewById(R.id.goods_detail_add_cart_ll);
		mAddCartLl.setOnClickListener(this);
		mNowBuyLl = (LinearLayout) findViewById(R.id.goods_detail_now_buy_ll);
		mNowBuyLl.setOnClickListener(this);

		mNum = (EditText) findViewById(R.id.goods_detail_count_et);
		mAddIb = (ImageButton) findViewById(R.id.goods_detail_add_ib);
		mReduceIb = (ImageButton) findViewById(R.id.goods_detail_reduce_ib);

		mAddIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// mGoods.setNum(String.valueOf(Integer.parseInt(mGoods.getNum())
				// + 1));
				mNum.setText(String.valueOf(Integer.parseInt(mNum.getText()
						.toString().trim()) + 1));
			}
		});
		mReduceIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Integer.parseInt(mNum.getText().toString().trim()) > 1) {
					// mGoods.setNum(String.valueOf(Integer.parseInt(mGoods
					// .getNum()) - 1));
					mNum.setText(String.valueOf(Integer.parseInt(mNum.getText()
							.toString().trim()) - 1));
				}

			}
		});
	}

	private void initData() {
		mGoodsId = (String) getIntent().getSerializableExtra(
				GoodsDetailActivity.GOODS_ID_KEY);
		mNowAction = getIntent().getAction();

		// if (null != mGoods) {
		// fillUpGoodsData();
		// }
		if (!TextUtils.isEmpty(mGoodsId)) {
			GoodsLogic.getGoodsById(mContext, mHandler, mGoodsId);
		}

	}

	private void fillUpGoodsData() {
		ImageLoader.getInstance().displayImage(mGoods.getImage(), mGoodsIconIv);

		mGoods.setNum("1");
		mNum.setText(mGoods.getNum());
		mGoodsNameTv.setText(!TextUtils.isEmpty(mGoods.getName()) ? mGoods
				.getName() : "");
		mGoodsPriceTv.setText(!TextUtils.isEmpty(mGoods.getFinalPrice()) ? "¥"
				+ mGoods.getFinalPrice() : "¥");
		mGoodsOrgPriceTv.setText(!TextUtils.isEmpty(mGoods.getPrice()) ? "原价:¥"
				+ mGoods.getPrice() : "原价:¥");
		// mGoodsProductAreaTv
		// .setText(!TextUtils.isEmpty(mGoods.getArea()) ? "商品产地："
		// + mGoods.getArea() : "商品产地：");
		// mGoodsFactoryTv.setText(!TextUtils.isEmpty(mGoods.getFactory()) ?
		// "酒厂："
		// + mGoods.getFactory() : "酒厂：");
		// mGoodsBrandTv.setText(!TextUtils.isEmpty(mGoods.getLevel()) ? "产品品牌："
		// + mGoods.getLevel() : "产品品牌：");
		// mGoodsJHLTv.setText(!TextUtils.isEmpty(mGoods.getContent()) ? "净含量："
		// + mGoods.getContent() : "净含量：");
		// mGoodsDegreeTv.setText(!TextUtils.isEmpty(mGoods.getDegree()) ?
		// "酒精度："
		// + mGoods.getDegree() : "酒精度：");
		// mGoodsScentTv.setText(!TextUtils.isEmpty(mGoods.getModel()) ? "香型："
		// + mGoods.getModel() : "香型：");
		// mGoodsMaterialTv
		// .setText(!TextUtils.isEmpty(mGoods.getMetrial()) ? "原料："
		// + mGoods.getMetrial() : "原料：");
		// mGoodsBriefTv.setText(mGoods.getDesc());

		// String string = mGoods.getImagesUrl();
		// if (!TextUtils.isEmpty(string)) {
		// String[] strings = string.split(";");
		// for (int i = 0; i < strings.length; i++) {
		// CustomImageView customImageView = new CustomImageView(mContext,
		// strings[i]);
		// // ImageView imageView = new ImageView(this);
		// // imageView.setLayoutParams(new LayoutParams(
		// // LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// // imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		// // // imageView.setImageResource(R.drawable.ic_launcher);
		// // ImageLoader.getInstance().displayImage(strings[i],
		// // imageView);
		// mBriefLl.addView(customImageView);
		// }
		// }

	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final ViewGroup parent, final View view,
			int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	private void setAnim(final View v, int[] startLocation) {
		anim_mask_layout = null;
		anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(v);// 把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, v,
				startLocation);
		int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
		mCartIv.getLocationInWindow(endLocation);// shopCart是那个购物车

		// 计算位移
		int endX = 0 - startLocation[0] + SystemUtils.dip2Px(mContext, 330);// 动画位移的X坐标
		int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(800);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				v.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
				// mBuyNumView.setText(String.valueOf(CartManager.getAllCartNum()));//
				// mBuyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				// mBuyNumView.show();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_detail_cart_iv: {
			if (!TextUtils.isEmpty(mNowAction)
					&& ORIGIN_FROM_ADS_ACTION.equals(mNowAction)) {
				// ActivitiyInfoManager
				// .finishActivity("com.xgf.winecome.ui.activity.SpecialEventsActivity");
			}
			finish();
			// HomeActivity.setTab(HomeActivity.TAB_CART);
			break;
		}
		case R.id.goods_detail_add_cart_ll: {
			// int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
			// v.getLocationInWindow(startLocation);//
			// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
			// mBall = new ImageView(mContext);//
			// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
			// mBall.setImageResource(R.drawable.sign);// 设置buyImg的图片
			// setAnim(mBall, startLocation);// 开始执行动画
			//
			mAddCartLl.setClickable(false);
			// int addNum = Integer.parseInt(mNum.getText().toString().trim());
			int addNum = 1;
			boolean isSuc = CartManager.cartModifyByDetail(mGoods, addNum);
			if (isSuc) {
				mAddCartLl.setClickable(true);
				mBuyNumView
						.setText(String.valueOf(CartManager.getAllCartNum()));
				mBuyNumView.show();
				// Toast.makeText(getApplicationContext(),
				// getString(R.string.add_cart_suc), Toast.LENGTH_SHORT)
				// .show();
			}
			break;
		}
		case R.id.goods_detail_now_buy_ll: {
			int addNum = Integer.parseInt(mNum.getText().toString().trim());
			boolean isSuc = CartManager.cartModifyByDetail(mGoods, addNum);

			if (!TextUtils.isEmpty(mNowAction)
					&& ORIGIN_FROM_ADS_ACTION.equals(mNowAction)) {
				// ActivitiyInfoManager
				// .finishActivity("com.xgf.winecome.ui.activity.SpecialEventsActivity");
			}
			finish();
			// HomeActivity.setTab(HomeActivity.TAB_CART);

			// ArrayList<Goods> goodsList = new ArrayList<Goods>();
			// mGoods.setNum(mNum.getText().toString().trim());
			// goodsList.add(mGoods);
			// CartManager.setsDetailBuyList(goodsList);
			//
			// Intent intent = new Intent(GoodsDetailActivity.this,
			// PersonInfoActivity.class);
			// intent.setAction(PersonInfoActivity.ORIGIN_FROM_DETAIL_ACTION);
			// startActivity(intent);
			// overridePendingTransition(R.anim.push_left_in,
			// R.anim.push_left_out);
			break;
		}
		case R.id.goods_detail_back_iv: {
			finish();
			// overridePendingTransition(R.anim.push_right_in,
			// R.anim.push_right_out);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
}
