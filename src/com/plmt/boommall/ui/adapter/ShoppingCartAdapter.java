package com.plmt.boommall.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.ShoppingCart;
import com.plmt.boommall.ui.activity.ShopCartActivity;
import com.plmt.boommall.ui.utils.ListItemClickHelp;

public class ShoppingCartAdapter extends BaseExpandableListAdapter {
	List<ShoppingCart> grouds;
	Map<String, List<ShoppingCart>> chiles;
	Context contenx;
	private String mNowMode;
	ischeck ischeck;

	private ListItemClickHelp mCallback;

	public void setischek(ischeck ischeck) {
		this.ischeck = ischeck;
	}

	public ShoppingCartAdapter(Context context, List<ShoppingCart> grouds, Map<String, List<ShoppingCart>> chiles,
			ListItemClickHelp callback) {
		this.contenx = context;
		this.grouds = grouds;
		this.chiles = chiles;
		mCallback = callback;
	}

	@Override
	public int getGroupCount() {
		return grouds.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((List<ShoppingCart>) chiles.get(grouds.get(groupPosition).getManufacturer())).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return grouds.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(contenx).inflate(R.layout.shoping_cart_group, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.tv = (TextView) convertView.findViewById(R.id.cart_group_tv);
			holder.btn = (CheckBox) convertView.findViewById(R.id.cart_group_cb);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.tv.setText(grouds.get(groupPosition).getManufacturer() + "号仓库");
		if (grouds.get(groupPosition).isIscheck()) {
			holder.btn.setChecked(true);
		} else {
			holder.btn.setChecked(false);
		}
		holder.tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(contenx, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
			}
		});
		holder.btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {

				}
			}
		});
		holder.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					grouds.get(groupPosition).setIscheck(true);
					Toast.makeText(contenx, groupPosition + "ischeck" + true, Toast.LENGTH_SHORT).show();

					ischeck.ischekgroup(groupPosition, true);
				} else {
					grouds.get(groupPosition).setIscheck(false);
					List<ShoppingCart> chiless = chiles.get(grouds.get(groupPosition).getManufacturer());
					for (ShoppingCart bean : chiless) {
						bean.setIscheck(false);
					}
					ischeck.ischekgroup(groupPosition, false);
				}
			}
		});
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ViewHolderChild viewholder;
		if (convertView == null) {
			convertView = LayoutInflater.from(contenx).inflate(R.layout.shoping_cart_item, null);
			viewholder = new ViewHolderChild();
			viewholder.mChildCb = (CheckBox) convertView.findViewById(R.id.cart_goods_cb);

			viewholder.mName = (TextView) convertView.findViewById(R.id.cart_goods_name_tv);
			viewholder.mPrice = (TextView) convertView.findViewById(R.id.cart_goods_price_tv);
			viewholder.mOriginalPrice = (TextView) convertView.findViewById(R.id.cart_goods_original_prices_tv);

			viewholder.mCount = (TextView) convertView.findViewById(R.id.cart_goods_count_tv);

			viewholder.mAddIb = (ImageButton) convertView.findViewById(R.id.cart_goods_add_ib);
			viewholder.mReduceIb = (ImageButton) convertView.findViewById(R.id.cart_goods_reduce_ib);

			viewholder.mCollectionLl = (LinearLayout) convertView.findViewById(R.id.cart_goods_collect_ll);
			viewholder.mDelLl = (LinearLayout) convertView.findViewById(R.id.cart_goods_del_ll);
			viewholder.mCollectionAndDelLl = (LinearLayout) convertView.findViewById(R.id.cart_goods_collect_del_ll);
			viewholder.mUpdateCountLl = (LinearLayout) convertView.findViewById(R.id.right_bottom_rl);

			viewholder.mNum = (EditText) convertView.findViewById(R.id.cart_goods_count_et);
			viewholder.mIcon = (ImageView) convertView.findViewById(R.id.cart_goods_iv);

			convertView.setTag(viewholder);
		}
		viewholder = (ViewHolderChild) convertView.getTag();

		viewholder.mCollectionAndDelLl.setVisibility(View.GONE);
		viewholder.mUpdateCountLl.setVisibility(View.GONE);
		viewholder.mCount.setVisibility(View.GONE);
		if (mNowMode.equals(ShopCartActivity.EDITOR_MODE)) {
			viewholder.mCollectionAndDelLl.setVisibility(View.VISIBLE);
			viewholder.mUpdateCountLl.setVisibility(View.VISIBLE);
		} else {
			viewholder.mCount.setVisibility(View.VISIBLE);
		}

		String fPrice = this.chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).getFinalPrice();
		if (fPrice.contains(".")) {
			int index = fPrice.indexOf(".");
			if (fPrice.length() > index + 2) {
				fPrice = fPrice.substring(0, index + 2);
			}
		}
		String yPrice = this.chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).getPrice();
		if (yPrice.contains(".")) {
			int index = yPrice.indexOf(".");
			if (yPrice.length() > index + 2) {
				yPrice = yPrice.substring(0, index + 2);
			}
		}

		viewholder.mName
				.setText(this.chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).getName());
		viewholder.mPrice.setText("￥" + fPrice);
		viewholder.mOriginalPrice.setText("原价￥" + yPrice);
		viewholder.mNum
				.setText(this.chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).getQty());
		viewholder.mCount.setText(
				"数量：" + this.chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).getQty());

		// ImageLoader.getInstance().displayImage(mDatas.get(position).getImage(),
		// viewholder.mIcon);

		// final int tempPosition = position;
		// final View view = convertView;
		// final int whichAdd = holder.mAddIb.getId();
		// final int whichReduce = holder.mReduceIb.getId();
		// final int whichCollection = holder.mCollectionLl.getId();
		// final int whichDel = holder.mDelLl.getId();

		// viewholder.vId = tempPosition;

		// holder.mCheckIb.setOnCheckedChangeListener(new
		// OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// getmIsSelected().put(tempPosition, isChecked);
		// if (!isChecked) {
		// HomeActivity.mIsCancelAll = false;
		// HomeActivity.mCheckAllIb.setChecked(false);
		// }
		// notifyDataSetChanged();
		// HomeActivity.mIsCancelAll = true;
		// }
		// });
		//
		// holder.mCheckIb.setChecked(getmIsSelected().get(position));
		viewholder.mAddIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// mCallback.onClick(view, v, tempPosition, whichAdd);

			}
		});
		viewholder.mReduceIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// mCallback.onClick(view, v, tempPosition, whichReduce);
			}
		});
		viewholder.mCollectionLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// mCallback.onClick(view, v, tempPosition, whichCollection);
			}
		});
		viewholder.mDelLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// mCallback.onClick(view, v, tempPosition, whichDel);
			}
		});

		// viewholder.mChildCb.setText(
		// this.chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).getManufacturer());

		if (this.chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).isIscheck()) {
			viewholder.mChildCb.setChecked(true);
		} else {
			viewholder.mChildCb.setChecked(false);
		}
		viewholder.mChildCb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).setIscheck(true);
				} else {
					chiles.get(grouds.get(groupPosition).getManufacturer()).get(childPosition).setIscheck(false);
				}
			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public static class ViewHolder {
		private CheckBox btn;
		private TextView tv;
	}

	public static class ViewHolderChild {
		public int vId;

		public TextView mName;

		public TextView mPrice;

		public TextView mOriginalPrice;

		public TextView mCount;

		public CheckBox mChildCb;

		public ImageButton mAddIb;

		public ImageButton mReduceIb;

		public EditText mNum;

		public ImageView mIcon;

		public LinearLayout mCollectionAndDelLl;

		public LinearLayout mCollectionLl;

		public LinearLayout mDelLl;

		public LinearLayout mUpdateCountLl;
	}

	public interface ischeck {
		public void ischekgroup(int groupposition, boolean ischeck);
	}

	public String getmNowMode() {
		return mNowMode;
	}

	public void setmNowMode(String mNowMode) {
		this.mNowMode = mNowMode;
	}

}
