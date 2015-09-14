package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.utils.CartManager;

public class CartGoodsAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Goods> mDatas;

	private LayoutInflater mInflater;

	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> mIsSelected = new HashMap<Integer, Boolean>();

	public CartGoodsAdapter(Context context, ArrayList<Goods> datas) {
		mContext = context;
		mDatas = datas;
		mInflater = LayoutInflater.from(mContext);
	}

	public void initCheck() {
		for (int i = 0; i < mDatas.size(); i++) {
			getmIsSelected().put(i, false);
		}
	}

	public void initChecked() {
		for (int i = 0; i < mDatas.size(); i++) {
			getmIsSelected().put(i, true);
		}
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.list_cart_goods_item, null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView
					.findViewById(R.id.cart_goods_name_tv);
			holder.mPrice = (TextView) convertView
					.findViewById(R.id.cart_goods_price_tv);
			holder.mOriginalPrice = (TextView) convertView
					.findViewById(R.id.cart_goods_original_prices_tv);

			holder.mCheckIb = (CheckBox) convertView
					.findViewById(R.id.cart_goods_select_ib);
			holder.mAddIb = (ImageButton) convertView
					.findViewById(R.id.cart_goods_add_ib);
			holder.mReduceIb = (ImageButton) convertView
					.findViewById(R.id.cart_goods_reduce_ib);

			holder.mNum = (EditText) convertView
					.findViewById(R.id.cart_goods_count_et);

			holder.mIcon = (ImageView) convertView
					.findViewById(R.id.cart_goods_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mName.setText(mDatas.get(position).getName());
		holder.mPrice.setText("￥" + mDatas.get(position).getSalesPrice());
		holder.mOriginalPrice.setText("原价￥"
				+ mDatas.get(position).getMarketPrice());
		holder.mNum.setText(mDatas.get(position).getNum());
		ImageLoader.getInstance().displayImage(
				mDatas.get(position).getIconUrl(), holder.mIcon);

		final int tempPosition = position;
		holder.vId = tempPosition;

		holder.mCheckIb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						getmIsSelected().put(tempPosition, isChecked);

						CartManager.cartCheckDataRefresh(
								mDatas.get(tempPosition), isChecked);
						// if (!isChecked) {
						//
						// for (int i = 0; i < CartManager
						// .getsSelectCartList().size(); i++) {
						// if (CartManager
						// .getsSelectCartList()
						// .get(i)
						// .getId()
						// .equals(mDatas.get(tempPosition)
						// .getId())) {
						// CartManager.getsSelectCartList().remove(i);
						// break;
						// }
						// }
						// } else {
						// boolean isHas = false;
						// for (int i = 0; i < CartManager
						// .getsSelectCartList().size(); i++) {
						// if (CartManager
						// .getsSelectCartList()
						// .get(i)
						// .getId()
						// .equals(mDatas.get(tempPosition)
						// .getId())) {
						// isHas = true;
						// break;
						// }
						// }
						// if (!isHas) {
						// CartManager.getsSelectCartList().add(
						// mDatas.get(tempPosition));
						// }
						// }
						CartManager.setCartTotalMoney();
						notifyDataSetChanged();
					}
				});

		holder.mCheckIb.setChecked(getmIsSelected().get(position));
		final boolean isChecked = getmIsSelected().get(position);

		holder.mAddIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Goods goods = mDatas.get(tempPosition);
				goods.setNum(String.valueOf(Integer.parseInt(goods.getNum()) + 1));
				mDatas.set(tempPosition, goods);
				CartManager.cartModifyByCart(goods, isChecked);
				notifyDataSetChanged();

			}
		});
		holder.mReduceIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Goods goods = mDatas.get(tempPosition);
				if (Integer.parseInt(goods.getNum()) > 1) {
					goods.setNum(String.valueOf(Integer.parseInt(goods.getNum()) - 1));
					mDatas.set(tempPosition, goods);
					CartManager.cartModifyByCart(goods, isChecked);
					notifyDataSetChanged();
				}

			}
		});
		return convertView;
	}

	static class ViewHolder {

		public int vId;

		public TextView mName;

		public TextView mPrice;

		public TextView mOriginalPrice;

		public CheckBox mCheckIb;

		public ImageButton mAddIb;

		public ImageButton mReduceIb;

		public EditText mNum;

		public ImageView mIcon;
	}

	public static HashMap<Integer, Boolean> getmIsSelected() {
		return mIsSelected;
	}

	public static void setmIsSelected(HashMap<Integer, Boolean> mIsSelected) {
		CartGoodsAdapter.mIsSelected = mIsSelected;
	}

}
