package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Banner;
import com.plmt.boommall.ui.activity.Html5Activity;

public class BannerAdapter extends BaseAdapter {

	private Context mContext;

	private LayoutInflater mInflater;

	private ArrayList<Banner> mDatas;

	public BannerAdapter(Context context, ArrayList<Banner> datas) {
		mContext = context;
		this.mDatas = datas;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// 返回很大的值使得getView中的position不断增大来实现循环
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.banner_item, null);

			holder = new ViewHolder();
			holder.mBannerIcon = (ImageView) convertView
					.findViewById(R.id.banner_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int tempPosition = position % 3;
		if (0 == position % 3) {
			holder.mBannerIcon.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.menu_viewpager_1));
		} else if (1 == position % 3) {
			holder.mBannerIcon.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.menu_viewpager_1));
		} else {
			holder.mBannerIcon.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.menu_viewpager_1));
		}

		if (!TextUtils.isEmpty(mDatas.get(tempPosition).getImgurl())) {
			ImageLoader.getInstance().displayImage(
					mDatas.get(tempPosition).getImgurl(), holder.mBannerIcon);
		}
		// ImageLoader.getInstance().displayImage(
		// mDatas.get(position % 3).getIconUrl(), holder.mBannerIcon);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = position % 3;

				// 在这里可以设置跳转界面
//				Intent intent = new Intent(mContext, Html5Activity.class);
//				intent.putExtra("url", value)
//				mContext.startActivity(intent);
				if (null != mDatas.get(index).getLink()
						&& !"".equals(mDatas.get(index).getLink())) {
					Intent intent = new Intent(mContext, Html5Activity.class);
					intent.putExtra("url", mDatas.get(index).getLink());
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {

		public ImageView mBannerIcon;

	}

}
