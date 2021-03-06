package com.plmt.boommall.push.jpush;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plmt.boommall.entity.Msg;
import com.plmt.boommall.ui.activity.MsgActivity;
import com.plmt.boommall.utils.FileHelper;
import com.plmt.boommall.utils.FileManager;
import com.plmt.boommall.utils.FileUtils;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.cropimage.uitls.OSUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	private static ArrayList<Msg> mMsgList = new ArrayList<Msg>();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			// 打开自定义的Activity
			Intent i = new Intent(context, MsgActivity.class);
			i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals("cn.jpush.android.EXTRA")) {
				Log.e("Jpush_EXTRA", bundle.getString(key).toString());
				msgDataSave1(bundle.getString(key).toString());
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to JpushMainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (JpushMainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(
					JpushMainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(JpushMainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent
								.putExtra(JpushMainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}

	private static void msgDataSave(final String msg) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileUtils.makeDirectory(FileUtils.BASE_PATH);
					FileHelper.createSDFile("msg.txt");
					String jsonArrayStr = FileHelper.readFileSdcard("msg.txt");
					JSONArray jsonArray;

					JSONArray newJsonArray = new JSONArray();
					JSONObject jsonObject = new JSONObject(msg);
					Msg newMsg = (Msg) JsonUtils.fromJsonToJava(jsonObject,
							Msg.class);
					mMsgList.add(newMsg);
					newJsonArray.put(jsonObject);

					if (!TextUtils.isEmpty(jsonArrayStr)) {
						jsonArray = new JSONArray(jsonArrayStr);
					} else {
						jsonArray = new JSONArray();
					}

					int size = jsonArray.length();
					if (size >= 10) {
						size = 9;
					}
					for (int i = 0; i < size; i++) {
						JSONObject msgJsonObject = jsonArray.getJSONObject(i);
						Msg msg = (Msg) JsonUtils.fromJsonToJava(msgJsonObject,
								Msg.class);
						newJsonArray.put(msgJsonObject);
						mMsgList.add(msg);
					}
					FileHelper.writeSDFileNew(newJsonArray.toString(),
							"msg.txt");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private static void msgDataSave1(final String msg) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileUtils.makeDirectory(FileUtils.BASE_PATH);
					FileHelper.createSDFile("msg.txt");
					String jsonArrayStr = FileManager.read(
							OSUtils.getSdCardDirectory() + "/boommall/msg.txt",
							"UTF-8");
					JSONArray jsonArray;

					JSONArray newJsonArray = new JSONArray();
					JSONObject jsonObject = new JSONObject(msg);
					Msg newMsg = (Msg) JsonUtils.fromJsonToJava(jsonObject,
							Msg.class);
					mMsgList.add(newMsg);
					newJsonArray.put(jsonObject);

					if (!TextUtils.isEmpty(jsonArrayStr)) {
						jsonArray = new JSONArray(jsonArrayStr);
					} else {
						jsonArray = new JSONArray();
					}

					int size = jsonArray.length();
					if (size >= 10) {
						size = 9;
					}
					for (int i = 0; i < size; i++) {
						JSONObject msgJsonObject = jsonArray.getJSONObject(i);
						Msg msg = (Msg) JsonUtils.fromJsonToJava(msgJsonObject,
								Msg.class);
						newJsonArray.put(msgJsonObject);
						mMsgList.add(msg);
					}
					
					FileManager.write(newJsonArray.toString(),
							OSUtils.getSdCardDirectory() + "/boommall/msg.txt",
							"UTF-8");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
