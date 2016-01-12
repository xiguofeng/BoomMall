package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.NotifyGoodsInfo;
import com.plmt.boommall.network.logic.NoticeLogic;
import com.plmt.boommall.ui.adapter.PriceReduceAdapter;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.iosdialog.AlertDialog;
import com.plmt.boommall.utils.ActivitiyInfoManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class PriceReduceListActivity extends Activity
		implements OnClickListener, ListItemClickHelp {

	private Context mContext;

	private ListView mNotifyGoodsLv;
	private PriceReduceAdapter mAdapter;
	private ArrayList<NotifyGoodsInfo> mNotifyGoodsList = new ArrayList<NotifyGoodsInfo>();

	private ImageView mBackIv;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {

			case NoticeLogic.PRICE_REDUCE_GET_SUC: {
				if (null != msg.obj) {
					mNotifyGoodsList.clear();
					mNotifyGoodsList.addAll((Collection<? extends NotifyGoodsInfo>) msg.obj);
					mAdapter.notifyDataSetChanged();
				}
				break;
			}
			case NoticeLogic.PRICE_REDUCE_GET_FAIL: {
				break;
			}
			case NoticeLogic.PRICE_REDUCE_GET_EXCEPTION: {
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

	Handler mCloseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case NoticeLogic.PRICE_REDUCE_CLOSE_SUC: {
				mProgressDialog.show();
				refreshGoods();
				Toast.makeText(mContext, "删除降价通知成功！", Toast.LENGTH_SHORT).show();
				break;
			}
			case NoticeLogic.PRICE_REDUCE_CLOSE_FAIL: {
				if (null != msg.obj) {
					Toast.makeText(mContext, "删除降价通知失败：" + (String) msg.obj, Toast.LENGTH_SHORT).show();
				}
				if (null != mProgressDialog && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			}
			case NoticeLogic.PRICE_REDUCE_CLOSE_EXCEPTION: {
				if (null != mProgressDialog && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			}

			default: {
				if (null != mProgressDialog && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_reduce_list);
		mContext = PriceReduceListActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
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
		mBackIv = (ImageView) findViewById(R.id.price_reduce_list_back_iv);
		mBackIv.setOnClickListener(this);

		initListView();
	}

	private void initData() {
		refreshGoods();
	}

	private void initListView() {
		mNotifyGoodsLv = (ListView) findViewById(R.id.price_reduce_list_goods_lv);

		mAdapter = new PriceReduceAdapter(mContext, mNotifyGoodsList, this);
		mNotifyGoodsLv.setAdapter(mAdapter);

		mNotifyGoodsLv.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Log.i(TAG, "滚动状态变化");
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// Log.i(TAG, "正在滚动");
			}
		});
		mNotifyGoodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(PriceReduceListActivity.this, GoodsDetailActivity.class);
					intent.setAction(GoodsDetailActivity.ORIGIN_FROM_CATE_ACTION);
					Bundle bundle = new Bundle();
					bundle.putSerializable(GoodsDetailActivity.GOODS_ID_KEY, mNotifyGoodsList.get(position).getProduct_id());
					intent.putExtras(bundle);
					startActivity(intent);
			}
		});

	}

	private void refreshGoods() {
		mProgressDialog.show();
		NoticeLogic.getPriceReduce(mContext, mHandler);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.price_reduce_list_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		switch (which) {
		case R.id.list_price_reduce_del_iv: {
			final  int nowPosition = position;
			new AlertDialog(mContext)
			.builder()
			.setTitle(getString(R.string.prompt))
			.setMsg(getString(R.string.del_str))
			.setPositiveButton(getString(R.string.confirm),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							mProgressDialog.show();
							NoticeLogic.closePriceReduce(mContext, mCloseHandler, mNotifyGoodsList.get(nowPosition).getAlert_id());
						}
					})
			.setNegativeButton(getString(R.string.cancal),
					new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			PriceReduceListActivity.this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
