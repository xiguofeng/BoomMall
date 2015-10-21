package com.plmt.boommall.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Order;
import com.plmt.boommall.network.logic.OrderLogic;
import com.plmt.boommall.ui.adapter.OrderAdapter;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.listview.pullrefresh.XListView;

public class MyOrderListActivity extends Activity implements OnClickListener,
		XListView.IXListViewListener, ListItemClickHelp {

	private Context mContext;

	private XListView mListView;

	private OrderAdapter mOrderAdapter;

	private ArrayList<Order> mOrderList = new ArrayList<Order>();

	private HashMap<String, Object> mMsgMap = new HashMap<String, Object>();

	private int mIndex = 0;

	private int mRefreshIndex = 0;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDERLIST_GET_SUC: {
				if (null != msg.obj) {
				}

				break;
			}
			case OrderLogic.ORDERLIST_GET_FAIL: {
				break;
			}
			case OrderLogic.ORDERLIST_GET_EXCEPTION: {
				break;
			}
			case OrderLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}

		}

	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_orders);
		mContext = MyOrderListActivity.this;
		initView();
		initData();
	}

	private void initView() {

		mListView = (XListView) findViewById(R.id.orders_lv);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());

		mOrderList.clear();

		mOrderAdapter = new OrderAdapter(mContext, mMsgMap, this);
		mListView.setAdapter(mOrderAdapter);

	}

	private void initData() {
		OrderLogic.getOrders(mContext, mHandler, "", "", "");
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mIndex = ++mRefreshIndex;
				onLoad();
			}
		}, 2500);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad();
			}
		}, 2500);
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_list_back_iv: {
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
