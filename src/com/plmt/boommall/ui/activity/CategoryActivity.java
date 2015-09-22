package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.ui.adapter.TopCategoryAdapter;

public class CategoryActivity extends Activity implements OnClickListener {

	private Context mContext;
	private LinearLayout mSearchLl;
	private EditText mSearchEt;
	private ImageView mSearchIv;

	private ListView mTopLevelLv;
	private TopCategoryAdapter mTopCategoryAdapter;
	private ArrayList<Category> mTopCategoryList = new ArrayList<Category>();
	
	private GridView mSecondLevelGv;
	private TopCategoryAdapter mSecondCategoryAdapter;
	private ArrayList<Category> mSecondCategoryList = new ArrayList<Category>();


	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {

			case GoodsLogic.GOODS_LIST_BY_KEY_GET_SUC: {
				if (null != msg.obj) {
					mTopCategoryList.clear();
					mTopCategoryList.addAll((Collection<? extends Category>) msg.obj);
					mTopCategoryAdapter.notifyDataSetChanged();
					
				}
				break;
			}
			case GoodsLogic.GOODS_LIST_BY_KEY_GET_FAIL: {
				break;
			}
			case GoodsLogic.GOODS_LIST_BY_KEY_GET_EXCEPTION: {
				break;
			}

			default:
				break;
			}
		
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		mContext = CategoryActivity.this;
		initView();
		initData();
	}

	private void initView() {
		mSearchLl = (LinearLayout) findViewById(R.id.category_search_ll);

		mSearchIv = (ImageView) findViewById(R.id.category_search_iv);
		mSearchIv.setOnClickListener(this);

		mSearchEt = (EditText) findViewById(R.id.category_search_et);
		mSearchEt
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// 此处为得到焦点时的处理内容
							mSearchLl.setVisibility(View.GONE);
							mSearchIv.setVisibility(View.VISIBLE);
						} else {
							// 此处为失去焦点时的处理内容
							mSearchEt.setText("");
							mSearchLl.setVisibility(View.VISIBLE);
							mSearchIv.setVisibility(View.GONE);
						}
					}
				});

		mSearchLl.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

			}
		});
		
		initCategoryView();
	}
	
	private void initCategoryView(){
		mTopLevelLv = (ListView) findViewById(R.id.category_top_lv);
		mSecondLevelGv =  (GridView) findViewById(R.id.category_second_gv);
	}

	private void initData() {
		
	}

	
	@Override
	protected void onResume() {
		super.onResume();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.category_search_iv: {
			if (!TextUtils.isEmpty(mSearchEt.getText().toString().trim())) {
				
			} else {
				Toast.makeText(mContext, getString(R.string.search_hint),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			break;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
