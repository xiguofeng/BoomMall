package com.plmt.boommall.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Order;
import com.plmt.boommall.network.logic.OrderLogic;
import com.plmt.boommall.ui.adapter.OrderAdapter;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.listview.pullrefresh.XListView;

public class OrdersAllFragment extends Fragment implements
		XListView.IXListViewListener, ListItemClickHelp {

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
					mMsgMap.clear();
					mMsgMap.putAll((Map<? extends String, ? extends Object>) msg.obj);
					Log.e("xxx_size", "" + mMsgMap.size());
					mOrderAdapter.notifyDataSetChanged();
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.orders_fragment, null);
		initView(view);
		return view;
	}

	private void initView(View view) {

		mListView = (XListView) view.findViewById(R.id.orders_lv);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());

		mOrderList.clear();

		mOrderAdapter = new OrderAdapter(getActivity(), mMsgMap, this);
		mListView.setAdapter(mOrderAdapter);

		OrderLogic.getOrders(getActivity(), mHandler, "", "", "");
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
	public void onClick(View item, View widget, int position, int which) {

	}

}
