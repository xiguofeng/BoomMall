package com.plmt.boommall.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;

public class OrderGoodsView extends LinearLayout {

	private ImageView mIv;

	private TextView mNameTv;
	private TextView mNumTv;
	private TextView mPriceTv;

	public OrderGoodsView(Context context, Goods goods) {
		super(context);
		initView(context, goods);
		fillData(context, goods);
	}

	private void initView(final Context context, Goods goods) {

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.order_goods, null);
		mIv = (ImageView) layout.findViewById(R.id.order_goods_iv);
		mNameTv = (TextView) layout.findViewById(R.id.order_goods_name_tv);
		mPriceTv = (TextView) layout.findViewById(R.id.order_goods_price_tv);
		mNumTv = (TextView) layout.findViewById(R.id.order_goods_num_tv);

		this.addView(layout);
	}

	public void fillData(Context context, Goods goods) {

		ImageLoader.getInstance().displayImage(goods.getImage(), mIv);
		mNameTv.setText(goods.getId());
		// mPriceTv.setText("￥" + goods.getFinalPrice());
		// mNumTv.setText(goods.getNum() + "瓶");
	}

}
