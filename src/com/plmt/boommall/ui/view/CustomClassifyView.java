package com.plmt.boommall.ui.view;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.HomeRecommend;
import com.plmt.boommall.entity.RootName;
import com.plmt.boommall.ui.activity.CategoryActivity;
import com.plmt.boommall.ui.activity.GoodsDetailActivity;
import com.plmt.boommall.ui.activity.HomeActivity;
import com.plmt.boommall.ui.adapter.MainGoodsAdapter;
import com.plmt.boommall.ui.adapter.MainGoodsGvAdapter;
import com.plmt.boommall.ui.view.gridview.CustomGridView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomClassifyView extends LinearLayout {

	private ImageView mIv;

	private RelativeLayout mCustomClassifyRl;

	private TextView mTitleNameTv;
	private TextView mFirstNameTv;
	private TextView mSecondNameTv;
	private TextView mNumTv;

	private HorizontalListView mGoodsLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mGoodsAdapter;

	private CustomGridView mGoodsGv;
	private MainGoodsGvAdapter mGoodsGvAdapter;

	public CustomClassifyView(Context context, HomeRecommend classifyGoods) {
		super(context);
		initView(context, classifyGoods);
		// fillData(context, classifyGoods);
	}

	private void initView(final Context context,
			final HomeRecommend homeRecommend) {

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.custom_classify, null);

		mCustomClassifyRl = (RelativeLayout) layout
				.findViewById(R.id.custom_classify_rl);
		mCustomClassifyRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CategoryActivity.setDataFromMain(homeRecommend.getName());
				HomeActivity.setTab(HomeActivity.TAB_CATEGORY);
			}
		});

		mIv = (ImageView) layout.findViewById(R.id.custom_classify_iv);
		mTitleNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_title_tv);
		mFirstNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_first_name_tv);
		mSecondNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_second_name_tv);

		mGoodsGv = (CustomGridView) layout
				.findViewById(R.id.custom_classify_goods_gv);

		ArrayList<Goods> goodsList = new ArrayList<>();
		goodsList.addAll(homeRecommend.getGoodsList());
		mGoodsList.addAll(homeRecommend.getGoodsList());

		mGoodsGvAdapter = new MainGoodsGvAdapter(context, mGoodsList);
		mGoodsGv.setAdapter(mGoodsGvAdapter);
		mGoodsGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, GoodsDetailActivity.class);
				intent.setAction(GoodsDetailActivity.ORIGIN_FROM_MAIN_ACTION);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GoodsDetailActivity.GOODS_ID_KEY,
						mGoodsList.get(position).getId());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		ArrayList<RootName> rootNameList = new ArrayList<>();
		rootNameList.addAll(homeRecommend.getRootNameList());
		if (rootNameList.size() >= 2) {
			mFirstNameTv.setText(rootNameList.get(0).getSubname());
			mSecondNameTv.setText(rootNameList.get(1).getSubname());
		}

		mTitleNameTv.setText(homeRecommend.getName());
		ImageLoader.getInstance().displayImage(homeRecommend.getImage(), mIv);

		this.addView(layout);
	}

	public void fillData(Context context, HomeRecommend classifyGoods) {

	}

}
