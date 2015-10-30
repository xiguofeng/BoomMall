package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.ShoppingCart;
import com.plmt.boommall.network.logic.CartLogic;
import com.plmt.boommall.ui.adapter.ShoppingCartAdapter;
import com.plmt.boommall.ui.adapter.ShoppingCartAdapter.ischeck;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.utils.CartManager;
import com.plmt.boommall.utils.UserInfoManager;

public class ShoppingCartActivity extends Activity implements ischeck,
		OnClickListener, ListItemClickHelp {

	public final static String EDITOR_MODE = "0";
	public final static String COMPLETE_MODE = "1";

	private Context mContext;
	private String mNowMode = COMPLETE_MODE;

	private ExpandableListView mGoodsElv;
	private ShoppingCartAdapter mEAdapter;
	private List<ShoppingCart> mGroup;
	private Map<String, List<ShoppingCart>> mChild;
	private ShoppingCart bean;

	private TextView mTotalNumTv;
	private TextView mEditorTv;

	public static LinearLayout mCartNullLl;
	public static LinearLayout mCartNotLoginLl;
	public static TextView mCartNullTv;
	public static Button mCartLoginBtn;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CartLogic.CART_LIST_GET_SUC: {
				if (null != msg.obj) {
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
		setContentView(R.layout.shopping_cart_expandablelistview);
		initView();
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

		initListView();
	}

	private void initListView() {
		mGoodsElv = (ExpandableListView) findViewById(R.id.lv);
		mGroup = new ArrayList<ShoppingCart>();
		bean = new ShoppingCart();
		bean.setTxt("一个商家");
		mGroup.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("另一个供货商");
		mGroup.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("最后一个");
		mGroup.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("另一个供货商123");
		mGroup.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("另一个供货商098");
		mGroup.add(bean);

		this.mChild = new HashMap<String, List<ShoppingCart>>();
		List<ShoppingCart> chiless = new ArrayList<ShoppingCart>();
		bean = new ShoppingCart();
		bean.setTxt("好商品");
		chiless.add(bean);
		bean.setTxt("又好商品");
		chiless.add(bean);

		mChild.put(mGroup.get(0).getTxt(), chiless);
		chiless = new ArrayList<ShoppingCart>();
		bean = new ShoppingCart();
		bean.setTxt("就一个");
		chiless.add(bean);

		mChild.put(mGroup.get(1).getTxt(), chiless);
		chiless = new ArrayList<ShoppingCart>();
		bean = new ShoppingCart();
		bean.setTxt("333好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("1111又好商品");
		chiless.add(bean);

		bean = new ShoppingCart();
		bean.setTxt("1115又好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("11177又好商品");
		chiless.add(bean);

		mChild.put(mGroup.get(2).getTxt(), chiless);
		chiless = new ArrayList<ShoppingCart>();
		bean = new ShoppingCart();
		bean.setTxt("333好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("1111好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("1115好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("11177好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("111777好商品");
		chiless.add(bean);

		mChild.put(mGroup.get(3).getTxt(), chiless);
		chiless = new ArrayList<ShoppingCart>();
		bean = new ShoppingCart();
		bean.setTxt("333好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("1111好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("1115好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("11177好商品");
		chiless.add(bean);
		bean = new ShoppingCart();
		bean.setTxt("111777好商品");
		chiless.add(bean);

		mChild.put(mGroup.get(4).getTxt(), chiless);
		mEAdapter = new ShoppingCartAdapter(this, mGroup, mChild, this);
		mEAdapter.setischek(this);

		mGoodsElv.setAdapter(mEAdapter);
		int size = mEAdapter.getGroupCount();
		for (int i = 0; i < size; i++) {
			mGoodsElv.expandGroup(i);
		}
	}

	private void initData() {
		mGoodsElv.setVisibility(View.GONE);
		mEAdapter.setmNowMode(COMPLETE_MODE);
		mEAdapter.notifyDataSetChanged();
		HomeActivity.setCartMenuShow(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	public static void refreshView(boolean isChecked, boolean isCancelAll) {
		// if (null != mGoodsAdapter) {
		// if (isChecked) {
		// mGoodsAdapter.initChecked();
		// } else {
		// if (isCancelAll) {
		// mGoodsAdapter.initCheck();
		// }
		// }
		// mGoodsAdapter.notifyDataSetChanged();
		// }
	}

	private void refresh() {
		mGoodsElv.setVisibility(View.VISIBLE);
		if (null != mProgressDialog && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mCartNotLoginLl.setVisibility(View.GONE);

		if (mGroup.size() > 0) {
			mCartNullLl.setVisibility(View.GONE);
			mCartNullTv.setVisibility(View.GONE);
		}

		mTotalNumTv.setText("(" + String.valueOf(mGroup.size()) + ")");

		CartManager.getsSelectCartList().clear();
		CartManager.getsSelectCartList().addAll(CartManager.getsCartList());

		if (CartManager.getsSelectCartList().size() > 0) {
			// HomeActivity.mCheckAllIb.setChecked(true);
		} else {
			// HomeActivity.mCheckAllIb.setChecked(false);
		}

		mEAdapter.notifyDataSetChanged();
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
			mGoodsElv.setVisibility(View.GONE);
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
		mEAdapter.setmNowMode(mNowMode);
		mEAdapter.notifyDataSetChanged();
	}

	private void modifyCart() {
		delCart();
		updateCart();
	}

	private void updateCart() {
		// for (Goods goods : mGoodsUpdateList) {
		// mProgressDialog = new CustomProgressDialog(mContext);
		// mProgressDialog.show();
		// CartLogic.update(mContext, mHandler, goods.getScid(), "",
		// goods.getNum());
		// }
	}

	private void delCart() {
		// for (Goods goods : mGoodsDelList) {
		// mProgressDialog = new CustomProgressDialog(mContext);
		// mProgressDialog.show();
		// CartLogic.del(mContext, mHandler, goods.getScid());
		// }
	}

	private void addUpdate(Goods goods) {
		// boolean isHasGoods = false;
		// for (int i = 0; i < mGoodsUpdateList.size(); i++) {
		// if (mGoodsUpdateList.get(i).getScid().equals(goods.getScid())) {
		// mGoodsUpdateList.set(i, goods);
		// isHasGoods = true;
		// }
		// }
		// if (!isHasGoods) {
		// mGoodsUpdateList.add(goods);
		// }
		// mGoodsAdapter.notifyDataSetChanged();
	}

	@Override
	public void ischekgroup(int groupposition, boolean ischeck) {
		List<ShoppingCart> child = mChild.get(mGroup.get(groupposition)
				.getTxt());
		for (ShoppingCart bean : child) {
			bean.setIscheck(ischeck);
		}
		mGroup.get(groupposition).setIscheck(ischeck);
		mEAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.shop_cart_modify_tv: {
			setCartMode();
			break;
		}
		case R.id.shop_cart_login_btn: {
			Intent intent = new Intent(ShoppingCartActivity.this,
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
			// Goods goods = mGoodsList.get(position);
			// String num = String.valueOf(Integer.parseInt(goods.getNum()) +
			// 1);
			// goods.setNum(num);
			// mGoodsList.set(position, goods);
			// addUpdate(goods);
			break;
		}
		case R.id.cart_goods_reduce_ib: {
			// Goods goods = mGoodsList.get(position);
			// if (Integer.parseInt(goods.getNum()) > 1) {
			// String num = String
			// .valueOf(Integer.parseInt(goods.getNum()) - 1);
			// goods.setNum(num);
			// mGoodsList.set(position, goods);
			// addUpdate(goods);
			// }

			break;
		}
		case R.id.cart_goods_collect_ll: {

			break;
		}
		case R.id.cart_goods_del_ll: {
			// mGoodsDelList.add(mGoodsList.get(position));
			// mGoodsList.remove(position);
			// ArrayList<Goods> arrayList = new ArrayList<Goods>();
			// for (Goods goods : mGoodsList) {
			// arrayList.add(goods);
			// }
			// mGoodsList.clear();
			// mGoodsList.addAll(arrayList);
			// mGoodsAdapter.notifyDataSetChanged();
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
