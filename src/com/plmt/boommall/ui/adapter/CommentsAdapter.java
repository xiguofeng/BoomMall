package com.plmt.boommall.ui.adapter;

import java.util.ArrayList;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<Comment> mDatas;

	private LayoutInflater mInflater;

	public CommentsAdapter(Context context, ArrayList<Comment> datas) {
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
			convertView = mInflater.inflate(R.layout.list_comments_item, null);

			holder = new ViewHolder();
			holder.mCommentNameTv = (TextView) convertView.findViewById(R.id.list_comments_comment_detial_name_tv);
			holder.mCommentContentTv = (TextView) convertView.findViewById(R.id.list_comments_comment_detial_content_tv);
			holder.mCommentTimeTv = (TextView) convertView.findViewById(R.id.list_comments_comment_detial_time_tv);
			//holder.mCommentRatioTv = (TextView) convertView.findViewById(R.id.list_comments_comment_detial_content_tv);
			holder.mGameScoreFirstIb = (ImageButton) convertView.findViewById(R.id.list_comments_score_first_btn);
			holder.mGameScoreSecondIb = (ImageButton) convertView.findViewById(R.id.list_comments_score_second_btn);
			holder.mGameScoreThirdIb = (ImageButton) convertView.findViewById(R.id.list_comments_score_third_btn);
			holder.mGameScoreFourthIb = (ImageButton) convertView.findViewById(R.id.list_comments_score_fourth_btn);
			holder.mGameScoreFifthIb = (ImageButton) convertView.findViewById(R.id.list_comments_score_fifth_btn);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mCommentNameTv.setText(mDatas.get(position).getNickname());
		holder.mCommentTimeTv.setText(mDatas.get(position).getCreated_at());
		holder.mCommentContentTv.setText(mDatas.get(position).getDetail());
		
		setStar(holder,Integer.parseInt(mDatas.get(position).getStart_avg()));

		return convertView;
	}

	private void setStar(ViewHolder holder, int score) {
		switch (score) {
		case 1: {
			holder.mGameScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreSecondIb.setBackgroundResource(R.drawable.star_defalut);
			holder.mGameScoreThirdIb.setBackgroundResource(R.drawable.star_defalut);
			holder.mGameScoreFourthIb.setBackgroundResource(R.drawable.star_defalut);
			holder.mGameScoreFifthIb.setBackgroundResource(R.drawable.star_defalut);
			break;
		}
		case 2: {
			holder.mGameScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreSecondIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreThirdIb.setBackgroundResource(R.drawable.star_defalut);
			holder.mGameScoreFourthIb.setBackgroundResource(R.drawable.star_defalut);
			holder.mGameScoreFifthIb.setBackgroundResource(R.drawable.star_defalut);
			break;
		}
		case 3: {
			holder.mGameScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreSecondIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreThirdIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreFourthIb.setBackgroundResource(R.drawable.star_defalut);
			holder.mGameScoreFifthIb.setBackgroundResource(R.drawable.star_defalut);
			break;
		}
		case 4: {
			holder.mGameScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreSecondIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreThirdIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreFourthIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreFifthIb.setBackgroundResource(R.drawable.star_defalut);
			break;
		}
		case 5: {
			holder.mGameScoreFirstIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreSecondIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreThirdIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreFourthIb.setBackgroundResource(R.drawable.star_select_icon);
			holder.mGameScoreFifthIb.setBackgroundResource(R.drawable.star_select_icon);
			break;
		}

		default:
			break;
		}
	}

	static class ViewHolder {

		private TextView mCommentRatioTv;
		
		private TextView mCommentPersonNumTv;
		
		private TextView mCommentTimeTv;
		
		private TextView mCommentContentTv;
		
		private TextView mCommentNameTv;

		public ImageButton mGameScoreFirstIb;

		public ImageButton mGameScoreSecondIb;

		public ImageButton mGameScoreThirdIb;

		public ImageButton mGameScoreFourthIb;

		public ImageButton mGameScoreFifthIb;
	}

}
