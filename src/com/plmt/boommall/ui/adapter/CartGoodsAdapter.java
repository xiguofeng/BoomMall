package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.ui.activity.HomeActivity;
import com.plmt.boommall.ui.activity.ShopCartActivity;
import com.plmt.boommall.ui.utils.ListItemClickHelp;

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
import android.widget.LinearLayout;
import android.widget.TextView;

public class CartGoodsAdapter extends BaseAdapter {

	private Context mContext;
	
	private String mNowMode;

	private ArrayList<Goods> mDatas;

	private LayoutInflater mInflater;

	private ListItemClickHelp mCallback;

	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> mIsSelected = new HashMap<Integer, Boolean>();

	public CartGoodsAdapter(Context context, ArrayList<Goods> datas, ListItemClickHelp callback) {
		mContext = context;
		mDatas = datas;
		mCallback = callback;
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
			convertView = mInflater.inflate(R.layout.list_shop_cart_goods_item,null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.cart_goods_name_tv);
			holder.mPrice = (TextView) convertView.findViewById(R.id.cart_goods_price_tv);
			holder.mOriginalPrice = (TextView) convertView.findViewById(R.id.cart_goods_original_prices_tv);

			//holder.mCheckIb = (CheckBox) convertView.findViewById(R.id.cart_goods_select_ib);
			holder.mAddIb = (ImageButton) convertView.findViewById(R.id.cart_goods_add_ib);
			holder.mReduceIb = (ImageButton) convertView.findViewById(R.id.cart_goods_reduce_ib);
			
			holder.mCollectionLl = (LinearLayout) convertView.findViewById(R.id.cart_goods_collect_ll);
			holder.mDelLl = (LinearLayout) convertView.findViewById(R.id.cart_goods_del_ll);

			holder.mNum = (EditText) convertView.findViewById(R.id.cart_goods_count_et);
			holder.mIcon = (ImageView) convertView.findViewById(R.id.cart_goods_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(mNowMode.equals(ShopCartActivity.EDITOR_MODE)){
			
		}else{
			
		}

		holder.mName.setText(mDatas.get(position).getName());
		holder.mPrice.setText("￥" + mDatas.get(position).getFinalPrice());
		holder.mOriginalPrice.setText("原价￥" + mDatas.get(position).getPrice());
		holder.mNum.setText(mDatas.get(position).getNum());
		ImageLoader.getInstance().displayImage(mDatas.get(position).getImage(), holder.mIcon);

		final int tempPosition = position;
		final View view = convertView;
		final int whichAdd = holder.mAddIb.getId();
		final int whichReduce = holder.mReduceIb.getId();
		final int whichCollection = holder.mCollectionLl.getId();
		final int whichDel = holder.mDelLl.getId();

		holder.vId = tempPosition;

//		holder.mCheckIb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				getmIsSelected().put(tempPosition, isChecked);
//				if (!isChecked) {
//					HomeActivity.mIsCancelAll = false;
//					HomeActivity.mCheckAllIb.setChecked(false);
//				}
//				notifyDataSetChanged();
//				HomeActivity.mIsCancelAll = true;
//			}
//		});
//
//		holder.mCheckIb.setChecked(getmIsSelected().get(position));
		holder.mAddIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mCallback.onClick(view, v, tempPosition, whichAdd);

				// Goods goods = mDatas.get(tempPosition);
				// if (TextUtils.isEmpty(goods.getNum())
				// || "null".equals(goods.getNum())) {
				// goods.setNum("1");
				// }
				// goods.setNum(String.valueOf(Integer.parseInt(goods.getNum())
				// + 1));
				// mDatas.set(tempPosition, goods);
				// CartManager.cartModifyByCart(goods, isChecked);
				// notifyDataSetChanged();

			}
		});
		holder.mReduceIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mCallback.onClick(view, v, tempPosition, whichReduce);
				// Goods goods = mDatas.get(tempPosition);
				// if (TextUtils.isEmpty(goods.getNum())
				// || "null".equals(goods.getNum())) {
				// goods.setNum("1");
				// }
				// if (Integer.parseInt(goods.getNum()) > 1) {
				// goods.setNum(String.valueOf(Integer.parseInt(goods.getNum())
				// - 1));
				// mDatas.set(tempPosition, goods);
				// CartManager.cartModifyByCart(goods, isChecked);
				// notifyDataSetChanged();
				// }
			}
		});
		holder.mCollectionLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mCallback.onClick(view, v, tempPosition, whichCollection);
			}
		});
		holder.mDelLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mCallback.onClick(view, v, tempPosition, whichDel);
			}
		});
		return convertView;
	}

	static class ViewHolder {

		public int vId;

		public TextView mName;

		public TextView mPrice;

		public TextView mOriginalPrice;

		//public CheckBox mCheckIb;

		public ImageButton mAddIb;

		public ImageButton mReduceIb;

		public EditText mNum;

		public ImageView mIcon;
		
		public LinearLayout mCollectionLl;

		public LinearLayout mDelLl;
	}

	public String getmNowMode() {
		return mNowMode;
	}

	public void setmNowMode(String mNowMode) {
		this.mNowMode = mNowMode;
	}
	
	public static HashMap<Integer, Boolean> getmIsSelected() {
		return mIsSelected;
	}

	public static void setmIsSelected(HashMap<Integer, Boolean> mIsSelected) {
		CartGoodsAdapter.mIsSelected = mIsSelected;
	}

	private boolean isAllSelect() {
		boolean isAllSelect = true;
		Set<Entry<Integer, Boolean>> entrySet = getmIsSelected().entrySet();

		for (Entry<Integer, Boolean> entry : entrySet) {
			if (!entry.getValue()) {
				isAllSelect = false;
			}
		}
		return isAllSelect;
	}

}
