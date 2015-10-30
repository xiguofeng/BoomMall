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

public class ShoppingCartActivity extends Activity implements ischeck{

	private ExpandableListView list;
	private ShoppingCartAdapter adapter;
	private List<GroupBean> grou;
	private Map<String, List<GroupBean>> chile;
	private GroupBean bean;
	private CheckBox cb;
	private LinearLayout empty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_cart_expandablelistview);
		this.list = (ExpandableListView)findViewById(R.id.lv);
		empty = (LinearLayout) findViewById(R.id.empty);
		cb = (CheckBox) findViewById(R.id.cb);
		grou = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("一个商家");
		grou.add(bean);
		bean = new GroupBean();
		bean.setTxt("另一个供货商");
		grou.add(bean);
		bean = new GroupBean();
		bean.setTxt("最后一个");
		grou.add(bean);
		bean = new GroupBean();
		bean.setTxt("另一个供货商123");
		grou.add(bean);
		bean = new GroupBean();
		bean.setTxt("另一个供货商098");
		grou.add(bean);
		
		this.chile = new HashMap<String,List<GroupBean>>();
		List<GroupBean> chiless = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("好商品");
		chiless.add(bean);
		bean.setTxt("又好商品");
		chiless.add(bean);
		
		chile.put(grou.get(0).getTxt(), chiless);
		chiless = new ArrayList<GroupBean>();
		bean = new GroupBean();
		bean.setTxt("就一个");
		chiless.add(bean);
		
		chile.put(grou.get(1).getTxt(), chiless);
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
		
		chile.put(grou.get(2).getTxt(), chiless);
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
		
		
		
		chile.put(grou.get(3).getTxt(), chiless);
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
		
		chile.put(grou.get(4).getTxt(), chiless);
		adapter = new ShoppingCartAdapter(this, grou, chile);
		adapter.setischek(this);
		
		
		list.setAdapter(adapter);
		int size = adapter.getGroupCount();
		for(int i = 0;i<size;i++){
			list.expandGroup(i);
		}
		cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int size = grou.size();
				for(int i = 0;i<size;i++){
					grou.get(i).setIscheck(cb.isChecked());
					ischekgroup(i, cb.isChecked());
				}
				adapter.notifyDataSetChanged();
			}
		});
		list.setEmptyView(empty);
	}
	@Override
	public void ischekgroup(int groupposition, boolean ischeck) {
		// TODO Auto-generated method stub
		List<GroupBean> chiless = chile.get(grou.get(groupposition).getTxt());
		for(GroupBean bean:chiless){
			bean.setIscheck(ischeck);
		}
		grou.get(groupposition).setIscheck(ischeck);
		adapter.notifyDataSetChanged();
	}
	


}
