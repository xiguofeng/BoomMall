package com.plmt.boommall.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.ui.adapter.GoodsAdapter;
import com.plmt.boommall.ui.adapter.GoodsGvPagingAdaper;
import com.plmt.boommall.ui.adapter.RVCategoryAdapter;
import com.plmt.boommall.ui.adapter.RVGoodsAdapter;
import com.plmt.boommall.ui.utils.MyItemClickListener;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.gridview.paging.PagingGridView;
import com.plmt.boommall.ui.view.listview.pullrefresh.XListView;

public class SearchActivity extends Activity implements OnClickListener,
		MyItemClickListener, XListView.IXListViewListener {
	public static final int VIEW_MODE_LIST = 0;
	public static final int VIEW_MODE_GRID = 1;

	private Context mContext;
	private MultiStateView mMultiStateView;
	private LinearLayout mSearchLl;
	private EditText mSearchEt;
	private ImageView mSearchIv;

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

	private String mCatgoryName;

	private int mCurrentPage = 1;
	private int mCurrentViewMode = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {

			case GoodsLogic.GOODS_LIST_BY_KEY_GET_SUC: {
				if (null != msg.obj) {
					mGoodsList.clear();
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

			mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
			onLoadComplete();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_list);
		mContext = SearchActivity.this;
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

		mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

		mSearchLl = (LinearLayout) findViewById(R.id.goods_list_search_ll);

		mSearchIv = (ImageView) findViewById(R.id.goods_list_search_iv);
		mSearchIv.setOnClickListener(this);

		mSearchEt = (EditText) findViewById(R.id.goods_list_search_et);
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

		mSearchLl.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

			}
		});
		mViewModeIv = (ImageView) findViewById(R.id.goods_list_show_mode_iv);
		mViewModeIv.setOnClickListener(this);
		mBackIv = (ImageView) findViewById(R.id.goods_list_back_iv);
		mBackIv.setOnClickListener(this);

		initListView();
		initGridView();
		showViewMode(VIEW_MODE_LIST);
	}

	private void initData() {
		mCatgoryName = getIntent().getStringExtra("categoryName");
		refreshGoods();
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
				// Log.i(TAG, "滚动状态变化");
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// Log.i(TAG, "正在滚动");
			}
		});
		mGoodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					Intent intent = new Intent(SearchActivity.this,
							GoodsDetailActivity.class);
					intent.setAction(GoodsDetailActivity.ORIGIN_FROM_CATE_ACTION);
					Bundle bundle = new Bundle();
					bundle.putSerializable(GoodsDetailActivity.GOODS_ID_KEY,
							mGoodsList.get(position - 1).getId());
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
		mGoodsGv.setHasMoreItems(true);
		mGoodsGv.setPagingableListener(new PagingGridView.Pagingable() {
			@Override
			public void onLoadMoreItems() {
				if (mCurrentPage < 3) {
					refreshGoods();
				} else {
					mGoodsGv.onFinishLoading(false, null);
				}
			}
		});
		mGoodsGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SearchActivity.this,
						GoodsDetailActivity.class);
				intent.setAction(GoodsDetailActivity.ORIGIN_FROM_CATE_ACTION);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GoodsDetailActivity.GOODS_ID_KEY,
						mGoodsGvList.get(position).getId());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@SuppressLint("NewApi")
	private void showViewMode(int mode) {
		mCurrentViewMode = mode;
		if (mode == VIEW_MODE_LIST) {
			mViewModeIv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_list_selector));
			mGoodsGv.setVisibility(View.GONE);
			mGoodsLv.setVisibility(View.VISIBLE);
			mGoodsAdapter.notifyDataSetChanged();
		} else {
			mViewModeIv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_grid_selector));
			mGoodsLv.setVisibility(View.GONE);
			mGoodsGv.setVisibility(View.VISIBLE);
			if (mGoodsGv.getAdapter() == null) {
				mGoodsGv.setAdapter(mGoodsGvAdapter);
			}
			ArrayList<Goods> goodsList = new ArrayList<Goods>();
			goodsList.addAll(mGoodsList);
			mGoodsGvList.addAll(goodsList);
			mGoodsGv.onFinishLoading(true, goodsList);
		}
	}

	private void refreshGoods() {
		GoodsLogic.getGoodsListByCategory(mContext, mHandler, mCatgoryName, 1,
				1);
	}

	private void search(String key) {
	}

	private void onLoadComplete() {
		mGoodsLv.stopRefresh();
		mGoodsLv.stopLoadMore();
		mGoodsLv.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	@Override
	public void onRefresh() {
		Log.e("xxx_onRefresh()", "");
		refreshGoods();

	}

	@Override
	public void onLoadMore() {
		Log.e("xxx_onLoadMore", "");
		refreshGoods();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_list_search_iv: {
			if (!TextUtils.isEmpty(mSearchEt.getText().toString().trim())) {
				search(mSearchEt.getText().toString().trim());
			} else {
				Toast.makeText(mContext, getString(R.string.search_hint),
						Toast.LENGTH_SHORT).show();
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
		case R.id.goods_list_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onItemClick(View view, int postion) {
		Toast.makeText(mContext, "onItemClick:" + postion, Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			SearchActivity.this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
