package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Banner;

public class MainGvCategoryAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Banner> mDatas;

	public MainGvCategoryAdapter(Context context, ArrayList<Banner> data) {

		this.mContext = context;
		this.mDatas = data;
	}

	@Override
	public int getCount() {
		return mDatas.size();
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
			currentView = LayoutInflater.from(mContext).inflate(
					R.layout.main_gv_category_item, null);
			holderView.iconIv = (ImageView) currentView
					.findViewById(R.id.iv_adapter_grid_pic);
			
			holderView.nameTv = (TextView) currentView
					.findViewById(R.id.main_gv_category_name_tv);
			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}
		if (!TextUtils.isEmpty(mDatas.get(position).getImgurl())) {
			ImageLoader.getInstance().displayImage(
					mDatas.get(position).getImgurl(), holderView.iconIv);
		}
		
		holderView.nameTv.setText(mDatas.get(position).getTitle());

		return currentView;
	}

	public class HolderView {

		private ImageView iconIv;
		
		private TextView nameTv;

	}

}
