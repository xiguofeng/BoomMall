package com.plmt.boommall.ui.view.gridview.paging;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListAdapter;

public class PagingGridView extends HeaderGridView {

	public interface Pagingable {
		void onLoadMoreItems();
	}

	private boolean isLoading;
	private boolean hasMoreItems;
	private Pagingable pagingableListener;
	private LoadingView loadinView;

	private boolean mScrollFlag = false;

	private ScrollListener mListener;

	public PagingGridView(Context context) {
		super(context);
		init();
	}

	public PagingGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PagingGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public boolean isLoading() {
		return this.isLoading;
	}

	public void setIsLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public void setPagingableListener(Pagingable pagingableListener) {
		this.pagingableListener = pagingableListener;
	}

	public void setHasMoreItems(boolean hasMoreItems) {
		this.hasMoreItems = hasMoreItems;
		if (!this.hasMoreItems) {
			removeFooterView(loadinView);
		}
	}

	public boolean hasMoreItems() {
		return this.hasMoreItems;
	}

	public void onFinishLoading(boolean hasMoreItems, List<? extends Object> newItems) {
		setHasMoreItems(hasMoreItems);
		setIsLoading(false);
		if (newItems != null && newItems.size() > 0) {
			ListAdapter adapter = ((FooterViewGridAdapter) getAdapter()).getWrappedAdapter();
			if (adapter instanceof PagingBaseAdapter) {
				((PagingBaseAdapter) adapter).addMoreItems(newItems);
			}
		}
	}

	private void init() {
		isLoading = false;
		loadinView = new LoadingView(getContext());
		addFooterView(loadinView);
		setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
					mScrollFlag = false;
					// 判断滚动到底部
					// if (mGoodsLv.getLastVisiblePosition() ==
					// (mGoodsLv.getCount() - 1)) {
					// mBackTopIv.setVisibility(View.VISIBLE);
					// }
					// 判断滚动到顶部
					if (null != mListener) {
						if (getFirstVisiblePosition() == 0) {
							mListener.onPagingScrollDown(false);
						}
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
					mScrollFlag = true;
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
					mScrollFlag = true;
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (totalItemCount > 0) {
					int lastVisibleItem = firstVisibleItem + visibleItemCount;
					if (!isLoading && hasMoreItems && (lastVisibleItem == totalItemCount)) {
						if (pagingableListener != null) {
							isLoading = true;
							pagingableListener.onLoadMoreItems();
						}

					}
				}

				if (null != mListener) {
					// 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
					if (mScrollFlag && firstVisibleItem > 0) {
						mListener.onPagingScrollDown(true);
					}
					if (firstVisibleItem == 0) {
						mListener.onPagingScrollDown(false);
					}
				}
			}
		});
	}

	public ScrollListener getmListener() {
		return mListener;
	}

	public void setmListener(ScrollListener mListener) {
		this.mListener = mListener;
	}

	public interface ScrollListener {
		public void onPagingScrollDown(boolean isShowBackTop);
	}

}
