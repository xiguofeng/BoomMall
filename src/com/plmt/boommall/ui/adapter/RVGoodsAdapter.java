package com.plmt.boommall.ui.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.ui.utils.MyItemClickListener;
import com.squareup.picasso.Picasso;

public class RVGoodsAdapter extends RecyclerView.Adapter<RVGoodsAdapter.ViewHolder> {

	private List<Goods> items;
	private int itemLayout;
	private MyItemClickListener mItemClickListener;

	public RVGoodsAdapter(List<Goods> items, int itemLayout) {
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
		Goods item = items.get(position);
		holder.name.setText(item.getName());
		holder.iconIv.setImageBitmap(null);
		Picasso.with(holder.iconIv.getContext()).cancelRequest(holder.iconIv);
		Picasso.with(holder.iconIv.getContext()).load(item.getIconUrl())
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
		public TextView name;
		public TextView price;
		public TextView originalPrice;

		// private ListItemClickHelp mClickHelp;
		private MyItemClickListener mListener;

		public ViewHolder(View itemView, MyItemClickListener listener) {
			super(itemView);

			iconIv = (ImageView) itemView.findViewById(R.id.goods_iv);
			name = (TextView) itemView.findViewById(R.id.goods_name_tv);
			price = (TextView) itemView.findViewById(R.id.goods_price_tv);
			originalPrice = (TextView) itemView
					.findViewById(R.id.goods_original_prices_tv);

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