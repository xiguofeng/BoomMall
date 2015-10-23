package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Ads;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.entity.DemoItem;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.ui.adapter.BannerAdapter;
import com.plmt.boommall.ui.adapter.DemoAdapter;
import com.plmt.boommall.ui.adapter.MainGoodsAdapter;
import com.plmt.boommall.ui.adapter.MainGvCategoryAdapter;
import com.plmt.boommall.ui.view.CustomClassifyView;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.asymmetricgridview.widget.AsymmetricGridView;
import com.plmt.boommall.ui.view.gridview.CustomGridView;
import com.plmt.boommall.ui.view.iosdialog.AlertDialog;
import com.plmt.boommall.ui.view.listview.HorizontalListView;
import com.plmt.boommall.ui.view.srollview.BorderScrollView;
import com.plmt.boommall.ui.view.srollview.BorderScrollView.OnBorderListener;
import com.plmt.boommall.ui.view.viewflow.CircleFlowIndicator;
import com.plmt.boommall.ui.view.viewflow.ViewFlow;

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
	private ArrayList<Ads> mBannerActivityList = new ArrayList<Ads>();
	private BannerAdapter mBannerAdapter;
	private FrameLayout mBannerFl;

	private CustomGridView mCategoryGv;
	private ArrayList<Category> mCategoryList = new ArrayList<Category>();
	private MainGvCategoryAdapter mCategoryAdapter;
	private int[] pic_path_classify = { R.drawable.menu_guide_1,
			R.drawable.menu_guide_2, R.drawable.menu_guide_3,
			R.drawable.menu_guide_4, R.drawable.menu_guide_5,
			R.drawable.menu_guide_6, R.drawable.menu_guide_7,
			R.drawable.menu_guide_8 };

	private LinearLayout mCategoryAndGoodsListLl;

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

	private long exitTime = 0;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = MainActivity.this;

		initView();
		// initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
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

		// mMultiStateView = (MultiStateView)
		// findViewById(R.id.main_multiStateView);
		// mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR)
		// .findViewById(R.id.retry)
		// .setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mMultiStateView
		// .setViewState(MultiStateView.VIEW_STATE_LOADING);
		// Toast.makeText(getApplicationContext(),
		// "Fetching Data", Toast.LENGTH_SHORT).show();
		// }
		// });
		// // mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
		// mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

		initSearchAndMsgView();
		initCircleimage();
		initCategoryView();
		initGoodsShow();
		// mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
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
		mSearchEt
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
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
		for (int i = 0; i < 3; i++) {
			Ads promotion = new Ads();
			promotion.setImgUrl("");
			mBannerActivityList.add(promotion);
		}

		showcircleimage();
	}

	private void showcircleimage() {
		mBannerAdapter = new BannerAdapter(mContext, mBannerActivityList);
		mViewFlow.setAdapter(mBannerAdapter);
		mViewFlow.setmSideBuffer(3); // 实际图片张数
		mViewFlow.setFlowIndicator(mIndic);
		mViewFlow.setViewGroup(mBannerFl);
		mViewFlow.setTimeSpan(2000);
		mViewFlow.setSelection(3 * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		mViewFlow.requestFocus();
	}

	private void initCategoryView() {
		mCategoryGv = (CustomGridView) findViewById(R.id.main_category_gv);
		int size = pic_path_classify.length;
		for (int i = 0; i < size; i++) {
			Category category = new Category();
			category.setLocalImage(pic_path_classify[i]);
			mCategoryList.add(category);
		}

		mCategoryAdapter = new MainGvCategoryAdapter(mContext, mCategoryList);
		mCategoryGv.setAdapter(mCategoryAdapter);
		mCategoryGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						CreateOrderActivity.class);
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
		for (int i = 0; i < 5; i++) {
			CustomClassifyView cv = new CustomClassifyView(mContext, null);
			mCategoryAndGoodsListLl.addView(cv);
		}

	}

	private void initGoodsShowOld() {
		// mAsymmetricGridView = (AsymmetricGridView)
		// findViewById(R.id.main_goods_classify_lv);
		// for (int i = 0; i < 10; i++) {
		// Goods goods = new Goods();
		// goods.setName("商品" + i);
		// goods.setImage("http://img3.douban.com/view/commodity_story/medium/public/p19671.jpg");
		// mGoodsList.add(goods);
		// }
		//
		// mAsymmetricGridView.setRequestedColumnWidth(Utils.dpToPx(this, 120));
		// mAsymmetricGridView.setRequestedColumnCount(3);
		// mAsymmetricGridView
		// .setRequestedHorizontalSpacing(Utils.dpToPx(this, 3));
		// mAsymmetricGridView.setDebugging(true);
		//
		// // initialize your items array
		// mGoodsAdapter = new DefaultListAdapter(this, getMoreItems(5));
		// AsymmetricGridViewAdapter asymmetricAdapter = new
		// AsymmetricGridViewAdapter<>(
		// this, mAsymmetricGridView, mGoodsAdapter);
		//
		// mAsymmetricGridView.setAdapter(asymmetricAdapter);
		//
		// // initialize your items array

	}

	private void initData() {
		GoodsLogic.getGoodsListByCategory(mContext, mHandler, "1", 1, 1);
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
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			new AlertDialog(MainActivity.this)
					.builder()
					.setTitle(getString(R.string.prompt))
					.setMsg(getString(R.string.exit_str))
					.setPositiveButton(getString(R.string.confirm),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									finish();
								}
							})
					.setNegativeButton(getString(R.string.cancal),
							new OnClickListener() {
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
