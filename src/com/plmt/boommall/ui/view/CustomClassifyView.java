package com.plmt.boommall.ui.view;

import java.util.ArrayList;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.HomeRecommend;
import com.plmt.boommall.entity.RootName;
import com.plmt.boommall.ui.activity.GoodsDetailActivity;
import com.plmt.boommall.ui.adapter.MainGoodsAdapter;
import com.plmt.boommall.ui.adapter.MainGoodsGvAdapter;
import com.plmt.boommall.ui.view.gridview.CustomGridView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomClassifyView extends LinearLayout {

	private ImageView mIv;

	private TextView mTitleNameTv;
	private TextView mFirstNameTv;
	private TextView mSecondNameTv;
	private TextView mNumTv;

	private HorizontalListView mGoodsLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private MainGoodsAdapter mGoodsAdapter;

	private CustomGridView mGoodsGv;
	private MainGoodsGvAdapter mGoodsGvAdapter;

	public CustomClassifyView(Context context,
			HomeRecommend classifyGoods) {
		super(context);
		initView(context, classifyGoods);
		// fillData(context, classifyGoods);
	}

	private void initView(final Context context,
			HomeRecommend homeRecommend) {

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.custom_classify, null);

		mIv = (ImageView) layout.findViewById(R.id.custom_classify_iv);
		mTitleNameTv= (TextView) layout
				.findViewById(R.id.custom_classify_title_tv);
		mFirstNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_first_name_tv);
		mSecondNameTv = (TextView) layout
				.findViewById(R.id.custom_classify_second_name_tv);
		
		

		mGoodsGv = (CustomGridView) layout
				.findViewById(R.id.custom_classify_goods_gv);
		
		ArrayList<Goods> goodsList=new ArrayList<>();
		goodsList.addAll(homeRecommend.getGoodsList());
		mGoodsList.addAll(homeRecommend.getGoodsList());
//		for (int i = 0; i < goodsList.size(); i++) {
//			
//			Goods goods = new Goods();
//			goods.setName("商品" + i);
//			goods.setImage("http://image.rayliimg.cn/2014/0803/2014832242370.jpg");
//			mGoodsList.add(goods);
//		}
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
		ArrayList<RootName> rootNameList=new ArrayList<>();
		rootNameList.addAll(homeRecommend.getRootNameList());
		if(rootNameList.size()>=2){
			mFirstNameTv.setText(rootNameList.get(0).getSubname());
			mSecondNameTv.setText(rootNameList.get(1).getSubname());
		}
		
		mTitleNameTv.setText(homeRecommend.getRootCategoryName());
		
		this.addView(layout);
	}

	public void fillData(Context context, HomeRecommend classifyGoods) {

	}

}
