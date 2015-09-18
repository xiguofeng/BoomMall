package com.plmt.boommall.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CoustomGridView extends GridView {

	public CoustomGridView(Context context) {
		super(context);
	}

	public CoustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CoustomGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
