package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.logic.CartLogic;
import com.plmt.boommall.ui.adapter.CartGoodsAdapter;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.utils.CartManager;
import com.plmt.boommall.utils.UserInfoManager;

public class ShopCartActivity extends Activity implements OnClickListener,
		ListItemClickHelp {

	private Context mContext;

	public final static String EDITOR_MODE = "0";

	public final static String COMPLETE_MODE = "1";

	private String mNowMode = COMPLETE_MODE;

	private MultiStateView mMultiStateView;

	private TextView mTotalNumTv;

	public static LinearLayout mCartNullLl;
	public static LinearLayout mCartNotLoginLl;
	public static TextView mCartNullTv;
	public static Button mCartLoginBtn;

	private TextView mEditorTv;

	public static ArrayList<Goods> sGoodsList = new ArrayList<Goods>();

	private ListView mGoodsLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private static CartGoodsAdapter mGoodsAdapter;

	private ArrayList<Goods> mGoodsDelList = new ArrayList<Goods>();
	private ArrayList<Goods> mGoodsUpdateList = new ArrayList<Goods>();

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CartLogic.CART_LIST_GET_SUC: {
				if (null != msg.obj) {
					mGoodsList.clear();
					mGoodsList.addAll((Collection<? extends Goods>) msg.obj);
					refresh();
				}

				break;

			}
			case CartLogic.CART_LIST_GET_FAIL: {
				break;
			}
			case CartLogic.CART_LIST_GET_EXCEPTION: {
				break;
			}
			case CartLogic.CART_UPDATE_SUC: {
				getCartList();
				break;

			}
			case CartLogic.CART_UPDATE_FAIL: {
				break;
			}
			case CartLogic.CART_UPDATE_EXCEPTION: {
				break;
			}
			case CartLogic.CART_DEL_SUC: {
				getCartList();
				break;

			}
			case CartLogic.CART_DEL_FAIL: {
				break;
			}
			case CartLogic.CART_DEL_EXCEPTION: {
				break;
			}
			case CartLogic.CART_ADD_SUC: {
				if (null != msg.obj) {

				}

				break;
			}
			case CartLogic.CART_ADD_FAIL: {
				Toast.makeText(mContext, R.string.login_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case CartLogic.CART_ADD_EXCEPTION: {
				break;
			}
			case CartLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			// mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_cart);
		mContext = ShopCartActivity.this;
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mTotalNumTv = (TextView) findViewById(R.id.shop_cart_total_num_tv);
		mCartNullTv = (TextView) findViewById(R.id.shop_cart_null_tv);
		mEditorTv = (TextView) findViewById(R.id.shop_cart_modify_tv);
		mEditorTv.setOnClickListener(this);

		mCartNullLl = (LinearLayout) findViewById(R.id.shop_cart_null_and_not_login_ll);
		mCartNotLoginLl = (LinearLayout) findViewById(R.id.shop_cart_not_login_ll);
		mCartLoginBtn = (Button) findViewById(R.id.shop_cart_login_btn);
		mCartLoginBtn.setOnClickListener(this);

		initMultiStateView();
		initListView();
	}

	private void initMultiStateView() {
		mMultiStateView = (MultiStateView) findViewById(R.id.shop_cart_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR)
				.findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView
								.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(),
								"Fetching Data", Toast.LENGTH_SHORT).show();
					}
				});
		// mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

	}

	private void initListView() {
		mGoodsLv = (ListView) findViewById(R.id.shop_cart_order_lv);
		mGoodsAdapter = new CartGoodsAdapter(mContext, mGoodsList, this);
		mGoodsLv.setAdapter(mGoodsAdapter);
	}

	private void initData() {
		mGoodsLv.setVisibility(View.GONE);
		mGoodsAdapter.setmNowMode(COMPLETE_MODE);
		mGoodsAdapter.notifyDataSetChanged();
		getCartList();
		HomeActivity.setCartMenuShow(true,"0");
	}

	public static void refreshView(boolean isChecked, boolean isCancelAll) {
		if (null != mGoodsAdapter) {
			if (isChecked) {
				mGoodsAdapter.initChecked();
			} else {
				if (isCancelAll) {
					mGoodsAdapter.initCheck();
				}
			}
			mGoodsAdapter.notifyDataSetChanged();
		}
	}

	private void refresh() {
		mGoodsLv.setVisibility(View.VISIBLE);
		if (null != mProgressDialog && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mCartNotLoginLl.setVisibility(View.GONE);

		if (mGoodsList.size() > 0) {
			mCartNullLl.setVisibility(View.GONE);
			mCartNullTv.setVisibility(View.GONE);
		}

		mTotalNumTv.setText("(" + String.valueOf(mGoodsList.size()) + ")");

		CartManager.getsSelectCartList().clear();
		CartManager.getsSelectCartList().addAll(CartManager.getsCartList());

		if (CartManager.getsSelectCartList().size() > 0) {
			// HomeActivity.mCheckAllIb.setChecked(true);
		} else {
			// HomeActivity.mCheckAllIb.setChecked(false);
		}

		mGoodsAdapter.notifyDataSetChanged();
		// CartManager.setCartTotalMoney();
	}

	private void getCartList() {
		if (UserInfoManager.getLoginIn(mContext)) {
			mCartNullLl.setVisibility(View.VISIBLE);
			mCartNullTv.setVisibility(View.VISIBLE);
			mCartNotLoginLl.setVisibility(View.GONE);
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			CartLogic.getList(mContext, mHandler);
		} else {
			mCartNullLl.setVisibility(View.VISIBLE);
			mCartNullTv.setVisibility(View.VISIBLE);
			mCartNotLoginLl.setVisibility(View.VISIBLE);
			mGoodsLv.setVisibility(View.GONE);
		}
	}

	private void setCartMode() {
		if (mNowMode.equals(COMPLETE_MODE)) {
			mNowMode = EDITOR_MODE;
			mEditorTv.setText(getString(R.string.complete));
		} else {
			mNowMode = COMPLETE_MODE;
			mEditorTv.setText(getString(R.string.editor));
			modifyCart();
		}
		mGoodsAdapter.setmNowMode(mNowMode);
		mGoodsAdapter.notifyDataSetChanged();
	}

	private void modifyCart() {
		delCart();
		updateCart();
	}

	private void updateCart() {
		for (Goods goods : mGoodsUpdateList) {
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			CartLogic.update(mContext, mHandler, goods.getScid(), "",
					goods.getNum());
		}
	}

	private void delCart() {
		for (Goods goods : mGoodsDelList) {
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			CartLogic.del(mContext, mHandler, goods.getScid());
		}
	}

	private void addUpdate(Goods goods) {
		boolean isHasGoods = false;
		for (int i = 0; i < mGoodsUpdateList.size(); i++) {
			if (mGoodsUpdateList.get(i).getScid().equals(goods.getScid())) {
				mGoodsUpdateList.set(i, goods);
				isHasGoods = true;
			}
		}
		if (!isHasGoods) {
			mGoodsUpdateList.add(goods);
		}
		mGoodsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.shop_cart_modify_tv: {
			setCartMode();
			break;
		}
		case R.id.shop_cart_login_btn: {
			Intent intent = new Intent(ShopCartActivity.this,
					LoginActivity.class);
			intent.setAction(LoginActivity.ORIGIN_FROM_CART_KEY);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		switch (which) {
		case R.id.cart_goods_add_ib: {
			Goods goods = mGoodsList.get(position);
			String num = String.valueOf(Integer.parseInt(goods.getNum()) + 1);
			goods.setNum(num);
			mGoodsList.set(position, goods);
			addUpdate(goods);
			break;
		}
		case R.id.cart_goods_reduce_ib: {
			Goods goods = mGoodsList.get(position);
			if (Integer.parseInt(goods.getNum()) > 1) {
				String num = String
						.valueOf(Integer.parseInt(goods.getNum()) - 1);
				goods.setNum(num);
				mGoodsList.set(position, goods);
				addUpdate(goods);
			}

			break;
		}
		case R.id.cart_goods_collect_ll: {

			break;
		}
		case R.id.cart_goods_del_ll: {
			mGoodsDelList.add(mGoodsList.get(position));
			mGoodsList.remove(position);
			ArrayList<Goods> arrayList = new ArrayList<Goods>();
			for (Goods goods : mGoodsList) {
				arrayList.add(goods);
			}
			mGoodsList.clear();
			mGoodsList.addAll(arrayList);
			mGoodsAdapter.notifyDataSetChanged();
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
