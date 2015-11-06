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
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.AddressData;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.ui.view.wheel.widget.OnWheelChangedListener;
import com.plmt.boommall.ui.view.wheel.widget.WheelView;
import com.plmt.boommall.ui.view.wheel.widget.adapters.ArrayWheelAdapter;
import com.plmt.boommall.utils.JsonUtils;

public class AddressEditSelectActivity extends Activity implements
		OnClickListener, OnWheelChangedListener {
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	private TextView mCancelTv;
	private TextView mConfirmTv;

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
	protected AddressData mCurrentProvice;
	/**
	 * 当前市的名称
	 */
	protected AddressData mCurrentCity;
	/**
	 * 当前区的名称
	 */
	protected AddressData mCurrentDistrict;

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatasStr;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMapStr = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMapStr = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMapStr = new HashMap<String, String>();

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_select);
		mContext = AddressEditSelectActivity.this;
		initData();
	}

	private void setUpViews() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.5
		p.width = (int) (d.getWidth()); // 宽度设置为屏幕的宽度
		// p.alpha = 1.0f; // 设置本身透明度
		p.dimAmount = 0.6f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setAttributes(p);
		getWindow().setGravity(Gravity.BOTTOM); // 设置靠底部

		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);

		mConfirmTv = (TextView) findViewById(R.id.address_select_confirm_tv);
		mCancelTv = (TextView) findViewById(R.id.address_select_cancel_tv);
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		// 添加onclick事件
		mConfirmTv.setOnClickListener(this);
		mCancelTv.setOnClickListener(this);
	}

	private void initData() {
		String addressData = getIntent().getStringExtra("addressData");
		if (!TextUtils.isEmpty(addressData)) {
			parserData(addressData);
		}
	}

	private void parserData(String data) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray provinceJsonArray = jsonObject
					.getJSONArray(MsgResult.RESULT_DATA_TAG);
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
					JSONObject cityJsonObject = citisJsonArray.getJSONObject(j);
					AddressData cityAddressData = (AddressData) JsonUtils
							.fromJsonToJava(cityJsonObject, AddressData.class);
					citisDatas[j] = cityAddressData;

					// 解析区
					JSONArray districtJsonArray = cityJsonObject
							.getJSONArray("sub");
					AddressData[] districtDatas = new AddressData[districtJsonArray
							.length()];
					for (int k = 0; k < districtJsonArray.length(); k++) {
						JSONObject districtJsonObject = districtJsonArray
								.getJSONObject(k);
						AddressData districtAddressData = (AddressData) JsonUtils
								.fromJsonToJava(districtJsonObject,
										AddressData.class);
						districtDatas[k] = districtAddressData;
					}
					mDistrictDatasMap.put(cityAddressData, districtDatas);
				}
				mCitisDatasMap.put(provinceAddressData, citisDatas);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpData() {

		mCurrentProvice = mProvinceDatas[0];
		if (mCurrentProvice != null) {
			mCurrentCity = mCitisDatasMap.get(mCurrentProvice)[0];
			mCurrentDistrict = mDistrictDatasMap.get(mCurrentCity)[0];
		}

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
		case R.id.address_select_confirm_tv: {
			Intent intent = new Intent();
			intent.putExtra("area", "" + mCurrentProvice.getName() + ","
					+ mCurrentCity.getName() + "," + mCurrentDistrict.getName());
			intent.putExtra("proviceCode", mCurrentProvice.getCode());
			intent.putExtra("cityCode", mCurrentCity.getCode());
			intent.putExtra("districtCode", mCurrentDistrict.getCode());
			intent.putExtra("postCode", mCurrentDistrict.getZip_code());
			setResult(RESULT_OK, intent);
			finish();
			showSelectedResult();
			break;
		}
		case R.id.address_select_cancel_tv: {
			finish();
			break;
		}
		default:
			break;
		}
	}

	private void showSelectedResult() {
		Toast.makeText(
				AddressEditSelectActivity.this,
				"当前选中:" + mCurrentProvice.getName() + ","
						+ mCurrentCity.getName() + ","
						+ mCurrentDistrict.getName(), Toast.LENGTH_SHORT)
				.show();
	}
}
