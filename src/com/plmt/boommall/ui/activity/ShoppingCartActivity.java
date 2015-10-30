package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.GroupBean;
import com.plmt.boommall.ui.adapter.ShoppingCartAdapter;
import com.plmt.boommall.ui.adapter.ShoppingCartAdapter.ischeck;

public class ShoppingCartActivity extends Activity implements ischeck {

	private ExpandableListView list;
	private ShoppingCartAdapter adapter;
	private List<GroupBean> mGroup;
	private Map<String, List<GroupBean>> mChild;
	private GroupBean bean;
	private CheckBox cb;
	private LinearLayout empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_cart_expandablelistview);
		this.list = (ExpandableListView) findViewById(R.id.lv);
		empty = (LinearLayout) findViewById(R.id.empty);
		cb = (CheckBox) findViewById(R.id.cb);
		mGroup = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("一个商家");
		mGroup.add(bean);
		bean = new GroupBean();
		bean.setTxt("另一个供货商");
		mGroup.add(bean);
		bean = new GroupBean();
		bean.setTxt("最后一个");
		mGroup.add(bean);
		bean = new GroupBean();
		bean.setTxt("另一个供货商123");
		mGroup.add(bean);
		bean = new GroupBean();
		bean.setTxt("另一个供货商098");
		mGroup.add(bean);

		this.mChild = new HashMap<String, List<GroupBean>>();
		List<GroupBean> chiless = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("好商品");
		chiless.add(bean);
		bean.setTxt("又好商品");
		chiless.add(bean);

		mChild.put(mGroup.get(0).getTxt(), chiless);
		chiless = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("就一个");
		chiless.add(bean);

		mChild.put(mGroup.get(1).getTxt(), chiless);
		chiless = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("333好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("1111又好商品");
		chiless.add(bean);

		bean = new GroupBean();
		bean.setTxt("1115又好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("11177又好商品");
		chiless.add(bean);

		mChild.put(mGroup.get(2).getTxt(), chiless);
		chiless = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("333好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("1111好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("1115好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("11177好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("111777好商品");
		chiless.add(bean);

		mChild.put(mGroup.get(3).getTxt(), chiless);
		chiless = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("333好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("1111好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("1115好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("11177好商品");
		chiless.add(bean);
		bean = new GroupBean();
		bean.setTxt("111777好商品");
		chiless.add(bean);

		mChild.put(mGroup.get(4).getTxt(), chiless);
		adapter = new ShoppingCartAdapter(this, mGroup, mChild);
		adapter.setischek(this);

		list.setAdapter(adapter);
		int size = adapter.getGroupCount();
		for (int i = 0; i < size; i++) {
			list.expandGroup(i);
		}
		cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int size = mGroup.size();
				for (int i = 0; i < size; i++) {
					mGroup.get(i).setIscheck(cb.isChecked());
					ischekgroup(i, cb.isChecked());
				}
				adapter.notifyDataSetChanged();
			}
		});
		list.setEmptyView(empty);
	}

	@Override
	public void ischekgroup(int groupposition, boolean ischeck) {
		List<GroupBean> child = mChild
				.get(mGroup.get(groupposition).getTxt());
		for (GroupBean bean : child) {
			bean.setIscheck(ischeck);
		}
		mGroup.get(groupposition).setIscheck(ischeck);
		adapter.notifyDataSetChanged();
	}

}
