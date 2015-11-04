package com.plmt.boommall.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.config.MsgRequest;
import com.plmt.boommall.network.logic.SearchLogic;
import com.plmt.boommall.ui.adapter.GoodsAdapter;
import com.plmt.boommall.ui.adapter.HotWordAdapter;
import com.plmt.boommall.ui.adapter.MySimpleAdapter;
import com.plmt.boommall.ui.view.AutoClearEditText;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.gridview.CustomGridView;
import com.plmt.boommall.ui.view.listview.pullrefresh.XListView;
import com.plmt.boommall.utils.CacheManager;

public class SearchActivity extends Activity implements OnClickListener,
		XListView.IXListViewListener {

	private AutoClearEditText mSearchGoodsEt;

	private ImageView mBackIv;
	private TextView mSearchTagTv;
	private TextView mSearchTv;

	private ListView mSearchHistroyLv;
	private MySimpleAdapter mSimpleAdapter;
	private ArrayList<String> mSearchHistoryList = new ArrayList<String>();

	private CustomGridView mHotWordGv;
	private HotWordAdapter mHotWordAdapter;
	private ArrayList<String> mHotWordList = new ArrayList<String>();

	private String mSearchKey;

	private XListView mGoodsLv;
	private GoodsAdapter mGoodsAdapter;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private int mCurrentPageNum = 1;
	private String mNowSortType;

	private CustomProgressDialog mCustomProgressDialog;

	private final Context mContext = SearchActivity.this;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case SearchLogic.NORAML_GET_SUC: {
				mGoodsList.clear();
				mGoodsList.addAll((ArrayList<Goods>) msg.obj);
				if (mGoodsList.size() > 0) {
					mGoodsAdapter.notifyDataSetChanged();
					mSearchHistroyLv.setVisibility(View.GONE);
					mGoodsLv.setVisibility(View.VISIBLE);
					mSearchTagTv.setText(getString(R.string.find_goods));
				} else {
					Toast.makeText(mContext, "没有查询到相关商品", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}
			case SearchLogic.NORAML_GET_FAIL: {
				Toast.makeText(mContext, "没有查询到相关商品", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			case SearchLogic.NORAML_GET_EXCEPTION: {
				Toast.makeText(mContext, "没有查询到相关商品", Toast.LENGTH_SHORT)
				.show();
				break;
			}
			case SearchLogic.HOT_KEY_GET_SUC: {
				if (null != msg.obj) {
					mHotWordList.clear();
					mHotWordList.addAll((Collection<? extends String>) msg.obj);
					mHotWordAdapter.notifyDataSetChanged();
				}
				break;
			}
			case SearchLogic.HOT_KEY_GET_FAIL: {
				break;
			}
			case SearchLogic.HOT_KEY_GET_EXCEPTION: {

				break;
			}
			case SearchLogic.NET_ERROR: {
				break;
			}
			default:
				break;
			}

			if (null != mCustomProgressDialog) {
				mCustomProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		mCustomProgressDialog = new CustomProgressDialog(mContext);
		initView();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();
		CacheManager.setSearchHistroy(mContext);
		mSearchHistoryList.clear();
		mSearchHistoryList.addAll(CacheManager.sSearchHistroyList);
		mSimpleAdapter.notifyDataSetChanged();
	}

	private void initView() {
		// 初始化控件
		mSearchGoodsEt = (AutoClearEditText) findViewById(R.id.search_et);
		mSearchTv = (TextView) findViewById(R.id.search_tv);
		mSearchTagTv = (TextView) findViewById(R.id.search_tag_tv);
		mSearchHistroyLv = (ListView) findViewById(R.id.search_history_lv);

		mBackIv = (ImageView) findViewById(R.id.search_back_iv);
		mBackIv.setOnClickListener(this);

		initHotGv();
		initXListView();
	}

	private void initHotGv() {
		mHotWordGv = (CustomGridView) findViewById(R.id.search_hot_gv);
		mHotWordAdapter = new HotWordAdapter(mContext, mHotWordList);
		mHotWordGv.setAdapter(mHotWordAdapter);
		mHotWordGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSearchKey = mHotWordList.get(position);
				mSearchGoodsEt.setText(mSearchKey);
				getGoodsData(mSearchKey);
			}
		});
	}

	private void initXListView() {
		mGoodsLv = (XListView) findViewById(R.id.search_goods_xlv);
		mGoodsLv.setPullRefreshEnable(false);
		mGoodsLv.setPullLoadEnable(false);
		mGoodsLv.setAutoLoadEnable(false);
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
					intent.setAction(GoodsDetailActivity.ORIGIN_FROM_SEAR_ACTION);
					Bundle bundle = new Bundle();
					bundle.putSerializable(GoodsDetailActivity.GOODS_ID_KEY,
							mGoodsList.get(position - 1).getId());
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}
		});

	}

	private void initData() {
		mSearchTv.setOnClickListener(this);

		mSimpleAdapter = new MySimpleAdapter(SearchActivity.this,
				mSearchHistoryList);
		mSearchHistroyLv.setAdapter(mSimpleAdapter);
		mSearchHistroyLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (!TextUtils.isEmpty(mSearchHistoryList.get(position))) {
					mSearchKey = mSearchHistoryList.get(position);
					mSearchGoodsEt.setText(mSearchKey);
					getGoodsData(mSearchKey);
				}
			}
		});
		mSearchHistoryList.clear();
		mSearchHistoryList.addAll(CacheManager.sSearchHistroyList);
		mSimpleAdapter.notifyDataSetChanged();

		if (null != getIntent().getExtras()) {
			mSearchKey = getIntent().getExtras().getString("searchKey");
			if (!TextUtils.isEmpty(mSearchKey)) {
				mSearchGoodsEt.setText(mSearchKey);
				getGoodsData(mSearchKey);
			}
		}

		getHotWordsData();
	}

	private void getHotWordsData() {
		SearchLogic.getHotWords(mContext, mHandler);
	}

	private void getGoodsData(String keyword) {
		mCustomProgressDialog.show();
		SearchLogic.queryGoods(mContext, mHandler, keyword, "",
				mCurrentPageNum, MsgRequest.PAGE_SIZE, mNowSortType);
		CacheManager.addSearchHistroy(getApplicationContext(), mSearchKey);
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}

	protected void alertInfo() {
		showAlertDialog("查找信息", " 没有找到相关商品,继续查找！", "继续",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
					}
				}, "取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	protected void showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_tv: {
			mSearchKey = mSearchGoodsEt.getText().toString().trim();
			CacheManager.addSearchHistroy(getApplicationContext(), mSearchKey);
			if ("".equals(mSearchKey)) {
				Toast.makeText(
						mContext,
						mContext.getResources()
								.getString(R.string.search_thing),
						Toast.LENGTH_SHORT).show();
			} else {
				getGoodsData(mSearchKey);
			}
			break;
		}
		case R.id.search_back_iv: {
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
