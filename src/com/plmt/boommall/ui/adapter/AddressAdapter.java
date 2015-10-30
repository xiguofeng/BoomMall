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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.ui.utils.ListItemClickHelp;

public class AddressAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Address> mDatas;

	private ListItemClickHelp mCallback;

	private LayoutInflater mInflater;

	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> mIsSelected = new HashMap<Integer, Boolean>();

	public AddressAdapter(Context context, ArrayList<Address> datas, ListItemClickHelp callback) {
		this.mContext = context;
		this.mDatas = datas;
		this.mCallback = callback;
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
			convertView = mInflater.inflate(R.layout.list_address_item, null);

			holder = new ViewHolder();
			holder.mNameTv = (TextView) convertView.findViewById(R.id.address_user_name_tv);
			holder.mPhoneTv = (TextView) convertView.findViewById(R.id.address_phone_tv);
			holder.mAddressDetail = (TextView) convertView.findViewById(R.id.address_detail_tv);

			holder.mEditLl = (LinearLayout) convertView.findViewById(R.id.list_address_item_edit_rl);
			holder.mDelLl = (LinearLayout) convertView.findViewById(R.id.list_address_item_del_rl);

			holder.mDefaultCb = (CheckBox) convertView.findViewById(R.id.list_address_item_cb);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mNameTv.setText(mDatas.get(position).getUsername().trim());
		holder.mPhoneTv.setText(mDatas.get(position).getTelephone());
		holder.mAddressDetail.setText(mDatas.get(position).getContent());

		final int tempPosition = position;
		final View view = convertView;
		final int whichDelView = holder.mDelLl.getId();
		final int whichEditView = holder.mEditLl.getId();

		holder.mEditLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onClick(view, v, tempPosition, whichEditView);
			}
		});

		holder.mDelLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onClick(view, v, tempPosition, whichDelView);
			}
		});

		holder.mDefaultCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked&&getmIsSelected().get(tempPosition)) {
					initCheck();
					getmIsSelected().put(tempPosition, true);
				} else {
					initCheck();
					getmIsSelected().put(tempPosition, isChecked);
				}
				notifyDataSetChanged();
			}
		});
		holder.mDefaultCb.setChecked(getmIsSelected().get(tempPosition));

		return convertView;
	}

	static class ViewHolder {

		public TextView mNameTv;

		public TextView mPhoneTv;

		public TextView mAddressDetail;

		public ImageView mUpdateIv;

		public LinearLayout mEditLl;

		public LinearLayout mDelLl;

		public CheckBox mDefaultCb;
	}

	public static HashMap<Integer, Boolean> getmIsSelected() {
		return mIsSelected;
	}

	public static void setmIsSelected(HashMap<Integer, Boolean> mIsSelected) {
		AddressAdapter.mIsSelected = mIsSelected;
	}

}
