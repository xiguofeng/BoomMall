package com.plmt.boommall.ui.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.ui.adapter.MainGoodsAdapter;

public class CustomClassifyView extends LinearLayout {

	private ImageView mIv;

	private TextView mFirstNameTv;
	private TextView mSecondNameTv;
	private TextView mNumTv;

	private HorizontalListView mGoodsLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mGoodsAdapter;

	public CustomClassifyView(Context context,
			HashMap<String, Object> classifyGoods) {
		super(context);
		initView(context, classifyGoods);
		// fillData(context, classifyGoods);
	}

	private void initView(final Context context,
			HashMap<String, Object> classifyGoods) {

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.custom_classify, null);

		mIv = (ImageView) layout.findViewById(R.id.custom_classify_iv);
		mFirstNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_first_name_tv);
		mSecondNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_second_name_tv);

		mGoodsLv = (HorizontalListView) layout
				.findViewById(R.id.custom_classify_goods_lv);

		for (int i = 0; i < 6; i++) {
			Goods goods = new Goods();
			goods.setName("商品" + i);
			goods.setImage("http://img3.douban.com/view/commodity_story/medium/public/p19671.jpg");
			mGoodsList.add(goods);
		}

		mGoodsAdapter = new MainGoodsAdapter(context, mGoodsList);
		mGoodsLv.setAdapter(mGoodsAdapter);
		mGoodsAdapter.notifyDataSetChanged();

		mFirstNameTv.setText("分类名称");
		mSecondNameTv.setText("分类别名");

		this.addView(layout);
	}

	public void fillData(Context context, HashMap<String, Object> classifyGoods) {
		// Category category = (Category) classifyGoods.get("category");
		// mGoodsList.addAll((ArrayList<Goods>) classifyGoods.get(category
		// .getPpid()));

	}

}
