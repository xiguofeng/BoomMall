package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.ShoppingCart;
import com.plmt.boommall.network.logic.CartLogic;
import com.plmt.boommall.ui.adapter.ShoppingCartAdapter;
import com.plmt.boommall.ui.adapter.ShoppingCartAdapter.ischeck;
import com.plmt.boommall.ui.utils.ListItemClickHelpWithID;
import com.plmt.boommall.ui.utils.OtherActivityClickHelp;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.utils.CartManager;
import com.plmt.boommall.utils.UserInfoManager;

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

public class ShoppingCartActivity extends Activity implements ischeck,
		OnClickListener, ListItemClickHelpWithID, OtherActivityClickHelp {

	public final static String EDITOR_MODE = "0";
	public final static String COMPLETE_MODE = "1";

	private static Context mContext;
	private String mNowMode = COMPLETE_MODE;

	private ExpandableListView mGoodsElv;
	private ShoppingCartAdapter mEAdapter;
	private List<ShoppingCart> mGroup = new ArrayList<ShoppingCart>();
	private Map<String, List<ShoppingCart>> mChild = new HashMap<String, List<ShoppingCart>>();

	private ArrayList<ShoppingCart> mShoppingCartList = new ArrayList<ShoppingCart>();
	private ArrayList<ShoppingCart> mShoppingCartListDelList = new ArrayList<ShoppingCart>();
	private ArrayList<ShoppingCart> mShoppingCartListUpdateList = new ArrayList<ShoppingCart>();

	private ArrayList<ShoppingCart> mSelectedShoppingCartList = new ArrayList<ShoppingCart>();

	private TextView mTotalNumTv;
	private TextView mEditorTv;

	public static LinearLayout mCartNullLl;
	public static LinearLayout mCartNotLoginLl;
	public static TextView mCartNullTv;
	public static Button mCartLoginBtn;

	private String mTotalMoney = "0";
	public static boolean isCanSumbit = false;

	public static boolean isNeedUpdate = false;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CartLogic.CART_LIST_GET_SUC: {
				if (null != msg.obj) {
					mShoppingCartList.clear();
					mShoppingCartList
							.addAll((Collection<? extends ShoppingCart>) msg.obj);
					refresh();
					isNeedUpdate = false;
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
				Toast.makeText(mContext, "添加购物车失败！", Toast.LENGTH_SHORT).show();
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
		}

	};

	Handler mSetSelectHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CartLogic.CART_SET_SELECT_SUC: {
				Intent intent = new Intent(ShoppingCartActivity.this,
						CreateOrderActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				break;

			}
			case CartLogic.CART_SET_SELECT_FAIL: {
				if (null != msg.obj) {
				}
				break;
			}
			case CartLogic.CART_SET_SELECT_EXCEPTION: {
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
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = ShoppingCartActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		setContentView(R.layout.shopping_cart_expandablelistview);
		initView();
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

		initListView();
		HomeActivity.setmOtherActivityClickHelp(this);
	}

	private void initListView() {
		mGoodsElv = (ExpandableListView) findViewById(R.id.shopping_cart_elv);
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
		getCartList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isNeedUpdate) {
			initData();
		}
		HomeActivity.setCartMenuShow(true, mTotalMoney);
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
		//clear 
		mGroup.clear();
		mChild.clear();
		mEAdapter.notifyDataSetChanged();
		int tempSize = mEAdapter.getGroupCount();
		for (int i = 0; i < tempSize; i++) {
			mGoodsElv.expandGroup(i);
		}
		
		for (int i = 0; i < mShoppingCartList.size(); i++) {
			String manuf = mShoppingCartList.get(i).getManufacturer();
			boolean isGroupHas = false;
			for (int j = 0; j < mGroup.size(); j++) {
				if (manuf.equals(mGroup.get(j).getManufacturer())) {
					isGroupHas = true;
				}
			}

			if (!isGroupHas) {
				mGroup.add(mShoppingCartList.get(i));
			}
			if (!mChild.containsKey(manuf)) {
				ArrayList<ShoppingCart> shoppingCartList = new ArrayList<ShoppingCart>();
				shoppingCartList.add(mShoppingCartList.get(i));
				mChild.put(manuf, shoppingCartList);
			} else {
				mChild.get(manuf).add(mShoppingCartList.get(i));
			}
			isGroupHas = false;
		}

		mEAdapter.notifyDataSetChanged();
		int size = mEAdapter.getGroupCount();
		for (int i = 0; i < size; i++) {
			mGoodsElv.expandGroup(i);
		}

		mGoodsElv.setVisibility(View.VISIBLE);
		if (null != mProgressDialog && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mCartNotLoginLl.setVisibility(View.GONE);

		if (mGroup.size() > 0) {
			mCartNullLl.setVisibility(View.GONE);
			mCartNullTv.setVisibility(View.GONE);
		} else {
			mTotalMoney = "0";
			HomeActivity.setCartMenuShow(true, mTotalMoney);
		}

		mTotalNumTv.setText("(" + String.valueOf(mGroup.size()) + ")");

		CartManager.getsSelectCartList().clear();
		CartManager.getsSelectCartList().addAll(CartManager.getsCartList());

		if (CartManager.getsSelectCartList().size() > 0) {
			// HomeActivity.mCheckAllIb.setChecked(true);
		} else {
			// HomeActivity.mCheckAllIb.setChecked(false);
		}

		// CartManager.setCartTotalMoney();
	}

	private void getCartList() {
		if (UserInfoManager.getLoginIn(mContext)) {
			mCartNullLl.setVisibility(View.VISIBLE);
			mCartNullTv.setVisibility(View.VISIBLE);
			mCartNotLoginLl.setVisibility(View.GONE);
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
		int size = mEAdapter.getGroupCount();
		for (int i = 0; i < size; i++) {
			mGoodsElv.expandGroup(i);
		}

	}

	private void modifyCart() {
		mProgressDialog.show();
		delCart();
		updateCart();

	}

	private void updateCart() {
		for (ShoppingCart shoppingcart : mShoppingCartListUpdateList) {
			CartLogic.update(mContext, mHandler, shoppingcart.getId(), "",
					shoppingcart.getQty());
		}
	}

	private void delCart() {
		for (ShoppingCart shoppingcart : mShoppingCartListDelList) {
			CartLogic.del(mContext, mHandler, shoppingcart.getId());
		}
	}

	private void addUpdate(ShoppingCart shoppingcart) {
		boolean isHasGoods = false;
		for (int i = 0; i < mShoppingCartListUpdateList.size(); i++) {
			if (mShoppingCartListUpdateList.get(i).getId()
					.equals(shoppingcart.getId())) {
				mShoppingCartListUpdateList.set(i, shoppingcart);
				isHasGoods = true;
			}
		}
		if (!isHasGoods) {
			mShoppingCartListUpdateList.add(shoppingcart);
		}

		mEAdapter.notifyDataSetChanged();
		int size = mEAdapter.getGroupCount();
		for (int i = 0; i < size; i++) {
			mGoodsElv.expandGroup(i);
		}

	}

	private ShoppingCart getShoppingCartById(String id) {
		ShoppingCart shoppingCart = null;
		for (int i = 0; i < mShoppingCartList.size(); i++) {
			if (id.equals(mShoppingCartList.get(i).getId())) {
				shoppingCart = mShoppingCartList.get(i);
			}
		}
		return shoppingCart;
	}

	private int getPositionById(String id) {
		int position = -1;
		for (int i = 0; i < mShoppingCartList.size(); i++) {
			if (id.equals(mShoppingCartList.get(i).getId())) {
				position = i;
			}
		}
		return position;
	}

	private void check() {
		mTotalMoney = "0";
		mSelectedShoppingCartList.clear();
		int selectManuNum = 0;
		for (Entry<String, List<ShoppingCart>> entry : mChild.entrySet()) {
			List<ShoppingCart> list = entry.getValue();
			boolean isHasChecked = false;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).isIscheck()) {
					mSelectedShoppingCartList.add(list.get(i));
					if (!isHasChecked) {
						selectManuNum = selectManuNum + 1;
					}
					isHasChecked = true;
					mTotalMoney = String.valueOf(Float.parseFloat(mTotalMoney)
							+ (Float.parseFloat(list.get(i).getFinalPrice()))
							* Integer.parseInt(list.get(i).getQty()));
				}
			}
		}
		if (selectManuNum > 1 || selectManuNum == 0) {
			isCanSumbit = false;
		} else {
			isCanSumbit = true;
		}

		HomeActivity.setCartMenuShow(true, mTotalMoney);
	}

	// public static void setSelect() {
	//
	// if (isCanSumbit) {
	// try {
	// JSONArray jsonArray = new JSONArray();
	// for (int i = 0; i < mSelectedShoppingCartList.size(); i++) {
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("items_id", mSelectedShoppingCartList.get(i).getId());
	// jsonArray.put(jsonObject);
	// }
	// String selectItems = jsonArray.toString();
	// selectItems = "'" + selectItems + "'";
	// CartLogic.setSelectItem(mContext, mSetSelectHandler, selectItems);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// } else {
	// Toast.makeText(mContext, "由于跨境运输限制,只能对单一仓库进行结算！",
	// Toast.LENGTH_LONG).show();
	// }
	// }

	@Override
	public void ischekgroup(int groupposition, boolean ischeck) {
		List<ShoppingCart> child = mChild.get(mGroup.get(groupposition)
				.getManufacturer());
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
	public void onClick(View item, View widget, int position, int which,
			String id) {
		switch (which) {
		case R.id.cart_goods_add_ib: {
			ShoppingCart shoppingCart = getShoppingCartById(id);
			int tempPosition = getPositionById(id);

			if (null != shoppingCart) {
				String num = String.valueOf(Integer.parseInt(shoppingCart
						.getNum()) + 1);
				shoppingCart.setNum(num);
				mShoppingCartList.set(tempPosition, shoppingCart);
				addUpdate(shoppingCart);
			}

			break;
		}
		case R.id.cart_goods_reduce_ib: {
			ShoppingCart shoppingCart = getShoppingCartById(id);
			int tempPosition = getPositionById(id);
			if (null != shoppingCart) {
				if (Integer.parseInt(shoppingCart.getNum()) > 1) {
					String num = String.valueOf(Integer.parseInt(shoppingCart
							.getNum()) - 1);
					shoppingCart.setNum(num);
					mShoppingCartList.set(tempPosition, shoppingCart);
					addUpdate(shoppingCart);
				}
			}

			break;
		}
		case R.id.cart_goods_collect_ll: {

			break;
		}
		case R.id.cart_goods_del_ll: {
			ShoppingCart shoppingCart = getShoppingCartById(id);
			int tempPosition = getPositionById(id);
			mShoppingCartListDelList.add(shoppingCart);
			mShoppingCartList.remove(tempPosition);

			ArrayList<ShoppingCart> arrayList = new ArrayList<ShoppingCart>();
			for (ShoppingCart tempShoppingCart : mShoppingCartList) {
				arrayList.add(tempShoppingCart);
			}
			mShoppingCartList.clear();
			mShoppingCartList.addAll(arrayList);
			refresh();
			break;
		}
		case R.id.cart_goods_cb: {
			check();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onClick(int which) {
		if (isCanSumbit) {
			mProgressDialog.show();
			try {
				JSONArray jsonArray = new JSONArray();
				for (int i = 0; i < mSelectedShoppingCartList.size(); i++) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("items_id", mSelectedShoppingCartList.get(i)
							.getId());
					jsonArray.put(jsonObject);
				}
				String selectItems = jsonArray.toString();
				// selectItems = "'" + selectItems + "'";
				CartLogic.setSelectItem(mContext, mSetSelectHandler,
						selectItems);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(mContext, "请选择单一仓库的商品进行结算！", Toast.LENGTH_LONG)
					.show();
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
