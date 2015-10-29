package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;

public class GoodsAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Goods> mDatas;

	private LayoutInflater mInflater;

	public GoodsAdapter(Context context, ArrayList<Goods> datas) {
		this.mContext = context;
		this.mDatas = datas;
		mInflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_goods_item, null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView
					.findViewById(R.id.goods_name_tv);
			holder.mPrice = (TextView) convertView
					.findViewById(R.id.goods_price_tv);
			holder.mOriginalPrice = (TextView) convertView
					.findViewById(R.id.goods_original_prices_tv);

			holder.mIcon = (ImageView) convertView.findViewById(R.id.goods_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mName.setText(mDatas.get(position).getName().trim());
		String fPrice = mDatas.get(position).getFinalPrice();
		if (fPrice.contains(".")) {
			int index = fPrice.indexOf(".");
			if (fPrice.length() > index + 2) {
				fPrice = fPrice.substring(0, index + 2);
			}
		}
		String yPrice = mDatas.get(position).getPrice();
		if (yPrice.contains(".")) {
			int index = yPrice.indexOf(".");
			if (yPrice.length() > index + 2) {
				yPrice = yPrice.substring(0, index + 2);
			}
		}
		holder.mPrice.setText("￥" + fPrice);
		holder.mOriginalPrice.setText("原价￥" + yPrice);

		ImageLoader.getInstance().displayImage(mDatas.get(position).getImage(),
				holder.mIcon);

		return convertView;
	}

	static class ViewHolder {

		public TextView mName;

		public TextView mPrice;

		public TextView mOriginalPrice;

		public ImageView mIcon;
	}

}
