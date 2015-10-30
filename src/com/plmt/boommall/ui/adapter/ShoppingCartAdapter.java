package com.plmt.boommall.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.GroupBean;

public class ShoppingCartAdapter extends BaseExpandableListAdapter {
	List<GroupBean> grouds;
	Map<String,List<GroupBean>> chiles;
	Context contenx;
	ischeck ischeck;
	public void setischek(ischeck ischeck){
		this.ischeck = ischeck;
	}
	public ShoppingCartAdapter(Context context,List<GroupBean> grouds,Map<String,List<GroupBean>> chiles){
		this.contenx = context;
		this.grouds = grouds;
		this.chiles = chiles;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return grouds.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return ((List<GroupBean>)chiles.get(grouds.get(groupPosition).getTxt())).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return grouds.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return chiles.get(grouds.get(groupPosition).getTxt()).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			convertView = LayoutInflater.from(contenx).inflate(R.layout.shoping_cart_group, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			holder.btn = (CheckBox) convertView.findViewById(R.id.rb);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.tv.setText(grouds.get(groupPosition).getTxt());
		if(grouds.get(groupPosition).isIscheck()){
			holder.btn.setChecked(true);
		}else{
			holder.btn.setChecked(false);
		}
		holder.tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(contenx, ((TextView)v).getText().toString(), Toast.LENGTH_SHORT).show();
			}
		});
		holder.btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					
				}
			}
		});
		holder.btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(((CheckBox)v).isChecked()){
					grouds.get(groupPosition).setIscheck(true);
					Toast.makeText(contenx, groupPosition+"ischeck"+true, Toast.LENGTH_SHORT).show();
					
					ischeck.ischekgroup(groupPosition, true);
				}else{
//					grouds.get(groupPosition).setIscheck(false);
//					List<GroupBean> chiless = chiles.get(grouds.get(groupPosition).getTxt());
//					for(GroupBean bean:chiless){
//						bean.setIscheck(false);
//					}
					ischeck.ischekgroup(groupPosition, false);
				}
			}
		});
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderChile viewholder;
		if(convertView==null){
			convertView = LayoutInflater.from(contenx).inflate(R.layout.shoping_cart_item, null);
			viewholder = new ViewHolderChile();
			convertView.setTag(viewholder);
			viewholder.tv = (CheckBox)convertView.findViewById(R.id.tv);
			viewholder.btn = (TextView) convertView.findViewById(R.id.tv2);
		}
		viewholder = (ViewHolderChile)convertView.getTag();
		viewholder.tv.setText( this.chiles.get(grouds.get(groupPosition).getTxt()).get(childPosition).getTxt());
		viewholder.btn.setText(viewholder.tv.getText()+"商品描述");
		if(this.chiles.get(grouds.get(groupPosition).getTxt()).get(childPosition).isIscheck()){
			viewholder.tv.setChecked(true);
		}else{
			viewholder.tv.setChecked(false);
		}
		viewholder.tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(((CheckBox)v).isChecked()){
					chiles.get(grouds.get(groupPosition).getTxt()).get(childPosition).setIscheck(true);
				}else{
					chiles.get(grouds.get(groupPosition).getTxt()).get(childPosition).setIscheck(false);
				}
			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static class ViewHolder{
		private CheckBox btn;
		private TextView tv;
	}
	public static class ViewHolderChile{
		private TextView btn;
		private CheckBox tv;
	}
	public interface ischeck{
		public void ischekgroup(int groupposition,boolean ischeck)
;	}

}
