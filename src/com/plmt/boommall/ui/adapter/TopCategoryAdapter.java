package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Category;

public class TopCategoryAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Category> mDatas;
	private LayoutInflater mInflater;

	private String mCurrentSelect;

	public TopCategoryAdapter(Context context, ArrayList<Category> data) {

		this.mContext = context;
		this.mDatas = data;
		mInflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.list_category_category_item, null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView
					.findViewById(R.id.category_item_name_tv);
			holder.mBg = (LinearLayout) convertView
					.findViewById(R.id.category_item_ll);
			holder.mSelectIv = (ImageView) convertView
					.findViewById(R.id.category_item_select_iv);
			holder.mIconIv = (ImageView) convertView
					.findViewById(R.id.category_item_icon_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mSelectIv.setVisibility(View.INVISIBLE);
		holder.mIconIv.setVisibility(View.GONE);

		holder.mBg.setBackgroundColor(mContext.getResources().getColor(
				R.color.white));
		holder.mName.setTextColor(mContext.getResources().getColor(
				R.color.black_character));
		if (mCurrentSelect.equals(mDatas.get(position).getId())) {
			holder.mSelectIv.setVisibility(View.VISIBLE);
			holder.mBg.setBackgroundColor(mContext.getResources().getColor(
					R.color.gray_select_bg));
			holder.mName.setTextColor(mContext.getResources().getColor(
					R.color.black_character));
		}
		holder.mName.setText(mDatas.get(position).getName());
		return convertView;
	}

	static class ViewHolder {

		public LinearLayout mBg;

		public TextView mName;

		public ImageView mSelectIv;

		public ImageView mIconIv;
	}

	public String getmCurrentSelect() {
		return mCurrentSelect;
	}

	public void setmCurrentSelect(String mCurrentSelect) {
		this.mCurrentSelect = mCurrentSelect;
	}

}
