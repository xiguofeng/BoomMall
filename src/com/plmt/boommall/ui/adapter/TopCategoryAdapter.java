package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Category;

public class TopCategoryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Category> data;

	public TopCategoryAdapter(Context context, ArrayList<Category> data) {

		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View currentView, ViewGroup arg2) {
		HolderView holderView = null;
		if (currentView == null) {
			holderView = new HolderView();
			currentView = LayoutInflater.from(context).inflate(
					R.layout.main_gv_category_item, null);
			holderView.iconIv = (ImageView) currentView
					.findViewById(R.id.iv_adapter_grid_pic);
			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}

		holderView.iconIv.setImageResource(data.get(position).getLocalImage());

		return currentView;
	}

	public class HolderView {

		private ImageView iconIv;

	}

}
