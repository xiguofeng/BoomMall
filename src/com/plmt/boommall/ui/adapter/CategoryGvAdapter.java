package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.MenuItem;

public class CategoryGvAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MenuItem> data;

	public CategoryGvAdapter(Context context, ArrayList<MenuItem> data) {

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
					R.layout.user_gv_common_item, null);
			holderView.iconIv = (ImageView) currentView
					.findViewById(R.id.user_gv_common_iv);
			holderView.nameTv = (TextView) currentView
					.findViewById(R.id.user_gv_common_name_tv);

			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}

		holderView.iconIv.setImageResource(data.get(position).getLocalImage());
		holderView.nameTv.setText(data.get(position).getName());

		return currentView;
	}

	public class HolderView {

		private ImageView iconIv;

		private TextView nameTv;

	}

}
