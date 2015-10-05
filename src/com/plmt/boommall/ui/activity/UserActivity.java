package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.MenuItem;
import com.plmt.boommall.ui.adapter.UserGvCommonAdapter;
import com.plmt.boommall.ui.view.gridview.CustomGridView;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog.OnSheetItemClickListener;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog.SheetItemColor;
import com.plmt.boommall.utils.UserInfoManager;
import com.plmt.boommall.utils.cropimage.ChooseDialog;
import com.plmt.boommall.utils.cropimage.CropHelper;
import com.plmt.boommall.utils.cropimage.uitls.OSUtils;

public class UserActivity extends Activity implements OnClickListener {

	private Context mContext;

	private TextView mUserNameTv;

	private CropHelper mCropHelper;
	private ChooseDialog mDialog;
	private ImageView headImage;

	private LinearLayout mMyOrdersLl;
	private CustomGridView mOrderGv;
	private ArrayList<MenuItem> mOrderList = new ArrayList<MenuItem>();
	private UserGvCommonAdapter mOrderAdapter;
	private int[] mOrderStatePicPath = {
			R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment };
	private String[] mOrderStateStr = { "待付款", "待收货", "待评价", "退款/售后" };

	private LinearLayout mMyPropertyLl;
	private CustomGridView mPropertyGv;
	private ArrayList<MenuItem> mPropertyList = new ArrayList<MenuItem>();
	private UserGvCommonAdapter mPropertyAdapter;
	private int[] mPropertyStatePicPath = {
			R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment };
	private String[] mPropertyStateStr = { "余额", "旺卡", "积分", "优惠券" };

	private LinearLayout mMyAccountLl;
	private String[] mCorpImgStr = { "照相", "从相册中选择" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);
		mContext = UserActivity.this;
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		intCropImage();
		initOrderView();
		initPropertyView();

		mMyAccountLl = (LinearLayout) findViewById(R.id.user_my_account_ll);
		mMyAccountLl.setOnClickListener(this);
		mUserNameTv = (TextView) findViewById(R.id.user_name_tv);
	}

	private void intCropImage() {
		headImage = (ImageView) findViewById(R.id.user_icon_iv);
		headImage.setOnClickListener(this);
		mCropHelper = new CropHelper(this, OSUtils.getSdCardDirectory()
				+ "/head.png");
		mDialog = new ChooseDialog(this, mCropHelper);
	}

	private void initOrderView() {
		mMyOrdersLl = (LinearLayout) findViewById(R.id.user_my_order_ll);
		mMyOrdersLl.setOnClickListener(this);

		mOrderGv = (CustomGridView) findViewById(R.id.user_order_gv);
		int size = mOrderStatePicPath.length;
		for (int i = 0; i < size; i++) {
			MenuItem menuItem = new MenuItem();
			menuItem.setLocalImage(mOrderStatePicPath[i]);
			menuItem.setName(mOrderStateStr[i]);
			mOrderList.add(menuItem);
		}

		mOrderAdapter = new UserGvCommonAdapter(mContext, mOrderList);
		mOrderGv.setAdapter(mOrderAdapter);
		mOrderGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!TextUtils.isEmpty(UserInfoManager.userInfo.getUsername())) {
					Intent intent = new Intent(UserActivity.this,
							OrderListActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				} else {
					Intent intent = new Intent(UserActivity.this,
							LoginActivity.class);
					intent.setAction(LoginActivity.ORIGIN_FROM_USER_KEY);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				}
			}
		});
	}

	private void initPropertyView() {
		mMyPropertyLl = (LinearLayout) findViewById(R.id.user_my_property_ll);
		mMyPropertyLl.setOnClickListener(this);

		mPropertyGv = (CustomGridView) findViewById(R.id.user_property_gv);
		int size = mPropertyStatePicPath.length;
		for (int i = 0; i < size; i++) {
			MenuItem menuItem = new MenuItem();
			menuItem.setLocalImage(mPropertyStatePicPath[i]);
			menuItem.setName(mPropertyStateStr[i]);
			mPropertyList.add(menuItem);
		}

		mPropertyAdapter = new UserGvCommonAdapter(mContext, mPropertyList);
		mPropertyGv.setAdapter(mPropertyAdapter);
		mPropertyGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!TextUtils.isEmpty(UserInfoManager.userInfo.getUsername())) {
					Intent intent = new Intent(UserActivity.this,
							OrderListActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				} else {
					Intent intent = new Intent(UserActivity.this,
							LoginActivity.class);
					intent.setAction(LoginActivity.ORIGIN_FROM_USER_KEY);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				}
			}
		});
	}

	private void initData() {
		if (!TextUtils.isEmpty(UserInfoManager.userInfo.getUsername())) {
			mUserNameTv.setText(UserInfoManager.userInfo.getUsername());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("onActivityResult", requestCode + "**" + resultCode);
		if (requestCode == RESULT_CANCELED) {
			return;
		} else {
			switch (requestCode) {
			case CropHelper.HEAD_FROM_ALBUM:
				mCropHelper.getDataFromAlbum(data);
				Log.e("onActivityResult", "接收到图库图片");
				break;
			case CropHelper.HEAD_FROM_CAMERA:
				mCropHelper.getDataFromCamera(data);
				Log.e("onActivityResult", "接收到拍照图片");
				break;
			case CropHelper.HEAD_SAVE_PHOTO:
				if (data != null && data.getParcelableExtra("data") != null) {
					headImage.setImageBitmap((Bitmap) data
							.getParcelableExtra("data"));
					mCropHelper.savePhoto(data, OSUtils.getSdCardDirectory()
							+ "/myHead.png");
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_my_order_ll: {
			if (!TextUtils.isEmpty(UserInfoManager.userInfo.getUsername())) {
				Intent intent = new Intent(UserActivity.this,
						OrderListActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {
				Intent intent = new Intent(UserActivity.this,
						LoginActivity.class);
				intent.setAction(LoginActivity.ORIGIN_FROM_USER_KEY);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}

			break;
		}
		case R.id.user_my_account_ll: {
			Intent intent = new Intent(UserActivity.this, LoginActivity.class);
			intent.setAction(LoginActivity.ORIGIN_FROM_USER_KEY);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}

		case R.id.user_icon_iv: {
			mDialog.popSelectDialog();
			// new ActionSheetDialog(UserActivity.this)
			// .builder()
			// .setTitle(getString(R.string.modify_head_portrait))
			// .setCancelable(false)
			// .setCanceledOnTouchOutside(false)
			// .addSheetItem(mCorpImgStr[0], SheetItemColor.Blue,
			// new OnSheetItemClickListener() {
			// @Override
			// public void onClick(int which) {
			// }
			// })
			// .addSheetItem(mCorpImgStr[1], SheetItemColor.Blue,
			// new OnSheetItemClickListener() {
			// @Override
			// public void onClick(int which) {
			//
			// }
			// }).show();
			break;
		}
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			HomeOldActivity.showMainByOnkey();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
