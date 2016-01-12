package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.NotifyGoodsInfo;
import com.plmt.boommall.ui.utils.ListItemClickHelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AOGAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<NotifyGoodsInfo> mDatas;

	private LayoutInflater mInflater;
	
	private ListItemClickHelp mCallback;

	public AOGAdapter(Context context, ArrayList<NotifyGoodsInfo> datas,ListItemClickHelp callback) {
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
			convertView = mInflater.inflate(R.layout.list_aog_item, null);

			holder = new ViewHolder();
			holder.mName = (TextView) convertView
					.findViewById(R.id.list_aog_name_tv);
			holder.mPrice = (TextView) convertView
					.findViewById(R.id.list_aog_price_tv);

			holder.mIcon = (ImageView) convertView.findViewById(R.id.list_aog_goods_iv);
			holder.mDelIv= (ImageView) convertView.findViewById(R.id.list_aog_del_iv);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.mName.setText(mDatas.get(position).getProduct_name().trim());
		holder.mPrice.setText("￥" + mDatas.get(position).getProduct_price());

		ImageLoader.getInstance().displayImage(mDatas.get(position).getProduct_image(),
				holder.mIcon);
		
		final int tempPosition = position;
		final View view = convertView;
		final int whichDel = holder.mDelIv.getId();
		
		holder.mDelIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mCallback.onClick(view, v, tempPosition, whichDel);

			}
		});

		return convertView;
	}

	static class ViewHolder {

		public TextView mName;

		public TextView mPrice;

		public TextView mOriginalPrice;

		public ImageView mIcon;
		
		public ImageView mDelIv;
	}

}
