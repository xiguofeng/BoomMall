package com.plmt.boommall.ui.view.viewflow;

import com.plmt.boommall.ui.view.viewflow.ViewFlow.ViewSwitchListener;

public interface FlowIndicator extends ViewSwitchListener {

	/*
	 * 设置当前ViewFlow。这个方法被调用的ViewFlow当FlowIndicator附属于它。
	 */
	public void setViewFlow(ViewFlow view);

	/**
	 * 
	 * 滚动位置已经被改变了。一个FlowIndicator可能实现这个方法,以反映当前的位置
	 */
	public void onScrolled(int h, int v, int oldh, int oldv);
}
