package com.plmt.boommall.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.entity.Filter;
import com.plmt.boommall.entity.FilterProperty;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.config.MsgRequest;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.ui.adapter.FilterPropertyAdapter;
import com.plmt.boommall.ui.adapter.GoodsAdapter;
import com.plmt.boommall.ui.adapter.GoodsGvPagingAdaper;
import com.plmt.boommall.ui.adapter.RVCategoryAdapter;
import com.plmt.boommall.ui.adapter.RVGoodsAdapter;
import com.plmt.boommall.ui.utils.MyItemClickListener;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.gridview.paging.PagingGridView;
import com.plmt.boommall.ui.view.listview.CustomListView;
import com.plmt.boommall.ui.view.listview.pullrefresh.XListView;
import com.plmt.boommall.utils.ActivitiyInfoManager;
import com.plmt.boommall.utils.ScreenUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsListActivity extends Activity
		implements OnClickListener, MyItemClickListener, XListView.IXListViewListener, PagingGridView.ScrollListener {
	public static final int VIEW_MODE_LIST = 0;
	public static final int VIEW_MODE_GRID = 1;

	private Context mContext;
	private MultiStateView mMultiStateView;
	private LinearLayout mSearchLl;
	private EditText mSearchEt;
	private ImageView mSearchIv;

	private LinearLayout mCompositeLl;
	private TextView mCompositeTv;
	private ImageView mCompositeIv;

	private LinearLayout mPriceLl;
	private TextView mPriceTv;
	private ImageView mPriceIv;

	private LinearLayout mSalesLl;
	private TextView mSalesTv;
	private ImageView mSalesIv;

	private ImageView mFilterIv;
	private DrawerLayout mDrawerLayout;
	private boolean isFilterOpen = false;

	private CustomListView mFilterPropertyLv;
	private ArrayList<Filter> mFilterPropertyList = new ArrayList<>();
	private FilterPropertyAdapter mFilterPropertyAdapter;

	private HashMap<String, ArrayList<Filter>> mFilterMap = new HashMap<>();
	private ArrayList<Filter> mRootFilterList = new ArrayList<>();

	private TextView mFilterConfrimTv;
	private TextView mFilterCancelTv;

	private ArrayList<Category> mCategoryList = new ArrayList<Category>();
	private RVCategoryAdapter mCategoryAdapter;
	private RVGoodsAdapter mRVGoodsAdapter;

	private XListView mGoodsLv;
	private GoodsAdapter mGoodsAdapter;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private PagingGridView mGoodsGv;
	private GoodsGvPagingAdaper mGoodsGvAdapter;
	private ArrayList<Goods> mGoodsGvList = new ArrayList<Goods>();

	private ImageView mViewModeIv;
	private ImageView mBackIv;

	private ImageView mBackTopIv;

	private String mCatgoryName;
	private String mCatgoryID;

	private String mNowSortType;

	private int mCurrentPageNum = 1;
	private int mCurrentViewMode = 0;

	private int mTotalSize = 0;

	private boolean mIsLoadComplete = true;

	private boolean mScrollFlag = false;// 标记是否滑动
	private int mLastVisibleItemPosition = 0;// 标记上次滑动位置

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {

			case GoodsLogic.GOODS_LIST_BY_KEY_GET_SUC: {
				if (null != msg.obj) {
					Bundle bData = msg.getData();
					if (null != bData) {
						mTotalSize = Integer.parseInt(bData.getString("total"));
					}

					if (1 == mCurrentPageNum) {
						mGoodsList.clear();
						mGoodsGvList.clear();
					}
					mCurrentPageNum++;
					mGoodsList.addAll((Collection<? extends Goods>) msg.obj);

					if (mCurrentViewMode == VIEW_MODE_LIST) {
						mGoodsAdapter.notifyDataSetChanged();
						onLoadComplete();
					} else {
						if (mGoodsGv.getAdapter() == null) {
							mGoodsGv.setAdapter(mGoodsGvAdapter);
						}
						ArrayList<Goods> goodsList = new ArrayList<Goods>();
						goodsList.addAll(mGoodsList);
						mGoodsGvList.addAll(goodsList);
						mGoodsGv.onFinishLoading(true, goodsList);
					}

				}
				break;
			}
			case GoodsLogic.GOODS_LIST_BY_KEY_GET_FAIL: {
				break;
			}
			case GoodsLogic.GOODS_LIST_BY_KEY_GET_EXCEPTION: {
				break;
			}

			default:
				break;
			}

			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			onLoadComplete();
		}

	};

	Handler mFilterHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case GoodsLogic.FILTE_GET_SUC: {
				if (null != msg.obj) {
					mFilterMap.clear();
					mFilterMap.putAll((Map<? extends String, ? extends ArrayList<Filter>>) msg.obj);
					fillUpFilterData();
				}
				break;
			}
			case GoodsLogic.FILTE_GET_FAIL: {
				break;
			}
			case GoodsLogic.FILTE_GET_EXCEPTION: {
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
		setContentView(R.layout.goods_list);
		mContext = GoodsListActivity.this;
		if (!ActivitiyInfoManager.activitityMap.containsKey(ActivitiyInfoManager.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap.put(ActivitiyInfoManager.getCurrentActivityName(mContext), this);
		}
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// refreshGoods();
	}

	private void initView() {
		mMultiStateView = (MultiStateView) findViewById(R.id.goods_list_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(), "Fetching Data", Toast.LENGTH_SHORT).show();
					}
				});

		mSearchLl = (LinearLayout) findViewById(R.id.goods_list_search_ll);
		mSearchLl.setOnClickListener(this);

		mSearchIv = (ImageView) findViewById(R.id.goods_list_search_iv);
		mSearchIv.setOnClickListener(this);

		mSearchEt = (EditText) findViewById(R.id.goods_list_search_et);
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

		mViewModeIv = (ImageView) findViewById(R.id.goods_list_show_mode_iv);
		mViewModeIv.setOnClickListener(this);
		mBackIv = (ImageView) findViewById(R.id.goods_list_back_iv);
		mBackIv.setOnClickListener(this);

		mBackTopIv = (ImageView) findViewById(R.id.goods_list_back_top_iv);
		mBackTopIv.setOnClickListener(this);

		initFilterView();
		initDrawerLayout();
		initListView();
		initGridView();
		showViewMode(VIEW_MODE_LIST);
	}

	private void initData() {
		mCatgoryName = getIntent().getStringExtra("categoryName");
		mCatgoryID = getIntent().getStringExtra("categoryID");
		mProgressDialog = new CustomProgressDialog(mContext);
		mProgressDialog.show();
		fetchGoods(mNowSortType);
	}

	private void initFilterView() {
		mCompositeLl = (LinearLayout) findViewById(R.id.goods_list_composite_ll);
		mCompositeTv = (TextView) findViewById(R.id.goods_list_composite_tv);
		mCompositeIv = (ImageView) findViewById(R.id.goods_list_composite_iv);

		mPriceLl = (LinearLayout) findViewById(R.id.goods_list_price_ll);
		mPriceTv = (TextView) findViewById(R.id.goods_list_price_tv);
		mPriceIv = (ImageView) findViewById(R.id.goods_list_price_iv);

		mSalesLl = (LinearLayout) findViewById(R.id.goods_list_sales_ll);
		mSalesTv = (TextView) findViewById(R.id.goods_list_sales_tv);
		mSalesIv = (ImageView) findViewById(R.id.goods_list_sales_iv);

		mCompositeLl.setOnClickListener(this);
		mPriceLl.setOnClickListener(this);
		mSalesLl.setOnClickListener(this);

		mFilterIv = (ImageView) findViewById(R.id.goods_list_filter_iv);
		mFilterIv.setOnClickListener(this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.goods_list_dl);
	}

	private void setFilterViewDefalut() {
		mCompositeTv.setTextColor(getResources().getColor(R.color.gray_character));
		mPriceTv.setTextColor(getResources().getColor(R.color.gray_character));
		mSalesTv.setTextColor(getResources().getColor(R.color.gray_character));

		mCompositeIv.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_top));
		mPriceIv.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_top));
		mSalesIv.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_top));
	}

	private void initDrawerLayout() {

		mFilterConfrimTv = (TextView) findViewById(R.id.goods_list_filter_confirm_tv);
		mFilterConfrimTv.setOnClickListener(this);
		mFilterCancelTv = (TextView) findViewById(R.id.goods_list_filter_cancel_tv);
		mFilterCancelTv.setOnClickListener(this);

		mFilterPropertyLv = (CustomListView) findViewById(R.id.goods_list_filter_lv);
		mFilterPropertyAdapter = new FilterPropertyAdapter(mContext, mFilterPropertyList);
		mFilterPropertyLv.setAdapter(mFilterPropertyAdapter);
		mFilterPropertyLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if ("1".equals(mFilterPropertyList.get(position).getLevel())) {
					setFilterContent(mFilterPropertyList.get(position).getId());
				} else {
					setFilterRoot(mFilterPropertyList.get(position));
				}
			}
		});

	}

	private void initListView() {
		mGoodsLv = (XListView) findViewById(R.id.goods_list_goods_xlv);
		mGoodsLv.setPullRefreshEnable(false);
		mGoodsLv.setPullLoadEnable(true);
		mGoodsLv.setAutoLoadEnable(true);
		mGoodsLv.setXListViewListener(this);
		mGoodsLv.setRefreshTime(getTime());

		mGoodsAdapter = new GoodsAdapter(mContext, mGoodsList);
		mGoodsLv.setAdapter(mGoodsAdapter);

		mGoodsLv.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
					mScrollFlag = false;
					// 判断滚动到底部
					// if (mGoodsLv.getLastVisiblePosition() ==
					// (mGoodsLv.getCount() - 1)) {
					// mBackTopIv.setVisibility(View.VISIBLE);
					// }
					// 判断滚动到顶部
					if (mGoodsLv.getFirstVisiblePosition() == 0) {
						mBackTopIv.setVisibility(View.GONE);
					}

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
					mScrollFlag = true;
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
					mScrollFlag = true;
					break;
				}

			}

			/**
			 * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
			 * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
				if (mScrollFlag && firstVisibleItem > 0) {
					if (firstVisibleItem > mLastVisibleItemPosition) {// 下滑
						mBackTopIv.setVisibility(View.VISIBLE);
					} else if (firstVisibleItem < mLastVisibleItemPosition) {// 上滑
						// mBackTopIv.setVisibility(View.GONE);
					} else {
						return;
					}
					mLastVisibleItemPosition = firstVisibleItem;
				}
				if (firstVisibleItem == 0) {
					mBackTopIv.setVisibility(View.GONE);
				}
			}
		});

		mGoodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {
					Intent intent = new Intent(GoodsListActivity.this, GoodsDetailActivity.class);
					intent.setAction(GoodsDetailActivity.ORIGIN_FROM_CATE_ACTION);
					Bundle bundle = new Bundle();
					bundle.putSerializable(GoodsDetailActivity.GOODS_ID_KEY, mGoodsList.get(position - 1).getId());
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}
		});

	}

	private void initGridView() {
		mGoodsGv = (PagingGridView) findViewById(R.id.goods_list_goods_pgv);
		mGoodsGvAdapter = new GoodsGvPagingAdaper();
		// mGoodsGv.setAdapter(mGoodsGvAdapter);
		mGoodsGv.setmListener(this);
		mGoodsGv.setHasMoreItems(true);
		mGoodsGv.setPagingableListener(new PagingGridView.Pagingable() {
			@Override
			public void onLoadMoreItems() {
				if (mCurrentPageNum == 1) {
					fetchGoods(mNowSortType);
				} else if (mCurrentPageNum * MsgRequest.PAGE_SIZE < mTotalSize) {
					fetchGoods(mNowSortType);
				} else {
					mGoodsGv.onFinishLoading(false, null);
				}
			}
		});
		mGoodsGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(GoodsListActivity.this, GoodsDetailActivity.class);
				intent.setAction(GoodsDetailActivity.ORIGIN_FROM_CATE_ACTION);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GoodsDetailActivity.GOODS_ID_KEY, mGoodsGvList.get(position).getId());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@SuppressLint("NewApi")
	private void showViewMode(int mode) {
		mCurrentViewMode = mode;
		mGoodsGvList.clear();
		if (mode == VIEW_MODE_LIST) {
			mViewModeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid_selector));
			mGoodsGv.setVisibility(View.GONE);
			mGoodsLv.setVisibility(View.VISIBLE);
			mGoodsAdapter.notifyDataSetChanged();
		} else {
			mViewModeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_selector));
			mGoodsLv.setVisibility(View.GONE);
			mGoodsGv.setVisibility(View.VISIBLE);
			if (mGoodsGv.getAdapter() == null) {
				mGoodsGv.setAdapter(mGoodsGvAdapter);
			}
			mGoodsGvAdapter.removeAllItems();
			ArrayList<Goods> goodsList = new ArrayList<Goods>();
			goodsList.addAll(mGoodsList);
			mGoodsGvList.addAll(goodsList);
			mGoodsGv.onFinishLoading(true, goodsList);
		}
	}

	private void fetchGoods(String sortType) {
		GoodsLogic.getGoodsListByCategory(mContext, mHandler, mCatgoryName, mCurrentPageNum, MsgRequest.PAGE_SIZE,
				sortType);
	}

	private void getFilter(String price, String brand, String continent) {
		GoodsLogic.getFilter(mContext, mFilterHandler, mCatgoryID, price, brand, continent);
	}

	private void setFilterRoot(Filter filter) {
		mFilterPropertyList.clear();
		for (Filter rootFilter : mRootFilterList) {
			if (rootFilter.getId().equals(filter.getId())) {
				rootFilter.setContent(filter.getLabel());
				rootFilter.setValue(filter.getValue());
			}
			rootFilter.setLevel("1");
			mFilterPropertyList.add(rootFilter);
		}
		mFilterPropertyAdapter.notifyDataSetChanged();
	}

	private void setFilterContent(String key) {
		mRootFilterList.clear();
		mRootFilterList.addAll(mFilterPropertyList);
		mFilterPropertyList.clear();
		ArrayList<Filter> filterPropertyList = new ArrayList<>();
		filterPropertyList.addAll(mFilterMap.get(key));
		for (Filter filter : filterPropertyList) {
			filter.setId(key);
			filter.setTitle(filter.getLabel());
			filter.setLevel("2");
			filter.setContent("");
			mFilterPropertyList.add(filter);
		}
		mFilterPropertyAdapter.notifyDataSetChanged();
	}

	private void fillUpFilterData() {
		mFilterPropertyList.clear();
		for (String key : mFilterMap.keySet()) {
			Filter filterProperty = new Filter();
			filterProperty.setId(key);
			filterProperty.setLevel("1");
			filterProperty.setContent("全部");
			filterProperty.setValue("全部");
			if ("price".equals(key)) {
				filterProperty.setTitle("价格");
			} else if ("color".equals(key)) {
				filterProperty.setTitle("颜色");
			} else if ("brand_filter".equals(key)) {
				filterProperty.setTitle("品牌");
			} else if ("continent".equals(key)) {
				filterProperty.setTitle("国际");
			}
			mFilterPropertyList.add(filterProperty);
		}
		mFilterPropertyAdapter.notifyDataSetChanged();
	}

	private void filter() {
		mProgressDialog.show();

	}

	private void showBackTop() {

	}

	private void search(String key) {
	}

	private void onLoadComplete() {
		mGoodsLv.stopRefresh();
		mGoodsLv.stopLoadMore();
		mGoodsLv.setRefreshTime(getTime());
		mIsLoadComplete = true;
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}

	@Override
	public void onRefresh() {
		Log.e("xxx_onRefresh()", "");
		fetchGoods(mNowSortType);

	}

	@Override
	public void onLoadMore() {
		if (mIsLoadComplete && mCurrentViewMode == VIEW_MODE_LIST && mTotalSize > mGoodsList.size()) {
			fetchGoods(mNowSortType);
			mIsLoadComplete = !mIsLoadComplete;
		} else {
			onLoadComplete();
		}
		// }else if(mCurrentViewMode == VIEW_MODE_GRID){
		// fetchGoods(mNowSortType);
		// mIsLoadComplete = !mIsLoadComplete;
		// }
	}

	@Override
	public void onScrollDown() {

	}

	@Override
	public void onPagingScrollDown(boolean isShowBackTop) {
		if (isShowBackTop) {
			mBackTopIv.setVisibility(View.VISIBLE);
		} else {
			mBackTopIv.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_list_search_iv: {
			if (!TextUtils.isEmpty(mSearchEt.getText().toString().trim())) {
				search(mSearchEt.getText().toString().trim());
			} else {
				Toast.makeText(mContext, getString(R.string.search_hint), Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case R.id.goods_list_show_mode_iv: {
			if (mCurrentViewMode == VIEW_MODE_GRID) {
				showViewMode(VIEW_MODE_LIST);
			} else {
				showViewMode(VIEW_MODE_GRID);
			}
			break;
		}

		case R.id.goods_list_composite_ll: {
			setFilterViewDefalut();
			mCompositeTv.setTextColor(getResources().getColor(R.color.red_character));
			mCompositeIv.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_top));
			mCurrentPageNum = 1;
			mNowSortType = "";
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			fetchGoods(mNowSortType);
			break;
		}
		case R.id.goods_list_price_ll: {
			setFilterViewDefalut();
			mPriceTv.setTextColor(getResources().getColor(R.color.red_character));
			mPriceIv.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_top));
			mCurrentPageNum = 1;
			mNowSortType = "price";
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			fetchGoods(mNowSortType);
			break;
		}
		case R.id.goods_list_sales_ll: {
			setFilterViewDefalut();
			mSalesTv.setTextColor(getResources().getColor(R.color.red_character));
			mSalesIv.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_top));
			mCurrentPageNum = 1;
			mNowSortType = "salestotal";
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			fetchGoods(mNowSortType);
			break;
		}
		case R.id.goods_list_filter_iv: {
			if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			} else {
				mDrawerLayout.openDrawer(Gravity.RIGHT);
				getFilter("", "", "");
			}
			break;
		}

		case R.id.goods_list_filter_confirm_tv: {
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
			filter();
			break;
		}
		case R.id.goods_list_filter_cancel_tv: {
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
			break;
		}

		case R.id.goods_list_search_ll: {
			Intent intent = new Intent(GoodsListActivity.this, SearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		case R.id.goods_list_back_iv: {
			ActivitiyInfoManager.finishActivity("com.plmt.boommall.ui.activity.GoodsListActivity");
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}

		case R.id.goods_list_back_top_iv: {

			if (mCurrentViewMode == VIEW_MODE_GRID) {

				mGoodsGv.post(new Runnable() {
					@Override
					public void run() {
						mGoodsGv.scrollTo(0, 0);
					}
				});
			} else {
				mGoodsAdapter.notifyDataSetChanged();
				mGoodsLv.setSelection(0);
			}
			mBackTopIv.setVisibility(View.GONE);
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onItemClick(View view, int postion) {
		Toast.makeText(mContext, "onItemClick:" + postion, Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			ActivitiyInfoManager.finishActivity("com.plmt.boommall.ui.activity.GoodsListActivity");
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
