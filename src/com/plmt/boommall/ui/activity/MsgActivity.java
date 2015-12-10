package com.plmt.boommall.ui.activity;

import java.util.ArrayList;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Msg;
import com.plmt.boommall.ui.adapter.MsgAdapter;
import com.plmt.boommall.utils.FileHelper;
import com.plmt.boommall.utils.FileManager;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.cropimage.uitls.OSUtils;

public class MsgActivity extends Activity implements OnClickListener {

	public static final int SUC = 0;

	public static final int FAIL = SUC + 1;

	private final Context mContext = MsgActivity.this;

	private ImageView mBackIv;

	private ListView mMsgLv;
	private MsgAdapter mMsgAdapter;
	private ArrayList<Msg> mMsgList = new ArrayList<Msg>();

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case SUC: {
				break;

			}
			case FAIL: {
				break;
			}

			default:
				break;
			}

		}

	};

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

		mMsgLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, Html5Activity.class);
				intent.putExtra("url", mMsgList.get(position).getUrl());
				mContext.startActivity(intent);
			}
		});
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
					String jsonArrayStr = FileManager.read(
							OSUtils.getSdCardDirectory() + "/boommall/msg.txt",
							"UTF-8");
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
						Msg msg = (Msg) JsonUtils.fromJsonToJava(msgJsonObject,
								Msg.class);
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
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
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
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
