package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.logic.CartLogic;
import com.plmt.boommall.ui.adapter.CartGoodsAdapter;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.listview.SwipeMenu;
import com.plmt.boommall.ui.view.listview.SwipeMenuCreator;
import com.plmt.boommall.ui.view.listview.SwipeMenuItem;
import com.plmt.boommall.ui.view.listview.SwipeMenuListView;
import com.plmt.boommall.utils.CartManager;
import com.plmt.boommall.utils.UserInfoManager;

public class ShopCartActivity extends Activity implements OnClickListener, ListItemClickHelp {

	private Context mContext;

	public final static String EDITOR_MODE = "0";

	public final static String COMPLETE_MODE = "1";

	private String mNowMode = COMPLETE_MODE;

	private MultiStateView mMultiStateView;

	private TextView mTotalNumTv;

	public static TextView mCartNullTv;

	private TextView mEditorTv;

	public static ArrayList<Goods> sGoodsList = new ArrayList<Goods>();

	private SwipeMenuListView mSMGoodsLv;
	private ListView mGoodsLv;
	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();
	private static CartGoodsAdapter mGoodsAdapter;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case CartLogic.CART_LIST_GET_SUC: {
				if (null != msg.obj) {
					mGoodsList.clear();
					mGoodsList.addAll((Collection<? extends Goods>) msg.obj);
					mGoodsAdapter.initChecked();
					mGoodsAdapter.notifyDataSetChanged();
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
			case CartLogic.CART_MODIFY_SUC: {
				if (null != msg.obj) {
				}

				break;

			}
			case CartLogic.CART_MODIFY_FAIL: {
				break;
			}
			case CartLogic.CART_MODIFY_EXCEPTION: {
				break;
			}
			case CartLogic.CART_DEL_SUC: {
				getCartList();
				if (null != msg.obj) {
				}

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
				Toast.makeText(mContext, R.string.login_fail, Toast.LENGTH_SHORT).show();
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
			mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
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
		initMultiStateView();
		initListView();
		mCartNullTv.setVisibility(View.VISIBLE);
	}

	private void initMultiStateView() {
		mMultiStateView = (MultiStateView) findViewById(R.id.shop_cart_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(), "Fetching Data", Toast.LENGTH_SHORT).show();
					}
				});
		// mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
		mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
	}

	private void initListView() {
		mTotalNumTv = (TextView) findViewById(R.id.shop_cart_total_num_tv);
		mCartNullTv = (TextView) findViewById(R.id.shop_cart_null_tv);
		mEditorTv = (TextView) findViewById(R.id.shop_cart_modify_tv);
		mEditorTv.setOnClickListener(this);

		mSMGoodsLv = (SwipeMenuListView) findViewById(R.id.shop_cart_order_slv);
		mGoodsAdapter = new CartGoodsAdapter(mContext, mGoodsList, this);
		mGoodsAdapter.setmNowMode(COMPLETE_MODE);
		mSMGoodsLv.setAdapter(mGoodsAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(60));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mSMGoodsLv.setMenuCreator(creator);

		// step 2. listener item click event
		mSMGoodsLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					CartLogic.del(mContext, mHandler, mGoodsList.get(position).getScid());
					// mGoodsList.remove(position);
					// CartManager.cartRemove(position);
					// // del
					// for (int i = 0; i < mGoodsList.size(); i++) {
					// if (i < position) {
					// mGoodsAdapter.getmIsSelected().put(i,
					// mGoodsAdapter.getmIsSelected().get(i));
					//
					// } else {
					// mGoodsAdapter.getmIsSelected().put(i,
					// mGoodsAdapter.getmIsSelected().get(i + 1));
					// }
					//
					// }
					// mGoodsAdapter.getmIsSelected()
					// .remove(mGoodsList.size() + 1);
					// mGoodsAdapter.notifyDataSetChanged();
					//
					// mCartNullTv.setVisibility(View.VISIBLE);
					// if (CartManager.getsCartList().size() > 0) {
					// mCartNullTv.setVisibility(View.GONE);
					// }

					break;

				}
				return false;
			}
		});

		mSMGoodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Intent intent = new Intent(ShopCartActivity.this,
				// GoodsDetailActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putSerializable(GoodsDetailActivity.GOODS_KEY,
				// mGoodsList.get(position));
				// intent.putExtras(bundle);
				// startActivity(intent);
			}
		});

		mGoodsLv = (ListView) findViewById(R.id.shop_cart_order_lv);
		mGoodsLv.setAdapter(mGoodsAdapter);
	}

	private void initData() {
		mGoodsList.clear();
		getCartList();
		HomeActivity.setCartMenuShow(true);
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
		if (mGoodsList.size() > 0) {
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
		// CartManager.setCartTotalMoney();
	}

	private void getCartList() {
		if (UserInfoManager.getLoginIn(mContext)) {
			CartLogic.getList(mContext, mHandler);
		} else {
			Intent intent = new Intent(ShopCartActivity.this, LoginActivity.class);
			intent.setAction(LoginActivity.ORIGIN_FROM_CART_KEY);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	private void setCartMode() {
		if (mNowMode.equals(COMPLETE_MODE)) {
			mNowMode = EDITOR_MODE;
			mEditorTv.setText(getString(R.string.complete));
		} else {
			mNowMode = COMPLETE_MODE;
			mEditorTv.setText(getString(R.string.editor));
		}
		mGoodsAdapter.setmNowMode(mNowMode);
		mGoodsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shop_cart_modify_tv: {
			setCartMode();
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
			CartLogic.update(mContext, mHandler, goods.getScid(), "", num);
			break;
		}
		case R.id.cart_goods_reduce_ib: {
			Goods goods = mGoodsList.get(position);
			if (Integer.parseInt(goods.getNum()) > 1) {
				String num = String.valueOf(Integer.parseInt(goods.getNum()) - 1);
				CartLogic.update(mContext, mHandler, goods.getScid(), "", num);
			}
			break;
		}
		case R.id.cart_goods_collect_ll: {

			break;
		}
		case R.id.cart_goods_del_ll: {
			//CartLogic.update(mContext, mHandler, goods.getScid(), "", num);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			HomeActivity.showMainByOnkey();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
