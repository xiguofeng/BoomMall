package com.plmt.boommall.ui.activity;

import com.plmt.boommall.R;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog.OnSheetItemClickListener;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog.SheetItemColor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddressEditActivity extends Activity implements OnClickListener {

	public static final String ORIGIN_FROM_ADD_ACTION = "address.add";

	public static final String ORIGIN_FROM_EDIT_ACTION = "address.edit";

	private Context mContext;

	private EditText mConsigneeEt;
	private EditText mAddressDetailEt;
	private EditText mContactWayEt;

	private TextView mAreaTv;
	private RelativeLayout mAreaRl;
	private Button mSaveBtn;

	private ImageView mBackIv;
	private ImageView mDelIv;

	private String mNowAction = ORIGIN_FROM_ADD_ACTION;

	private CustomProgressDialog mProgressDialog;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AddressLogic.ANDRESS_DEL_SUC: {
				break;
			}
			case AddressLogic.ANDRESS_DEL_FAIL: {

				break;
			}
			case AddressLogic.ANDRESS_DEL_EXCEPTION: {

				break;
			}
			case AddressLogic.NET_ERROR: {

				break;
			}
			default:
				break;
			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_add);
		mContext = AddressEditActivity.this;
		initView();
		initData();

	}

	private void initView() {
		mConsigneeEt = (EditText) findViewById(R.id.address_add_input_consignee_et);
		mAddressDetailEt = (EditText) findViewById(R.id.address_add_input_address_detail_et);
		mContactWayEt = (EditText) findViewById(R.id.address_add_input_contact_way_et);

		mAreaTv = (TextView) findViewById(R.id.address_add_input_area_content_tv);
		mAreaRl = (RelativeLayout) findViewById(R.id.address_add_input_area_rl);
		mSaveBtn = (Button) findViewById(R.id.address_add_confirm_btn);
		mAreaRl.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);

		mBackIv = (ImageView) findViewById(R.id.address_add_back_iv);
		mDelIv = (ImageView) findViewById(R.id.address_add_del_iv);
		mBackIv.setOnClickListener(this);
		mDelIv.setOnClickListener(this);
	}

	private void initData() {
		mNowAction = getIntent().getAction();
		// mProgressDialog = new CustomProgressDialog(mContext);
		// mProgressDialog.show();

		AddressLogic.getAddressData(mContext, mHandler);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 500: {
				mAreaTv.setText(data.getStringExtra("area"));
				break;
			}
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_add_input_area_rl: {
			Intent intent = new Intent(AddressEditActivity.this,
					AddressEditSelectActivity.class);
			startActivityForResult(intent, 500);

			break;
		}
		case R.id.address_add_confirm_btn: {
		}
		case R.id.address_add_back_iv: {
			finish();
			break;
		}
		case R.id.address_add_del_iv: {
			new ActionSheetDialog(AddressEditActivity.this)
					.builder()
					.setTitle(getString(R.string.is_del_address_title))
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem(getString(R.string.del), SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									AddressLogic.del(mContext, mHandler, "");
								}
							}).show();
			break;
		}
		default:
			break;
		}

	}

}
