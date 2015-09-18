package com.plmt.boommall.ui.view.asymmetricgridview.widget;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.plmt.boommall.ui.view.asymmetricgridview.model.AsymmetricItem;

class RowInfo<T extends AsymmetricItem> implements Parcelable {

  private final List<RowItem<T>> items;
  private final int rowHeight;
  private final float spaceLeft;

  public RowInfo(int rowHeight, List<RowItem<T>> items, float spaceLeft) {
    this.rowHeight = rowHeight;
    this.items = items;
    this.spaceLeft = spaceLeft;
  }

  @SuppressWarnings("unchecked")
  public RowInfo(final Parcel in) {
    rowHeight = in.readInt();
    spaceLeft = in.readFloat();
    int totalItems = in.readInt();

    items = new ArrayList<>();
    final ClassLoader classLoader = AsymmetricItem.class.getClassLoader();

    for (int i = 0; i < totalItems; i++) {
      items.add(new RowItem(in.readInt(), (T) in.readParcelable(classLoader)));
    }
  }

  public List<RowItem<T>> getItems() {
    return items;
  }

  public int getRowHeight() {
    return rowHeight;
  }

  public float getSpaceLeft() {
    return spaceLeft;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(@NonNull Parcel dest, final int flags) {
    dest.writeInt(rowHeight);
    dest.writeFloat(spaceLeft);
    dest.writeInt(items.size());

    for (RowItem rowItem : items) {
      dest.writeInt(rowItem.getIndex());
      dest.writeParcelable(rowItem.getItem(), 0);
    }
  }

  public static final Parcelable.Creator<RowInfo> CREATOR = new Parcelable.Creator<RowInfo>() {

    @Override public RowInfo createFromParcel(@NonNull Parcel in) {
      return new RowInfo<>(in);
    }

    @Override @NonNull public RowInfo[] newArray(final int size) {
      return new RowInfo[size];
    }
  };
}
