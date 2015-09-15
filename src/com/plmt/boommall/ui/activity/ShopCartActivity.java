package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.ui.adapter.CartGoodsAdapter;
import com.plmt.boommall.ui.view.listview.SwipeMenu;
import com.plmt.boommall.ui.view.listview.SwipeMenuCreator;
import com.plmt.boommall.ui.view.listview.SwipeMenuItem;
import com.plmt.boommall.ui.view.listview.SwipeMenuListView;
import com.plmt.boommall.utils.CartManager;

public class ShopCartActivity extends Activity implements OnClickListener {

	private Context mContext;

	private TextView mTotalNumTv;

	public static TextView mCartNullTv;

	private SwipeMenuListView mGoodsLv;

	public static ArrayList<Goods> sGoodsList = new ArrayList<Goods>();

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private static CartGoodsAdapter mGoodsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_cart);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mContext = ShopCartActivity.this;
		mTotalNumTv = (TextView) findViewById(R.id.shop_cart_total_num_tv);
		mCartNullTv = (TextView) findViewById(R.id.shop_cart_null_tv);

		mGoodsLv = (SwipeMenuListView) findViewById(R.id.shop_cart_order_lv);
		mGoodsAdapter = new CartGoodsAdapter(mContext, mGoodsList);
		mGoodsLv.setAdapter(mGoodsAdapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(60));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mGoodsLv.setMenuCreator(creator);

		// step 2. listener item click event
		mGoodsLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:

					mGoodsList.remove(position);
					CartManager.cartRemove(position);
					// del
					for (int i = 0; i < mGoodsList.size(); i++) {
						if (i < position) {
							mGoodsAdapter.getmIsSelected().put(i,
									mGoodsAdapter.getmIsSelected().get(i));

						} else {
							mGoodsAdapter.getmIsSelected().put(i,
									mGoodsAdapter.getmIsSelected().get(i + 1));
						}

					}
					mGoodsAdapter.getmIsSelected()
							.remove(mGoodsList.size() + 1);
					mGoodsAdapter.notifyDataSetChanged();

					mCartNullTv.setVisibility(View.VISIBLE);
					if (CartManager.getsCartList().size() > 0) {
						mCartNullTv.setVisibility(View.GONE);
					}

					break;

				}
				return false;
			}
		});

		mGoodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Intent intent = new Intent(ShopCartActivity.this,
				// GoodsDetailActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putSerializable(GoodsDetailActivity.GOODS_KEY,
				// mGoodsList.get(position));
				// intent.putExtras(bundle);
				// startActivity(intent);
			}
		});

	}

	private void initData() {
		mGoodsList.clear();
		mGoodsList.addAll(CartManager.getsCartList());
		mGoodsAdapter.initCheck();
		mGoodsAdapter.notifyDataSetChanged();
		mTotalNumTv.setText("(" + String.valueOf(mGoodsList.size()) + ")");
		mCartNullTv.setVisibility(View.VISIBLE);
		if (CartManager.getsCartList().size() > 0) {
			mCartNullTv.setVisibility(View.GONE);
		}

		ShopCartActivity.refreshView(true);
		CartManager.getsSelectCartList().clear();
		CartManager.getsSelectCartList().addAll(CartManager.getsCartList());

		if (CartManager.getsSelectCartList().size() > 0) {
			//HomeActivity.mCheckAllIb.setChecked(true);
		} else {
			//HomeActivity.mCheckAllIb.setChecked(false);
		}
		CartManager.setCartTotalMoney();

	}

	public static void refreshView(boolean isChecked) {
		if (null != mGoodsAdapter) {
			if (isChecked) {
				mGoodsAdapter.initChecked();
			} else {
				mGoodsAdapter.initCheck();
			}
			mGoodsAdapter.notifyDataSetChanged();
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
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

	@Override
	public void onClick(View v) {
	}

}
