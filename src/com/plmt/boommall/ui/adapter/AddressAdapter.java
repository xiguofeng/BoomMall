package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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

	public AddressAdapter(Context context, ArrayList<Address> datas,
			ListItemClickHelp callback) {
		this.mContext = context;
		this.mDatas = datas;
		this.mCallback = callback;
		mInflater = LayoutInflater.from(mContext);
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
			holder.mNameTv = (TextView) convertView
					.findViewById(R.id.address_user_name_tv);
			holder.mPhoneTv = (TextView) convertView
					.findViewById(R.id.address_phone_tv);
			holder.mAddressDetail = (TextView) convertView
					.findViewById(R.id.address_detail_tv);

			holder.mEditLl = (LinearLayout) convertView
					.findViewById(R.id.list_address_item_edit_rl);
			holder.mDelLl = (LinearLayout) convertView
					.findViewById(R.id.list_address_item_del_rl);

			holder.mDefaultCb = (CheckBox) convertView
					.findViewById(R.id.list_address_item_cb);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mNameTv.setText(mDatas.get(position).getUsername().trim());
		holder.mPhoneTv.setText(mDatas.get(position).getTelephone());
		holder.mAddressDetail.setText(mDatas.get(position).getContent());

		final int tempPosition = position;
		final View view = convertView;
		final int whichView = holder.mDelLl.getId();
		
		holder.mEditLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onClick(view, v, tempPosition, whichView);
			}
		});
		
		holder.mDelLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onClick(view, v, tempPosition, whichView);
			}
		});

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

}
