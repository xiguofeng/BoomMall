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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

	private ImageView mIdcardFrontIv;
	private ImageView mIdcardBackIv;
	private CropHelper mCropHelper;
	private ChooseDialog mDialog;

	private EditText mRealNameEt;
	private EditText mIDCardEt;

	private Bitmap mFrontBit;
	private Bitmap mBackBit;

	private Button mSubmitBtn;

	private int mNowSelectIvFlag = 0;

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
		mRealNameEt = (EditText) findViewById(R.id.real_name_auth_id_card_et);
		mIDCardEt = (EditText) findViewById(R.id.real_name_auth_real_name_et);

		mIdcardFrontIv = (ImageView) findViewById(R.id.real_name_auth_idcard_front_iv);
		mIdcardBackIv = (ImageView) findViewById(R.id.real_name_auth_idcard_back_iv);
		mIdcardFrontIv.setOnClickListener(this);
		mIdcardBackIv.setOnClickListener(this);

		mSubmitBtn = (Button) findViewById(R.id.real_name_auth_btn);
		mSubmitBtn.setOnClickListener(this);

		mBackIv = (ImageView) findViewById(R.id.real_name_auth_back_iv);
		mBackIv.setOnClickListener(this);
	}

	private void initData() {
		mCropHelper = new CropHelper(this, OSUtils.getSdCardDirectory() + "/head.png");
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
					if (mNowSelectIvFlag == 0) {
						mIdcardFrontIv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
						mFrontBit = (Bitmap) data.getParcelableExtra("data");
					} else {
						mIdcardBackIv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
						mBackBit = (Bitmap) data.getParcelableExtra("data");
					}

					mCropHelper.savePhoto(data, OSUtils.getSdCardDirectory() + "/myHead.png");
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
		case R.id.real_name_auth_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}
		case R.id.real_name_auth_idcard_front_iv: {
			mDialog.popSelectDialog();
			mNowSelectIvFlag = 0;
			break;
		}
		case R.id.real_name_auth_idcard_back_iv: {
			mDialog.popSelectDialog();
			mNowSelectIvFlag = 1;
			break;
		}
		case R.id.real_name_auth_btn: {
			if (TextUtils.isEmpty(mRealNameEt.getText().toString())
					|| TextUtils.isEmpty(mIDCardEt.getText().toString())) {
				Toast.makeText(mContext, getString(R.string.real_name_id_card_hint), Toast.LENGTH_SHORT).show();
				return;
			}
			if (null == mFrontBit || null == mBackBit) {
				Toast.makeText(mContext, getString(R.string.upload_id_card), Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				TestLogic.test(mContext, mHandler, mRealNameEt.getText().toString(), mRealNameEt.getText().toString(),
						ImageUtils.Bitmap2StrByBase64(mFrontBit), ImageUtils.Bitmap2StrByBase64(mBackBit));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		}
		default:
			break;
		}

	}

}
