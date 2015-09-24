package com.plmt.boommall.ui.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.ui.utils.MyItemClickListener;
import com.squareup.picasso.Picasso;

public class RVCategoryAdapter extends
		RecyclerView.Adapter<RVCategoryAdapter.ViewHolder> {

	private List<Category> items;
	private int itemLayout;
	private MyItemClickListener mItemClickListener;

	public RVCategoryAdapter(List<Category> items, int itemLayout) {
		this.items = items;
		this.itemLayout = itemLayout;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout,
				parent, false);
		return new ViewHolder(v, mItemClickListener);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Category item = items.get(position);
		holder.name.setText(item.getName());
		holder.iconIv.setImageBitmap(null);
		Picasso.with(holder.iconIv.getContext()).cancelRequest(holder.iconIv);
		Picasso.with(holder.iconIv.getContext()).load(item.getImgUrl())
				.into(holder.iconIv);
		holder.itemView.setTag(item);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public void setOnItemClickListener(MyItemClickListener listener) {
		this.mItemClickListener = listener;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener, OnLongClickListener {
		public ImageView iconIv;
		public ImageView selectIv;
		public LinearLayout bgIv;
		public TextView name;

		// private ListItemClickHelp mClickHelp;
		private MyItemClickListener mListener;

		public ViewHolder(View itemView, MyItemClickListener listener) {
			super(itemView);
			bgIv = (LinearLayout) itemView.findViewById(R.id.category_item_ll);
			name = (TextView) itemView.findViewById(R.id.category_item_name_tv);
			iconIv = (ImageView) itemView
					.findViewById(R.id.category_item_icon_iv);
			selectIv = (ImageView) itemView
					.findViewById(R.id.category_item_select_iv);

			this.mListener = listener;
			itemView.setOnClickListener(this);
		}

		@Override
		public boolean onLongClick(View v) {
			return false;
		}

		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onItemClick(v, getPosition());
			}
		}

	}

}