package com.plmt.boommall.ui.activity;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.network.logic.TestLogic;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.utils.ImageUtils;
import com.plmt.boommall.utils.cropimage.ChooseDialog;
import com.plmt.boommall.utils.cropimage.CropHelper;
import com.plmt.boommall.utils.cropimage.uitls.OSUtils;

public class RealNameAuthActivity extends Activity implements OnClickListener {

	private Context mContext;

	private RelativeLayout mAddRl;
	private ImageView mBackIv;

	private ImageView mIdcardIv;
	private CropHelper mCropHelper;
	private ChooseDialog mDialog;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.real_name_auth);
		mContext = RealNameAuthActivity.this;
		initView();
		initData();

	}

	private void initView() {
		mAddRl = (RelativeLayout) findViewById(R.id.real_name_auth_rl);
		mAddRl.setOnClickListener(this);

		mIdcardIv = (ImageView) findViewById(R.id.real_name_auth_idcard_iv);

		mBackIv = (ImageView) findViewById(R.id.real_name_auth_back_iv);
		mBackIv.setOnClickListener(this);
	}

	private void initData() {
		mCropHelper = new CropHelper(this, OSUtils.getSdCardDirectory()
				+ "/head.png");
		mDialog = new ChooseDialog(this, mCropHelper);
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
					mIdcardIv.setImageBitmap((Bitmap) data
							.getParcelableExtra("data"));
					mCropHelper.savePhoto(data, OSUtils.getSdCardDirectory()
							+ "/myHead.png");

					try {
						TestLogic.test(mContext, mHandler, ImageUtils
								.Bitmap2StrByBase64((Bitmap) data
										.getParcelableExtra("data")));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
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
		case R.id.real_name_auth_rl: {
			mDialog.popSelectDialog();
			break;
		}
		case R.id.real_name_auth_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}
		default:
			break;
		}

	}

}
