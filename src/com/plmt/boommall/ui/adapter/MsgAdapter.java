package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Msg;

public class MsgAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Msg> mDatas;

	private LayoutInflater mInflater;

	public MsgAdapter(Context context, ArrayList<Msg> datas) {
		this.mContext = context;
		this.mDatas = datas;
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
			convertView = mInflater.inflate(R.layout.list_msg_item, null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.msg_item_name_tv);
			holder.mContent = (TextView) convertView.findViewById(R.id.msg_item_content_tv);
			// holder.mOriginalPrice = (TextView) convertView
			// .findViewById(R.id.Msg_original_prices_tv);
			//
			// holder.mIcon = (ImageView) convertView.findViewById(R.id.Msg_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mName.setText(mDatas.get(position).getTitle().trim());
		holder.mContent.setText(mDatas.get(position).getContent().trim());
		// holder.mOriginalPrice.setText("原价￥" +
		// mDatas.get(position).getPrice());
		//
		// ImageLoader.getInstance().displayImage(mDatas.get(position).getImage(),
		// holder.mIcon);

		return convertView;
	}

	static class ViewHolder {

		public TextView mName;

		public TextView mContent;

		public TextView mOriginalPrice;

		public ImageView mIcon;
	}

}
