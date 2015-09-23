package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.ui.adapter.MainGvCategoryAdapter;
import com.plmt.boommall.ui.view.CustomGridView;
import com.plmt.boommall.utils.cropimage.ChooseDialog;
import com.plmt.boommall.utils.cropimage.CropHelper;
import com.plmt.boommall.utils.cropimage.uitls.OSUtils;

public class UserActivity extends Activity implements OnClickListener {

	private Context mContext;

	private TextView mUserNameTv;

	private LinearLayout mMyOrdersLl;

	private CropHelper mCropHelper;
	private ChooseDialog mDialog;
	private ImageView headImage;

	private CustomGridView mOrderGv;
	private ArrayList<Category> mOrderList = new ArrayList<Category>();
	private MainGvCategoryAdapter mOrderAdapter;
	private int[] mPicPath = { R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment,
			R.drawable.personal_order_wait_for_payment };

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
		int size = mPicPath.length;
		for (int i = 0; i < size; i++) {
			Category category = new Category();
			category.setLocalImage(mPicPath[i]);
			mOrderList.add(category);
		}

		mOrderAdapter = new MainGvCategoryAdapter(mContext, mOrderList);
		mOrderGv.setAdapter(mOrderAdapter);
		mOrderGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(UserActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initData() {

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
			Intent intent = new Intent(UserActivity.this,
					OrderListActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.user_icon_iv: {
			mDialog.popSelectDialog();
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
			HomeActivity.showMainByOnkey();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
