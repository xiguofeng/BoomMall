package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.utils.cropimage.ChooseDialog;
import com.plmt.boommall.utils.cropimage.CropHelper;
import com.plmt.boommall.utils.cropimage.uitls.OSUtils;

public class UserActivity extends Activity implements OnClickListener {

	private Context mContext;

	private TextView mUserNameTv;

	private CropHelper mCropHelper;
	private ChooseDialog mDialog;
	private ImageView headImage;

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
		headImage = (ImageView) findViewById(R.id.user_icon_iv);
		mCropHelper = new CropHelper(this, OSUtils.getSdCardDirectory()
				+ "/head.png");
		mDialog = new ChooseDialog(this, mCropHelper);
		headImage.setOnClickListener(this);

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
		mDialog.popSelectDialog();
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
