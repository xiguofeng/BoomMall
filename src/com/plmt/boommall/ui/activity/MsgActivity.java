package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Msg;
import com.plmt.boommall.ui.adapter.MsgAdapter;
import com.plmt.boommall.utils.FileHelper;
import com.plmt.boommall.utils.JsonUtils;

public class MsgActivity extends Activity implements OnClickListener {

	private final Context mContext = MsgActivity.this;

	private ImageView mBackIv;

	private ListView mMsgLv;
	private MsgAdapter mMsgAdapter;
	private ArrayList<Msg> mMsgList = new ArrayList<Msg>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg);
		initView();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void initView() {
		mBackIv = (ImageView) findViewById(R.id.msg_back_iv);
		mBackIv.setOnClickListener(this);

		mMsgLv = (ListView) findViewById(R.id.msg_lv);
		mMsgAdapter = new MsgAdapter(mContext, mMsgList);
		mMsgLv.setAdapter(mMsgAdapter);
	}

	private void initData() {
		mMsgList.clear();
		getData();
	}

	private void getData() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileHelper.createSDFile("msg.txt");
					String jsonArrayStr = FileHelper.readSDFile("msg.txt");
					JSONArray jsonArray;

					if (!TextUtils.isEmpty(jsonArrayStr)) {
						jsonArray = new JSONArray(jsonArrayStr);
					} else {
						jsonArray = new JSONArray();
					}

					int size = jsonArray.length();
					if (size >= 10) {
						size = 10;
					}
					for (int i = 0; i < size; i++) {
						JSONObject msgJsonObject = jsonArray.getJSONObject(i);
						Msg msg = (Msg) JsonUtils.fromJsonToJava(msgJsonObject, Msg.class);
						mMsgList.add(msg);
					}
					mMsgAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
