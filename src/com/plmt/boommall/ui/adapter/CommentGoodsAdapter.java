package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.ui.utils.ListItemClickHelpWithID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentGoodsAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Goods> mDatas;

	private LayoutInflater mInflater;

	private ListItemClickHelpWithID mCallback;

	public CommentGoodsAdapter(Context context, ArrayList<Goods> datas,
			ListItemClickHelpWithID callback) {
		this.mContext = context;
		this.mDatas = datas;
		this.mCallback = callback;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View currentView, ViewGroup arg2) {
		HolderView holderView = null;
		if (currentView == null) {
			holderView = new HolderView();
			currentView = LayoutInflater.from(mContext).inflate(
					R.layout.list_comment_goods_item, null);
			holderView.iconIv = (ImageView) currentView
					.findViewById(R.id.comment_goods_iv);
			holderView.nameTv = (TextView) currentView
					.findViewById(R.id.comment_goods_name_tv);
			holderView.mCommentBtn = (Button) currentView
					.findViewById(R.id.comment_goods_comment_btn);

			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}
		
		holderView.nameTv.setText(mDatas.get(position).getName());
		ImageLoader.getInstance().displayImage(mDatas.get(position).getImage(),
				holderView.iconIv);
		
		
		final int tempPosition = position;
		final View view = currentView;
		final int whichComment = holderView.mCommentBtn.getId();
		
		holderView.mCommentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mCallback.onClick(view, v, tempPosition, whichComment,"");

			}
		});
		
		return currentView;
	}

	public class HolderView {

		private ImageView iconIv;

		private TextView nameTv;
		
		public Button mCommentBtn;

	}

}
