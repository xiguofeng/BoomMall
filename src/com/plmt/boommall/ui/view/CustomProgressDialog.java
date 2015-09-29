package com.plmt.boommall.ui.view;

import android.app.Dialog;
import android.content.Context;

import com.plmt.boommall.R;

public class CustomProgressDialog extends Dialog {

	Context context;

	public CustomProgressDialog(Context context) {
		super(context, R.style.DialogTheme);
		this.context = context;
		this.setCanceledOnTouchOutside(false);
		init();
	}

	void init() {
		setContentView(R.layout.loading_view);
	}

}
