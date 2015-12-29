package com.plmt.boommall.ui.activity;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog.OnSheetItemClickListener;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog.SheetItemColor;
import com.plmt.boommall.utils.VerifyUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddressEditActivity extends Activity implements OnClickListener, TextWatcher {

	public static final String ORIGIN_FROM_ADD_ACTION = "address.add";

	public static final String ORIGIN_FROM_EDIT_ACTION = "address.edit";

	public static final String ADDRESS_KEY = "addressKey";

	private Context mContext;

	private EditText mConsigneeEt;
	private EditText mAddressDetailEt;
	private EditText mContactWayEt;

	private TextView mAreaTv;
	private RelativeLayout mAreaRl;
	private Button mSaveBtn;

	private ImageView mBackIv;
	private ImageView mDelIv;

	private String mAddressData;

	private String mConsignee;
	private String mAddressDetail;
	private String mContactWay;

	private String mProviceCode;
	private String mCityCode;
	private String mDistrictCode;
	private String mPostCode;

	private String mAddressId;
	private Address mAddress;
	private String mNowAction = ORIGIN_FROM_ADD_ACTION;

	private CustomProgressDialog mProgressDialog;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AddressLogic.ANDRESS_MODIFY_SUC: {
				if (mNowAction.equals(ORIGIN_FROM_ADD_ACTION)) {
					Toast.makeText(mContext, getString(R.string.address_add_suc), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, getString(R.string.address_update_suc), Toast.LENGTH_SHORT).show();
				}
				finish();
				break;
			}
			case AddressLogic.ANDRESS_MODIFY_FAIL: {

				break;
			}
			case AddressLogic.ANDRESS_MODIFY_EXCEPTION: {
				break;
			}

			case AddressLogic.ANDRESS_DEL_SUC: {
				Toast.makeText(mContext, getString(R.string.address_del_suc), Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case AddressLogic.ANDRESS_DEL_FAIL: {

				break;
			}
			case AddressLogic.ANDRESS_DEL_EXCEPTION: {

				break;
			}
			case AddressLogic.ANDRESS_DATA_GET_SUC: {
				if (!TextUtils.isEmpty((String) msg.obj)) {
					mAddressData = ((String) msg.obj);
				}
				break;
			}
			case AddressLogic.ANDRESS_DATA_GET_FAIL: {

				break;
			}
			case AddressLogic.ANDRESS_DATA_GET_EXCEPTION: {

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
		mProgressDialog = new CustomProgressDialog(mContext);
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
		if (ORIGIN_FROM_EDIT_ACTION.equals(mNowAction)) {
			mAddress = (Address) getIntent().getSerializableExtra(ADDRESS_KEY);
			fillUpData();
		} else if (ORIGIN_FROM_EDIT_ACTION.equals(mNowAction)) {
			mAddressId = getIntent().getStringExtra("addressId");
		}
		// mProgressDialog = new CustomProgressDialog(mContext);
		// mProgressDialog.show();
		mProgressDialog.show();
		AddressLogic.getAddressData(mContext, mHandler);
	}

	private void fillUpData() {
		String addr = mAddress.getContent();
		String bAddr = "";
		String aAddr = "";
		if (!TextUtils.isEmpty(addr)) {
			if (addr.contains("区")) {
				int index = addr.indexOf("区");
				bAddr = addr.substring(0, index + 1);
				aAddr = addr.substring(index + 1);
			} else if (addr.contains("县")) {
				int index = addr.indexOf("县");
				bAddr = addr.substring(0, index + 1);
				aAddr = addr.substring(index + 1);
			} else {
				bAddr = addr;
				aAddr = addr;
			}
		}

		mProviceCode = mAddress.getCn_province();
		mCityCode = mAddress.getCn_city();
		mDistrictCode = mAddress.getCn_district();
		mPostCode = mAddress.getPostCode();

		mConsigneeEt.setText(mAddress.getUsername());
		mAddressDetailEt.setText(aAddr);
		mContactWayEt.setText(mAddress.getTelephone());
		mAreaTv.setText(bAddr);
	}

	private synchronized void checkInput() {
		String telOrMobile = mContactWayEt.getText().toString().trim();

		boolean isRight = false;
		if (!TextUtils.isEmpty(telOrMobile)) {
			if (VerifyUtils.isMobile(telOrMobile) || VerifyUtils.isPhone(telOrMobile)) {
				isRight = true;
			} else {
				isRight = false;
				CharSequence html = Html.fromHtml("<font color='red'>格式不正确</font>");
				mContactWayEt.setError(html);
			}
		}

		if (!TextUtils.isEmpty(mAddressDetailEt.getText().toString().trim())
				&& !TextUtils.isEmpty(mAddressDetailEt.getText().toString().trim()) && isRight) {
			mSaveBtn.setClickable(true);
			mSaveBtn.setBackgroundResource(R.drawable.corners_bg_red_all);
		}else{
			mSaveBtn.setClickable(false);
			mSaveBtn.setBackgroundResource(R.drawable.corners_bg_gray_all);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 500: {
				mAreaTv.setText(data.getStringExtra("area"));
				mProviceCode = data.getStringExtra("proviceCode");
				mCityCode = data.getStringExtra("cityCode");
				mDistrictCode = data.getStringExtra("districtCode");
				mPostCode = data.getStringExtra("postCode");
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
			if (!TextUtils.isEmpty(mAddressData)) {
				Intent intent = new Intent(AddressEditActivity.this, AddressEditSelectActivity.class);
				intent.putExtra("addressData", mAddressData);
				startActivityForResult(intent, 500);
			} else {
				// TODO
			}
			break;
		}
		case R.id.address_add_confirm_btn: {
			mConsignee = mConsigneeEt.getText().toString().trim();
			mAddressDetail = mAddressDetailEt.getText().toString().trim();
			mContactWay = mContactWayEt.getText().toString().trim();

			if (!TextUtils.isEmpty(mConsignee) && !TextUtils.isEmpty(mProviceCode) && !TextUtils.isEmpty(mCityCode)
					&& !TextUtils.isEmpty(mDistrictCode) && !TextUtils.isEmpty(mPostCode)
					&& !TextUtils.isEmpty(mAddressDetail) && !TextUtils.isEmpty(mContactWay)) {

				mProgressDialog.show();
				if (ORIGIN_FROM_ADD_ACTION.equals(mNowAction)) {
					AddressLogic.update(mContext, mHandler, "0", mConsignee, mProviceCode, mCityCode, mDistrictCode,
							mAddressDetail, mPostCode, mContactWay, "CN");
				} else {
					AddressLogic.update(mContext, mHandler, mAddress.getId(), mConsignee, mProviceCode, mCityCode,
							mDistrictCode, mAddressDetail, mPostCode, mContactWay, "CN");
				}

			} else {
				Toast.makeText(mContext, getString(R.string.address_hint), Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case R.id.address_add_back_iv: {
			finish();
			break;
		}
		case R.id.address_add_del_iv: {
			new ActionSheetDialog(AddressEditActivity.this).builder().setTitle(getString(R.string.is_del_address_title))
					.setCancelable(false).setCanceledOnTouchOutside(false)
					.addSheetItem(getString(R.string.del), SheetItemColor.Blue, new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							if (ORIGIN_FROM_EDIT_ACTION.equals(mNowAction)) {
								mProgressDialog.show();
								AddressLogic.del(mContext, mHandler, mAddress.getId());
							}
						}
					}).show();
			break;
		}
		default:
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		checkInput();

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

}
