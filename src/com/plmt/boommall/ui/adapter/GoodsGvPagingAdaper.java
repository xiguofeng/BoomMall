package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.ui.view.gridview.paging.PagingBaseAdapter;

public class GoodsGvPagingAdaper extends PagingBaseAdapter<Goods> {

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Goods getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		HolderView holderView = null;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.gv_goods_item, null);
			holderView.iconIv = (ImageView) convertView
					.findViewById(R.id.gv_goods_common_iv);
			holderView.nameTv = (TextView) convertView
					.findViewById(R.id.gv_goods_common_name_tv);
			holderView.priceTv = (TextView) convertView
					.findViewById(R.id.gv_goods_common_price_tv);

			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		Goods goods = getItem(position);
		holderView.nameTv.setText(goods.getName().trim());
		String fPrice = goods.getFinalPrice();
		if (fPrice.contains(".")) {
			int index = fPrice.indexOf(".");
			if (fPrice.length() > index + 2) {
				fPrice = fPrice.substring(0, index + 2);
			}
		}
		String yPrice = goods.getPrice();
		if (yPrice.contains(".")) {
			int index = yPrice.indexOf(".");
			if (yPrice.length() > index + 2) {
				yPrice = yPrice.substring(0, index + 2);
			}
		}
		holderView.priceTv.setText("￥" + fPrice);
		ImageLoader.getInstance().displayImage(goods.getImage(),
				holderView.iconIv);
		return convertView;
	}

	public class HolderView {

		private ImageView iconIv;

		private TextView nameTv;

		private TextView priceTv;

	}
}
