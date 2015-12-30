package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.Order;
import com.plmt.boommall.ui.adapter.CommentGoodsAdapter;
import com.plmt.boommall.ui.utils.ListItemClickHelpWithID;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.utils.ActivitiyInfoManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class CommentGoodsListActivity extends Activity implements OnClickListener, ListItemClickHelpWithID {
	
	public static final String ORDER_KEY = "orderKey";
	
	private Context mContext;

	private ListView mGoodsLv;
	private CommentGoodsAdapter mGoodsAdapter;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private ImageView mBackIv;
	private ImageView mBackTopIv;
	
	private Order mOrder;

	private CustomProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_goods_list);
		mContext = CommentGoodsListActivity.this;
		if (!ActivitiyInfoManager.activitityMap.containsKey(ActivitiyInfoManager.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap.put(ActivitiyInfoManager.getCurrentActivityName(mContext), this);
		}
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		mBackIv = (ImageView) findViewById(R.id.comment_goods_list_back_iv);
		mBackIv.setOnClickListener(this);


		mGoodsLv = (ListView) findViewById(R.id.comment_goods_list_goods_lv);
		mGoodsAdapter = new CommentGoodsAdapter(mContext, mGoodsList, this);
		mGoodsLv.setAdapter(mGoodsAdapter);
		mGoodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {
				}

			}
		});
	}

	private void initData() {
		mOrder = (Order) getIntent().getSerializableExtra(
				CommentGoodsListActivity.ORDER_KEY);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_goods_list_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onClick(View item, View widget, int position, int which, String id) {
		switch (which) {
		case R.id.comment_goods_comment_btn: {
			Intent intent = new Intent(CommentGoodsListActivity.this,CommentAddActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(CommentAddActivity.GOODS_KEY,
					mGoodsList.get(which));
			intent.putExtras(bundle);
		    startActivity(intent);
		    
			break;
		}

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			CommentGoodsListActivity.this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
