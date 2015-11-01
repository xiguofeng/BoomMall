package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Banner;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.entity.DemoItem;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.HomeRecommend;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.network.logic.PromotionLogic;
import com.plmt.boommall.ui.adapter.BannerAdapter;
import com.plmt.boommall.ui.adapter.DemoAdapter;
import com.plmt.boommall.ui.adapter.MainGoodsAdapter;
import com.plmt.boommall.ui.adapter.MainGvCategoryAdapter;
import com.plmt.boommall.ui.view.CustomClassifyView;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.asymmetricgridview.widget.AsymmetricGridView;
import com.plmt.boommall.ui.view.gridview.CustomGridView;
import com.plmt.boommall.ui.view.iosdialog.AlertDialog;
import com.plmt.boommall.ui.view.listview.HorizontalListView;
import com.plmt.boommall.ui.view.srollview.BorderScrollView;
import com.plmt.boommall.ui.view.srollview.BorderScrollView.OnBorderListener;
import com.plmt.boommall.ui.view.viewflow.CircleFlowIndicator;
import com.plmt.boommall.ui.view.viewflow.ViewFlow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener {

	private Context mContext;

	private MultiStateView mMultiStateView;
	private BorderScrollView mCustomSv;
	private boolean isSvInTop = true;
	private boolean hasMeasured = false;

	private RelativeLayout mTitleRl;
	private RelativeLayout mSearchBgRl;
	private RelativeLayout mSearchStandardRl;
	private RelativeLayout mSearchSimpleRl;
	private ImageView mSearchSimpleIv;
	private LinearLayout mSearchLl;
	private EditText mSearchEt;
	private ImageView mSearchIv;
	private ImageView mSimpleSearchIv;
	private LinearLayout mStandardMsgLl;
	private ImageView mSimpleMsgIv;

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mIndic;
	private ArrayList<Banner> mBannerActivityList = new ArrayList<Banner>();
	private BannerAdapter mBannerAdapter;
	private FrameLayout mBannerFl;

	private CustomGridView mCategoryGv;
	private ArrayList<Banner> mCategoryList = new ArrayList<Banner>();
	private MainGvCategoryAdapter mCategoryAdapter;

	private LinearLayout mCategoryAndGoodsListLl;
	private ArrayList<Category> mTopCategoryList = new ArrayList<Category>();
	private HashMap<String, HomeRecommend> mRecommendMap = new LinkedHashMap<String, HomeRecommend>();
	private int mRecommendSize = 0;

	private HorizontalListView mHotGoodsLv;
	private ArrayList<Goods> mHotGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mHotGoodsAdapter;

	private HorizontalListView mFirstGoodsLv;
	private ArrayList<Goods> mFirstGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mFirstGoodsAdapter;

	private HorizontalListView mSecondGoodsLv;
	private ArrayList<Goods> mSecondGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mSecondGoodsAdapter;

	private HorizontalListView mThirdGoodsLv;
	private ArrayList<Goods> mThirdGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mThirdGoodsAdapter;

	private HorizontalListView mFourthGoodsLv;
	private ArrayList<Goods> mFourthGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mFourthGoodsAdapter;

	private HorizontalListView mFifthGoodsLv;
	private ArrayList<Goods> mFifthGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mFifthGoodsAdapter;

	private AsymmetricGridView mAsymmetricGridView;
	private DemoAdapter mGoodsAdapter;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	public static boolean isBannerNeedUpdate = true;
	public static boolean isRoundNeedUpdate = true;
	public static boolean isCateAndGoodsNeedUpdate = true;
	private CustomProgressDialog mProgressDialog;

	private Handler mPromotionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case PromotionLogic.BANNER_GET_SUC: {
				if (null != msg.obj) {
					mBannerActivityList.clear();
					mBannerActivityList.addAll((Collection<? extends Banner>) msg.obj);
					showcircleimage();
					isBannerNeedUpdate = false;
				}

				break;

			}
			case PromotionLogic.BANNER_GET_FAIL: {
				break;
			}
			case PromotionLogic.BANNER_GET_EXCEPTION: {
				break;
			}

			case PromotionLogic.ROUND_GET_SUC: {
				if (null != msg.obj) {
					mCategoryList.clear();
					mCategoryList.addAll((Collection<? extends Banner>) msg.obj);
					mCategoryAdapter.notifyDataSetChanged();
					isRoundNeedUpdate = false;
				}

				break;

			}
			case PromotionLogic.ROUND_GET_FAIL: {
				break;
			}
			case PromotionLogic.ROUND_GET_EXCEPTION: {
				break;
			}

			case PromotionLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			// mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);

		}

	};

	private Handler mCateAndGoodsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case GoodsLogic.CATEGROY_TOP_LIST_GET_SUC: {
				if (null != msg.obj) {
					// mTopCategoryList.clear();
					// mTopCategoryList.addAll((Collection<? extends Category>)
					// msg.obj);
					//
					// if (mTopCategoryList.size() > 0) {
					// GoodsLogic.getSubCategoryHome(mContext,
					// mCateAndGoodsHandler,
					// mTopCategoryList.get(0).getName());
					// }
					//
					// for (int i = 0; i < mTopCategoryList.size(); i++) {
					// GoodsLogic.getSubCategoryHome(mContext,
					// mCateAndGoodsHandler,
					// mTopCategoryList.get(i).getName());
					// }
				}
				break;
			}
			case GoodsLogic.CATEGROY_TOP_LIST_GET_FAIL: {
				break;
			}
			case GoodsLogic.CATEGROY_TOP_LIST_GET_EXCEPTION: {
				break;
			}

			case GoodsLogic.CATEGROY_SUB_HOME_LIST_GET_SUC: {
				break;

			}
			case GoodsLogic.CATEGROY_SUB_HOME_LIST_GET_FAIL: {
				break;
			}
			case GoodsLogic.CATEGROY_SUB_HOME_LIST_GET_EXCEPTION: {
				break;
			}

			case GoodsLogic.CATEGROY_HOME_LIST_GET_SUC: {
				if (null != msg.obj) {
					mRecommendMap.clear();
					mRecommendMap = (HashMap<String, HomeRecommend>) msg.obj;
					initGoodsShow();
					isCateAndGoodsNeedUpdate = false;
				}
				break;

			}
			case GoodsLogic.CATEGROY_HOME_LIST_GET_FAIL: {
				break;
			}
			case GoodsLogic.CATEGROY_HOME_LIST_GET_EXCEPTION: {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = MainActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		HomeActivity.setCartMenuShow(false);
	}

	private void initView() {
		mCustomSv = (BorderScrollView) findViewById(R.id.main_sv);
		mTitleRl = (RelativeLayout) findViewById(R.id.main_title_ll);
		ViewTreeObserver vto = mTitleRl.getViewTreeObserver();

		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if (!hasMeasured) {
					int height = mTitleRl.getMeasuredHeight();
					mCustomSv.setHeadHeight(height);
					hasMeasured = true;
				}
				return true;
			}
		});
		int height = mTitleRl.getMeasuredHeight();

		// mCustomSv.setTopViewHeight(height);
		mCustomSv.setOnBorderListener(new OnBorderListener() {

			@Override
			public void onTop() {
				if (!isSvInTop) {
					isSvInTop = true;
					mSearchStandardRl.setVisibility(View.VISIBLE);
					mSearchSimpleRl.setVisibility(View.GONE);
				}
			}

			@Override
			public void onBottom() {

			}

			@Override
			public void onLeaveTop() {
				if (isSvInTop) {
					mSearchStandardRl.setVisibility(View.GONE);
					mSearchSimpleRl.setVisibility(View.VISIBLE);
					isSvInTop = false;
				}

			}
		});

		initSearchAndMsgView();
		initCircleimage();
		initCategoryView();
		initGoodsShow();

	}

	private void initSearchAndMsgView() {
		mSearchBgRl = (RelativeLayout) findViewById(R.id.main_search_rl);
		mSearchBgRl.setOnClickListener(this);

		mSimpleSearchIv = (ImageView) findViewById(R.id.main_title_simple_search_iv);
		mSimpleSearchIv.setOnClickListener(this);

		mSearchStandardRl = (RelativeLayout) findViewById(R.id.main_title_standard_ll);
		mSearchSimpleRl = (RelativeLayout) findViewById(R.id.main_title_simple_ll);

		mSearchLl = (LinearLayout) findViewById(R.id.main_search_ll);

		mSearchIv = (ImageView) findViewById(R.id.main_search_iv);
		mSearchIv.setOnClickListener(this);

		mSearchEt = (EditText) findViewById(R.id.main_search_et);
		mSearchEt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
					mSearchLl.setVisibility(View.GONE);
					mSearchIv.setVisibility(View.VISIBLE);
				} else {
					// 此处为失去焦点时的处理内容
					mSearchEt.setText("");
					mSearchLl.setVisibility(View.VISIBLE);
					mSearchIv.setVisibility(View.GONE);
				}
			}
		});

		mStandardMsgLl = (LinearLayout) findViewById(R.id.main_msg_ll);
		mStandardMsgLl.setOnClickListener(this);
		mSimpleMsgIv = (ImageView) findViewById(R.id.main_title_simple_msg_iv);
		mSimpleMsgIv.setOnClickListener(this);
	}

	private void initCircleimage() {
		mBannerFl = (FrameLayout) findViewById(R.id.main_framelayout);
		mBannerFl.setVisibility(View.VISIBLE);
		mViewFlow = (ViewFlow) findViewById(R.id.main_viewflow);
		mIndic = (CircleFlowIndicator) findViewById(R.id.main_viewflowindic);
	}

	private void showcircleimage() {
		mBannerAdapter = new BannerAdapter(mContext, mBannerActivityList);
		mViewFlow.setAdapter(mBannerAdapter);
		mViewFlow.setmSideBuffer(mBannerActivityList.size()); // 实际图片张数
		mViewFlow.setFlowIndicator(mIndic);
		mViewFlow.setViewGroup(mBannerFl);
		mViewFlow.setTimeSpan(2000);
		mViewFlow.setSelection(mBannerActivityList.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		mViewFlow.requestFocus();
	}

	private void initCategoryView() {
		mCategoryGv = (CustomGridView) findViewById(R.id.main_category_gv);

		mCategoryAdapter = new MainGvCategoryAdapter(mContext, mCategoryList);
		mCategoryGv.setAdapter(mCategoryAdapter);
		mCategoryGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initGoodsShow() {
		mCategoryAndGoodsListLl = (LinearLayout) findViewById(R.id.main_list_categoty_ll);

		// mHotGoodsLv = (HorizontalListView)
		// findViewById(R.id.main_hot_brand_goods_lv);
		// for (int i = 0; i < 6; i++) {
		// Goods goods = new Goods();
		// goods.setName("商品" + i);
		// goods.setImage("http://img3.douban.com/view/commodity_story/medium/public/p19671.jpg");
		// mHotGoodsList.add(goods);
		// }
		// mHotGoodsAdapter = new MainGoodsAdapter(mContext, mHotGoodsList);
		// mHotGoodsLv.setAdapter(mHotGoodsAdapter);
		// mHotGoodsAdapter.notifyDataSetChanged();
		//
		// mFirstGoodsLv = (HorizontalListView)
		// findViewById(R.id.main_first_classify_goods_lv);
		// for (int i = 0; i < 6; i++) {
		// Goods goods = new Goods();
		// goods.setName("1商品" + i);
		// goods.setImage("http://img3.douban.com/view/commodity_story/medium/public/p19671.jpg");
		// mFirstGoodsList.add(goods);
		// }
		// mFirstGoodsAdapter = new MainGoodsAdapter(mContext, mFirstGoodsList);
		// mFirstGoodsLv.setAdapter(mFirstGoodsAdapter);
		// mFirstGoodsAdapter.notifyDataSetChanged();
		//
		// mSecondGoodsLv = (HorizontalListView)
		// findViewById(R.id.main_second_classify_goods_lv);
		// for (int i = 0; i < 6; i++) {
		// Goods goods = new Goods();
		// goods.setName("2商品" + i);
		// goods.setImage("http://img3.douban.com/view/commodity_story/medium/public/p19671.jpg");
		// mSecondGoodsList.add(goods);
		// }
		// mSecondGoodsAdapter = new MainGoodsAdapter(mContext,
		// mSecondGoodsList);
		// mSecondGoodsLv.setAdapter(mSecondGoodsAdapter);
		// mSecondGoodsAdapter.notifyDataSetChanged();
		//
		// mThirdGoodsLv = (HorizontalListView)
		// findViewById(R.id.main_three_classify_goods_lv);
		// for (int i = 0; i < 6; i++) {
		// Goods goods = new Goods();
		// goods.setName("3商品" + i);
		// goods.setImage("http://img3.douban.com/view/commodity_story/medium/public/p19671.jpg");
		// mThirdGoodsList.add(goods);
		// }
		// mThirdGoodsAdapter = new MainGoodsAdapter(mContext, mThirdGoodsList);
		// mThirdGoodsLv.setAdapter(mThirdGoodsAdapter);
		// mThirdGoodsAdapter.notifyDataSetChanged();
		//
		// mFourthGoodsLv = (HorizontalListView)
		// findViewById(R.id.main_fourth_classify_goods_lv);
		// for (int i = 0; i < 6; i++) {
		// Goods goods = new Goods();
		// goods.setName("4商品" + i);
		// goods.setImage("http://img3.douban.com/view/commodity_story/medium/public/p19671.jpg");
		// mFourthGoodsList.add(goods);
		// }
		// mFourthGoodsAdapter = new MainGoodsAdapter(mContext,
		// mFourthGoodsList);
		// mFourthGoodsLv.setAdapter(mFourthGoodsAdapter);
		// mFourthGoodsAdapter.notifyDataSetChanged();
		//
		// mFifthGoodsLv = (HorizontalListView)
		// findViewById(R.id.main_fifth_classify_goods_lv);
		// for (int i = 0; i < 6; i++) {
		// Goods goods = new Goods();
		// goods.setName("5商品" + i);
		// goods.setImage("http://img3.douban.com/view/commodity_story/medium/public/p19671.jpg");
		// mFifthGoodsList.add(goods);
		// }
		// mFifthGoodsAdapter = new MainGoodsAdapter(mContext, mFifthGoodsList);
		// mFifthGoodsLv.setAdapter(mFifthGoodsAdapter);
		// mFifthGoodsAdapter.notifyDataSetChanged();
		// initialize your items array

		for (Entry<String, HomeRecommend> entry : mRecommendMap.entrySet()) {
			Log.e("xxx_entry_key", entry.getKey());

			// entry.getKey();
			// entry.getValue();
			CustomClassifyView cv = new CustomClassifyView(mContext, entry.getValue());
			mCategoryAndGoodsListLl.addView(cv);
		}

	}

	private void initData() {
		mProgressDialog.show();
		PromotionLogic.getBannerList(mContext, mPromotionHandler);
		PromotionLogic.getRounds(mContext, mPromotionHandler);
		GoodsLogic.getHomeCategory(mContext, mCateAndGoodsHandler);
	}
	
	private void getNotLoadData(){
		if(isBannerNeedUpdate||isRoundNeedUpdate||isCateAndGoodsNeedUpdate){
			mProgressDialog.show();
			if(isBannerNeedUpdate){
				PromotionLogic.getBannerList(mContext, mPromotionHandler);
			}
			if(isRoundNeedUpdate){
				PromotionLogic.getRounds(mContext, mPromotionHandler);
			}
			if(isCateAndGoodsNeedUpdate){
				GoodsLogic.getHomeCategory(mContext, mCateAndGoodsHandler);
			}
		}
	}

	private List<DemoItem> getMoreItems(int qty) {
		List<DemoItem> items = new ArrayList<>();

		for (int i = 0; i < qty; i++) {
			int colSpan = Math.random() < 0.2f ? 2 : 1;
			// Swap the next 2 lines to have items with variable
			// column/row span.
			// int rowSpan = Math.random() < 0.2f ? 2 : 1;
			int rowSpan = colSpan;
			DemoItem item = new DemoItem(colSpan, rowSpan, i);
			items.add(item);
		}

		return items;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_search_rl: {
			Intent intent = new Intent(MainActivity.this, SearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		case R.id.main_title_simple_search_iv: {
			Intent intent = new Intent(MainActivity.this, SearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		case R.id.main_msg_ll: {
			Intent intent = new Intent(MainActivity.this, MsgActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		case R.id.main_title_simple_msg_iv: {
			Intent intent = new Intent(MainActivity.this, MsgActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

			new AlertDialog(MainActivity.this).builder().setTitle(getString(R.string.prompt))
					.setMsg(getString(R.string.exit_str))
					.setPositiveButton(getString(R.string.confirm), new OnClickListener() {
						@Override
						public void onClick(View v) {
							finish();
						}
					}).setNegativeButton(getString(R.string.cancal), new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();

			// if ((System.currentTimeMillis() - exitTime) > 2000) {
			// Toast.makeText(getApplicationContext(), R.string.exit,
			// Toast.LENGTH_SHORT).show();
			// exitTime = System.currentTimeMillis();
			// } else {
			// finish();
			// }
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
