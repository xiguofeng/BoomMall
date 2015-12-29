package com.plmt.boommall.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.OrderOld;
import com.plmt.boommall.ui.adapter.OrderAdapter;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.listview.pullrefresh.XListView;

public class OrdersUnPayFragment extends Fragment implements
		XListView.IXListViewListener, ListItemClickHelp {

	private XListView mListView;

	private OrderAdapter mNewsAdapter;

	private ArrayList<OrderOld> mNewsList = new ArrayList<OrderOld>();

	private HashMap<String, Object> mMsgMap = new HashMap<String, Object>();

	private Handler mHandler;

	private int mIndex = 0;

	private int mRefreshIndex = 0;

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

		mHandler = new Handler();

		mListView = (XListView) view.findViewById(R.id.orders_lv);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());

		mNewsList.clear();

		mNewsAdapter = new OrderAdapter(getActivity(), mMsgMap, this);
		mListView.setAdapter(mNewsAdapter);
	}

	// @Override
	// public void onWindowFocusChanged(boolean hasFocus) {
	// super.onWindowFocusChanged(hasFocus);
	//
	// if (hasFocus) {
	// mListView.autoRefresh();
	// }
	// }

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
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollDown() {
		// TODO Auto-generated method stub
		
	}

}
