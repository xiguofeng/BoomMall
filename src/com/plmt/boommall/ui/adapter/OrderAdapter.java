package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.Order;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.OrderGoodsView;

public class OrderAdapter extends BaseAdapter {

	private Context mContext;

	private HashMap<String, Object> mMap;

	private LayoutInflater mInflater;

	private ListItemClickHelp mCallback;

	public OrderAdapter(Context context, HashMap<String, Object> datas,
			ListItemClickHelp callback) {
		this.mContext = context;
		this.mMap = datas;
		this.mCallback = callback;
		mInflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		if (((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)) != null) {
			return ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).size();
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
			convertView = mInflater.inflate(R.layout.list_order_item, null);

			holder = new ViewHolder();
			holder.mId = (TextView) convertView
					.findViewById(R.id.list_order_group_id_tv);
			holder.mTime = (TextView) convertView
					.findViewById(R.id.list_order_group_time_tv);
			holder.mState = (TextView) convertView
					.findViewById(R.id.list_order_group_state_tv);
			holder.mTotalMoneyTv = (TextView) convertView
					.findViewById(R.id.list_order_group_total_money_tv);
			holder.mFreightMoneyTv = (TextView) convertView
					.findViewById(R.id.list_order_group_total_freight_tv);
			holder.mNumTv = (TextView) convertView
					.findViewById(R.id.list_order_group_total_num_tv);

			holder.mBtnLl = (LinearLayout) convertView
					.findViewById(R.id.list_order_group_btn_ll);
			holder.mCommentBtn = (Button) convertView
					.findViewById(R.id.list_order_group_comment_btn);
			holder.mViewBtn = (Button) convertView
					.findViewById(R.id.list_order_group_see_btn);
			holder.mPayBtn = (Button) convertView
					.findViewById(R.id.list_order_group_continue_pay_btn);

			holder.mGoodsLl = (LinearLayout) convertView
					.findViewById(R.id.list_order_group_wine_ll);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).size() > position) {

			holder.mId.setText("订单号："
					+ ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(
							position).getIncrement_id());
			holder.mTime.setText(((ArrayList<Order>) mMap
					.get(MsgResult.ORDER_TAG)).get(position).getCreated_time());

			holder.mState.setText(((ArrayList<Order>) mMap
					.get(MsgResult.ORDER_TAG)).get(position).getStatusLabel());

			String orderStateStr = String.valueOf(((ArrayList<Order>) mMap
					.get(MsgResult.ORDER_TAG)).get(position).getStatusLabel());

			holder.mBtnLl.setVisibility(View.GONE);
			holder.mPayBtn.setVisibility(View.GONE);
			holder.mCommentBtn.setVisibility(View.GONE);
			holder.mViewBtn.setVisibility(View.GONE);
			if ("未支付".equals(orderStateStr)) {
				holder.mBtnLl.setVisibility(View.VISIBLE);
				holder.mPayBtn.setVisibility(View.VISIBLE);
				holder.mCommentBtn.setVisibility(View.GONE);
				holder.mViewBtn.setVisibility(View.GONE);
			} else if ("已完成".equals(orderStateStr)) {
				holder.mBtnLl.setVisibility(View.VISIBLE);
				holder.mPayBtn.setVisibility(View.GONE);
				holder.mCommentBtn.setVisibility(View.VISIBLE);
				holder.mViewBtn.setVisibility(View.GONE);
			}

			final int tempPosition = position;
			final View view = convertView;
			final int whichCancel = holder.mCommentBtn.getId();
			final int whichPay = holder.mPayBtn.getId();
			final int whichView = holder.mViewBtn.getId();

			holder.mPayBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mCallback.onClick(view, v, tempPosition, whichPay);

				}
			});

			holder.mCommentBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mCallback.onClick(view, v, tempPosition, whichCancel);

				}
			});

			holder.mViewBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCallback.onClick(view, v, tempPosition, whichView);
				}
			});

			holder.mGoodsLl.removeAllViews();

			ArrayList<Goods> goodsList = new ArrayList<Goods>();
			goodsList.addAll(((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG))
					.get(position).getGoodsList());
			for (int i = 0; i < goodsList.size(); i++) {
				// TODO
				Goods goods = goodsList.get(i);
				OrderGoodsView orderGoodsView = new OrderGoodsView(mContext,
						goods);
				holder.mGoodsLl.addView(orderGoodsView);
			}

			holder.mTotalMoneyTv.setText("¥"
					+ ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(
							position).getTotal());
			holder.mFreightMoneyTv.setText("(运费：¥"
					+ ((ArrayList<Order>) mMap.get(MsgResult.ORDER_TAG)).get(
							position).getShipping_amount() + ")");
			holder.mNumTv.setText("共" + goodsList.size() + "件商品," + "合计：");
		}
		return convertView;
	}

	static class ViewHolder {

		public TextView mState;

		public TextView mTime;

		public TextView mId;

		public TextView mTotalMoneyTv;

		public TextView mFreightMoneyTv;

		public TextView mNumTv;

		public Button mCommentBtn;

		public Button mViewBtn;

		public Button mPayBtn;

		public LinearLayout mGoodsLl;

		public LinearLayout mBtnLl;

	}

}
