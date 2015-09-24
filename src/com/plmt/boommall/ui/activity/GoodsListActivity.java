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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.plmt.boommall.ui.adapter.RVCategoryAdapter;
import com.plmt.boommall.ui.adapter.RVGoodsAdapter;
import com.plmt.boommall.ui.utils.MyItemClickListener;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.listview.pullrefresh.XListView;

public class GoodsListActivity extends Activity implements OnClickListener,
		MyItemClickListener, XListView.IXListViewListener {

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
	private int mCurrentPage = 1;

	private float y;
	private HashMap<String, Object> mAllMsgMap = new HashMap<String, Object>();
	private HashMap<String, Object> mSearchMsgMap = new HashMap<String, Object>();
	private HashMap<String, Object> mShowMsgMap = new HashMap<String, Object>();

	private long exitTime = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {

			case GoodsLogic.GOODS_LIST_BY_KEY_GET_SUC: {
				if (null != msg.obj) {
					mGoodsList.clear();
					mGoodsList.addAll((Collection<? extends Goods>) msg.obj);
					mGoodsAdapter.notifyDataSetChanged();
					onLoadComplete();
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
		mContext = GoodsListActivity.this;
		initView();
		initData();
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


		mSearchLl = (LinearLayout) findViewById(R.id.category_goods_search_ll);

		mSearchIv = (ImageView) findViewById(R.id.category_goods_search_iv);
		mSearchIv.setOnClickListener(this);

		mSearchEt = (EditText) findViewById(R.id.category_goods_search_et);
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

	}

	private void initData() {
		mGoodsLv = (XListView) findViewById(R.id.goods_list_goods_xlv);
		mGoodsLv.setPullRefreshEnable(true);
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
					Intent intent = new Intent(GoodsListActivity.this,
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
		refreshGoods();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
	}

	private void refreshGoods() {
		GoodsLogic.getGoodsListByCategory(mContext, mHandler, "1", 1, 1);
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
		case R.id.category_goods_search_iv: {
			if (!TextUtils.isEmpty(mSearchEt.getText().toString().trim())) {
				search(mSearchEt.getText().toString().trim());
			} else {
				Toast.makeText(mContext, getString(R.string.search_hint),
						Toast.LENGTH_SHORT).show();
			}
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
			GoodsListActivity.this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
