package com.plmt.boommall.ui.view.asymmetricgridview.model;

import android.os.Parcelable;

public interface AsymmetricItem extends Parcelable {

  int getColumnSpan();

  int getRowSpan();
}
