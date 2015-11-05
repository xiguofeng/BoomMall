package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.network.logic.GoodsLogic;
import com.plmt.boommall.ui.adapter.CategoryGvAdapter;
import com.plmt.boommall.ui.adapter.TopCategoryAdapter;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.gridview.GridViewWithHeaderAndFooter;
import com.plmt.boommall.utils.ActivitiyInfoManager;

public class CategoryActivity extends Activity implements OnClickListener {

	private Context mContext;
	private LinearLayout mSearchLl;
	private EditText mSearchEt;
	private ImageView mSearchIv;

	private View mCategoryHeadView;
	private ImageView mCategoryHeadIv;

	private ListView mTopLevelLv;
	private TopCategoryAdapter mTopCategoryAdapter;
	private ArrayList<Category> mTopCategoryList = new ArrayList<Category>();

	private GridViewWithHeaderAndFooter mSecondLevelGv;
	private CategoryGvAdapter mSecondCategoryAdapter;
	private ArrayList<Category> mSecondCategoryList = new ArrayList<Category>();

	public static String sCategoryName;
	public static boolean isNeedUpdate = false;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {

			case GoodsLogic.CATEGROY_TOP_LIST_GET_SUC: {
				if (null != msg.obj) {
					mTopCategoryList.clear();
					mTopCategoryList
							.addAll((Collection<? extends Category>) msg.obj);
					mTopCategoryAdapter.notifyDataSetChanged();

					if (mTopCategoryList.size() > 0) {
						if (isNeedUpdate) {
							setShowFromMain();
						} else {
							GoodsLogic.getSubCategory(mContext, mHandler,
									mTopCategoryList.get(0).getName());
						}
					}
				}
				break;
			}
			case GoodsLogic.CATEGROY_TOP_LIST_GET_FAIL: {
				break;
			}
			case GoodsLogic.CATEGROY_TOP_LIST_GET_EXCEPTION: {
				break;
			}

			case GoodsLogic.CATEGROY_SUB_LIST_GET_SUC: {
				if (null != msg.obj) {
					mSecondCategoryList.clear();
					mSecondCategoryList
							.addAll((Collection<? extends Category>) msg.obj);
					mSecondCategoryAdapter.notifyDataSetChanged();

					ImageLoader.getInstance().displayImage(
							mSecondCategoryList.get(0).getParentImageurl(),
							mCategoryHeadIv);

				}
				break;
			}
			case GoodsLogic.CATEGROY_SUB_LIST_GET_FAIL: {
				break;
			}
			case GoodsLogic.CATEGROY_SUB_LIST_GET_EXCEPTION: {
				break;
			}
			case GoodsLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}

			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		mContext = CategoryActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		mProgressDialog.show();

		if (!ActivitiyInfoManager.activitityMap
				.containsKey(ActivitiyInfoManager
						.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap
					.put(ActivitiyInfoManager.getCurrentActivityName(mContext),
							this);
		}

		initView();
		initData();
	}

	private void initView() {
		mSearchLl = (LinearLayout) findViewById(R.id.category_search_ll);
		mSearchLl.setOnClickListener(this);

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

		initCategoryView();
	}

	private void initCategoryView() {
		mTopLevelLv = (ListView) findViewById(R.id.category_top_lv);
		mTopCategoryAdapter = new TopCategoryAdapter(mContext, mTopCategoryList);
		mTopLevelLv.setAdapter(mTopCategoryAdapter);
		mTopCategoryAdapter.setmCurrentSelect("");
		mTopLevelLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mTopCategoryAdapter.setmCurrentSelect(mTopCategoryList.get(
						position).getName());
				mTopCategoryAdapter.notifyDataSetChanged();
				mProgressDialog.show();
				GoodsLogic.getSubCategory(mContext, mHandler, mTopCategoryList
						.get(position).getName());
			}
		});

		mSecondLevelGv = (GridViewWithHeaderAndFooter) findViewById(R.id.category_second_gv);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		mCategoryHeadView = layoutInflater.inflate(R.layout.view_gv_header,
				null);
		mCategoryHeadIv = (ImageView) mCategoryHeadView
				.findViewById(R.id.view_gv_header_iv);
		mSecondLevelGv.addHeaderView(mCategoryHeadView);
		mSecondCategoryAdapter = new CategoryGvAdapter(mContext,
				mSecondCategoryList);
		mSecondLevelGv.setAdapter(mSecondCategoryAdapter);
		mSecondLevelGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(CategoryActivity.this,
						GoodsListActivity.class);
				intent.putExtra("categoryName",
						mSecondCategoryList.get(position).getName());
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});

	}

	private void initData() {
		GoodsLogic.getTopCategory(mContext, mHandler);
	}

	@Override
	protected void onResume() {
		super.onResume();
		HomeActivity.setCartMenuShow(false, "0");
		if (mTopCategoryList.size() > 0) {
			setShowFromMain();
		}
	}

	public static void setDataFromMain(String categoryName) {
		isNeedUpdate = true;
		sCategoryName = categoryName;
	}

	public void setShowFromMain() {
		if (isNeedUpdate) {
			isNeedUpdate = false;
			boolean isHas = false;
			if (!TextUtils.isEmpty(sCategoryName)) {
				for (int i = 0; i < mTopCategoryList.size(); i++) {
					if (mTopCategoryList.get(i).getName().equals(sCategoryName)) {
						isHas = true;
					}
				}
			}
			if (isHas) {
				mTopCategoryAdapter.setmCurrentSelect(sCategoryName);
				mTopCategoryAdapter.notifyDataSetChanged();
				mProgressDialog.show();
				GoodsLogic.getSubCategory(mContext, mHandler, sCategoryName);
			}
		}
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
		case R.id.category_search_ll: {
			Intent intent = new Intent(CategoryActivity.this,
					SearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
			HomeActivity.showMainByOnkey();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
