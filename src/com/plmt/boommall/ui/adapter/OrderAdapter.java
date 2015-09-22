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
import com.plmt.boommall.entity.OrderOld;
import com.plmt.boommall.entity.OrderState;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.pay.PayConstants;
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
		if (((ArrayList<OrderOld>) mMap.get(MsgResult.ORDER_TAG)) != null) {
			return ((ArrayList<OrderOld>) mMap.get(MsgResult.ORDER_TAG)).size();
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
			holder.mTotalMoney = (TextView) convertView
					.findViewById(R.id.list_order_group_total_money_tv);

			holder.mCancelOrCommentBtn = (Button) convertView
					.findViewById(R.id.list_order_group_comment_or_cancel_btn);
			holder.mViewBtn = (Button) convertView
					.findViewById(R.id.list_order_group_see_btn);
			holder.mPayBtn = (Button) convertView
					.findViewById(R.id.list_order_group_continue_pay_btn);
			holder.mWineLl = (LinearLayout) convertView
					.findViewById(R.id.list_order_group_wine_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (((ArrayList<OrderOld>) mMap.get(MsgResult.ORDER_TAG)).size() > position) {

			holder.mId.setText(((ArrayList<OrderOld>) mMap
					.get(MsgResult.ORDER_TAG)).get(position).getId());
			holder.mTime.setText(((ArrayList<OrderOld>) mMap
					.get(MsgResult.ORDER_TAG)).get(position).getPayTime());

			int orderStateCode = Integer.parseInt(((ArrayList<OrderOld>) mMap
					.get(MsgResult.ORDER_TAG)).get(position).getOrderStatus());
			if (orderStateCode <= OrderState.state.length) {
				holder.mState.setText(OrderState.state[orderStateCode - 1]);
			}
			
			holder.mTotalMoney.setText("合计：￥"+((ArrayList<OrderOld>) mMap
					.get(MsgResult.ORDER_TAG)).get(position).getAmount());

			String orderStateCodeStr = String.valueOf(orderStateCode);
			String orderPayStateCodeStr = String
					.valueOf(((ArrayList<OrderOld>) mMap.get(MsgResult.ORDER_TAG))
							.get(position).getPayStatus());

			holder.mPayBtn.setVisibility(View.GONE);
			holder.mCancelOrCommentBtn.setVisibility(View.GONE);
			holder.mViewBtn.setVisibility(View.GONE);
			if (orderStateCode > 6) {
				holder.mPayBtn.setVisibility(View.GONE);
				holder.mCancelOrCommentBtn.setVisibility(View.GONE);
				holder.mViewBtn.setVisibility(View.GONE);
			} else {
				holder.mViewBtn.setVisibility(View.VISIBLE);
				if (orderStateCodeStr.equals(OrderState.ORDER_STATUS_ORDERED)
						|| orderStateCodeStr
								.equals(OrderState.ORDER_STATUS_GRABBED)) {
					holder.mCancelOrCommentBtn.setVisibility(View.VISIBLE);
					holder.mCancelOrCommentBtn.setText(mContext
							.getString(R.string.order_cancel));
				} else if (orderStateCodeStr
						.equals(OrderState.ORDER_STATUS_CONFIRMED)) {
					holder.mCancelOrCommentBtn.setVisibility(View.VISIBLE);
					holder.mCancelOrCommentBtn.setText(mContext
							.getString(R.string.add_comment));
				}

				if (OrderState.ORDER_STATUS_ORDERED.equals(orderStateCodeStr)
						&& PayConstants.PAY_STATUS_UNPAID
								.equals(orderPayStateCodeStr)) {
					holder.mPayBtn.setVisibility(View.VISIBLE);

					holder.mCancelOrCommentBtn.setVisibility(View.VISIBLE);
					holder.mCancelOrCommentBtn.setText(mContext
							.getString(R.string.order_cancel));

					holder.mViewBtn.setVisibility(View.GONE);
				}
			}

			final int tempPosition = position;
			final View view = convertView;
			final int whichCancel = holder.mCancelOrCommentBtn.getId();
			final int whichPay = holder.mPayBtn.getId();
			final int whichView = holder.mViewBtn.getId();

			holder.mPayBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mCallback.onClick(view, v, tempPosition, whichPay);

				}
			});

			holder.mCancelOrCommentBtn
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							mCallback.onClick(view, v, tempPosition,
									whichCancel);

						}
					});

			holder.mViewBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCallback.onClick(view, v, tempPosition, whichView);
				}
			});

			holder.mWineLl.removeAllViews();

			ArrayList<Goods> goodsList = new ArrayList<Goods>();
			goodsList.addAll(((ArrayList<Goods>) mMap
					.get(((ArrayList<OrderOld>) mMap.get(MsgResult.ORDER_TAG))
							.get(position).getId())));
			for (int i = 0; i < goodsList.size(); i++) {
				// TODO
				Goods goods = goodsList.get(i);
				OrderGoodsView orderGoodsView = new OrderGoodsView(mContext, goods);
				holder.mWineLl.addView(orderGoodsView);
			}
		}
		return convertView;
	}

	static class ViewHolder {

		public TextView mState;

		public TextView mTime;

		public TextView mId;
		
		public TextView mTotalMoney;

		public Button mCancelOrCommentBtn;

		public Button mViewBtn;

		public Button mPayBtn;

		public LinearLayout mWineLl;

	}

}
