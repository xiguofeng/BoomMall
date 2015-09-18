package com.plmt.boommall.ui.adapter;

import java.util.List;

import android.widget.ListAdapter;
import com.plmt.boommall.entity.DemoItem;

public interface DemoAdapter extends ListAdapter {

  void appendItems(List<DemoItem> newItems);

  void setItems(List<DemoItem> moreItems);
}
