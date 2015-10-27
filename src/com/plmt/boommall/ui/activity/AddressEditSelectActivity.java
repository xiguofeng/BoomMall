package com.plmt.boommall.ui.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.plmt.boommall.R;
import com.plmt.boommall.entity.AddressData;
import com.plmt.boommall.entity.AddressData;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.wheel.widget.OnWheelChangedListener;
import com.plmt.boommall.ui.view.wheel.widget.WheelView;
import com.plmt.boommall.ui.view.wheel.widget.adapters.ArrayWheelAdapter;
import com.plmt.boommall.utils.JsonUtils;

public class AddressEditSelectActivity extends Activity implements
		OnClickListener, OnWheelChangedListener {
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnConfirm;

	private Context mContext;

	/**
	 * 所有省
	 */
	protected AddressData[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<AddressData, AddressData[]> mCitisDatasMap = new HashMap<AddressData, AddressData[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<AddressData, AddressData[]> mDistrictDatasMap = new HashMap<AddressData, AddressData[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<AddressData, AddressData> mZipcodeDatasMap = new HashMap<AddressData, AddressData>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前省的名称
	 */
	protected AddressData mCurrentProvice;
	/**
	 * 当前市的名称
	 */
	protected AddressData mCurrentCity;
	/**
	 * 当前区的名称
	 */
	protected AddressData mCurrentDistrict;

	private CustomProgressDialog mProgressDialog;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AddressLogic.ANDRESS_DATA_GET_SUC: {
				if (!TextUtils.isEmpty((String) msg.obj)) {
					parserData((String) msg.obj);
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
		setContentView(R.layout.address_select);
		mContext = AddressEditSelectActivity.this;
		setUpViews();
		setUpListener();
		initData();
	}

	private void setUpViews() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.5
		p.width = (int) (d.getWidth()); // 宽度设置为屏幕的宽度
		// p.alpha = 1.0f; // 设置本身透明度
		p.dimAmount = 0.6f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setAttributes(p);
		getWindow().setGravity(Gravity.BOTTOM); // 设置靠底部

		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);
	}

	private void initData() {
		mProgressDialog = new CustomProgressDialog(mContext);
		mProgressDialog.show();
		AddressLogic.getAddressData(mContext, mHandler);
	}

	private void parserData(String data) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray provinceJsonArray = jsonObject
					.getJSONArray(MsgResult.RESULT_SUCCESS);
			mProvinceDatas = new AddressData[provinceJsonArray.length()];
			for (int i = 0; i < provinceJsonArray.length(); i++) {
				JSONObject provinceJsonObject = provinceJsonArray
						.getJSONObject(i);
				AddressData provinceAddressData = (AddressData) JsonUtils
						.fromJsonToJava(provinceJsonObject, AddressData.class);
				mProvinceDatas[i] = provinceAddressData;

				JSONArray citisJsonArray = provinceJsonObject
						.getJSONArray("sub");
				AddressData[] citisDatas = new AddressData[citisJsonArray
						.length()];
				for (int j = 0; j < citisJsonArray.length(); j++) {
					JSONObject cityJsonObject = citisJsonArray.getJSONObject(i);
					AddressData cityAddressData = (AddressData) JsonUtils
							.fromJsonToJava(cityJsonObject, AddressData.class);
					citisDatas[i] = cityAddressData;

					// 解析区
					JSONArray districtJsonArray = cityJsonObject
							.getJSONArray("sub");
					AddressData[] districtDatas = new AddressData[districtJsonArray
							.length()];
					for (int k = 0; k < districtJsonArray.length(); k++) {
						JSONObject districtJsonObject = districtJsonArray
								.getJSONObject(i);
						AddressData districtAddressData = (AddressData) JsonUtils
								.fromJsonToJava(districtJsonObject,
										AddressData.class);
						districtDatas[i] = districtAddressData;
					}
					mDistrictDatasMap.put(cityAddressData, districtDatas);
				}
				mCitisDatasMap.put(provinceAddressData, citisDatas);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		setUpData();
	}

	private void setUpData() {
		// initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<AddressData>(
				AddressEditSelectActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrict = mDistrictDatasMap.get(mCurrentCity)[newValue];
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCity = mCitisDatasMap.get(mCurrentProvice)[pCurrent];
		AddressData[] areas = mDistrictDatasMap.get(mCurrentCity);

		if (areas == null) {
			areas = new AddressData[] {};
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<AddressData>(this,
				areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProvice = mProvinceDatas[pCurrent];
		AddressData[] cities = mCitisDatasMap.get(mCurrentProvice);
		if (cities == null) {
			cities = new AddressData[] {};
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<AddressData>(this,
				cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			Intent intent = new Intent();
			intent.putExtra("area", "" + mCurrentProvice.getName()
					+ mCurrentProvice.getCode() + "," + mCurrentCity.getName()
					+ mCurrentCity.getCode() + "," + mCurrentDistrict.getName()
					+ mCurrentDistrict.getCode());
			setResult(RESULT_OK, intent);
			finish();
			showSelectedResult();
			break;
		default:
			break;
		}
	}

	private void showSelectedResult() {
		Toast.makeText(
				AddressEditSelectActivity.this,
				"当前选中:" + mCurrentProvice.getName() + mCurrentProvice.getCode()
						+ "," + mCurrentCity.getName() + mCurrentCity.getCode()
						+ "," + mCurrentDistrict.getName()
						+ mCurrentDistrict.getCode(), Toast.LENGTH_SHORT)
				.show();
	}
}
