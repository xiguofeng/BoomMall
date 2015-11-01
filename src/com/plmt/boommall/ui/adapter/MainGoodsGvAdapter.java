package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;

public class MainGoodsGvAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Goods> mDatas;

	public MainGoodsGvAdapter(Context context, ArrayList<Goods> data) {

		this.context = context;
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
			currentView = LayoutInflater.from(context).inflate(R.layout.gv_main_goods_item, null);
			holderView.mIconIv = (ImageView) currentView.findViewById(R.id.gv_main_goods_common_iv);
			holderView.mNameTv = (TextView) currentView.findViewById(R.id.gv_main_goods_common_name_tv);
			holderView.mPriceTv = (TextView) currentView.findViewById(R.id.gv_main_goods_common_price_tv);

			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}

		// holderView.iconIv.setImageResource(data.get(position).getLocalImage());
		holderView.mNameTv.setText(mDatas.get(position).getName());
		holderView.mPriceTv.setText("¥"+mDatas.get(position).getFinalPrice());
		ImageLoader.getInstance().displayImage(mDatas.get(position).getImage(), holderView.mIconIv);
		return currentView;
	}

	public class HolderView {

		private ImageView mIconIv;

		private TextView mNameTv;

		private TextView mPriceTv;

	}

}
