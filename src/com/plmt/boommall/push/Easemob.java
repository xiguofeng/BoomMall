package com.plmt.boommall.push;


public class Easemob {
//
//	private Context mContext;
//
//	private static Easemob instance = null;
//
//	protected EMEventListener eventListener = null;
//
//	public synchronized static Easemob getInstance() {
//		if (instance == null) {
//			instance = new Easemob();
//		}
//		return instance;
//	}
//
//	public void init(Context context) {
//		mContext = context;
//		EMChat.getInstance().init(context);
//		EMChat.getInstance().setDebugMode(false);
//
//		Log.e("xxx_Easemob", "init");
//		EMChatManager.getInstance().login("wangchao6", "123456",
//				new EMCallBack() {// 回调
//					@Override
//					public void onSuccess() {
//						Log.e("xxx_Easemob", "登陆聊天服务器成功！");
//					}
//
//					@Override
//					public void onProgress(int progress, String status) {
//						Log.e("xxx_Easemob", "登陆聊天服务器失败！");
//					}
//
//					@Override
//					public void onError(int code, String message) {
//						Log.e("xxx_Easemob", "登陆聊天服务器失败！");
//					}
//				});
//
//		// 注册message receiver， 接收聊天消息
//		// 只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
//		NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
//		IntentFilter intentFilter = new IntentFilter(EMChatManager
//				.getInstance().getNewMessageBroadcastAction());
//		intentFilter.setPriority(3);
//		mContext.registerReceiver(msgReceiver, intentFilter);
//
//		// 注册消息事件监听
//		// registerEventListener();
//		// 注册一个监听连接状态的listener
//		EMChatManager.getInstance().addConnectionListener(
//				new MyConnectionListener());
//	}
//
//	/**
//	 * 全局事件监听
//	 */
//	protected void registerEventListener() {
//		eventListener = new EMEventListener() {
//			private BroadcastReceiver broadCastReceiver = null;
//
//			@Override
//			public void onEvent(EMNotifierEvent event) {
//				EMMessage message = null;
//				if (event.getData() instanceof EMMessage) {
//					message = (EMMessage) event.getData();
//				}
//
//				switch (event.getEvent()) {
//				case EventNewMessage:
//					break;
//				case EventOfflineMessage:
//					break;
//				// below is just giving a example to show a cmd toast, the app
//				// should not follow this
//				// so be careful of this
//				case EventNewCMDMessage: {
//
//					// 获取消息body
//					CmdMessageBody cmdMsgBody = (CmdMessageBody) message
//							.getBody();
//					final String action = cmdMsgBody.action;// 获取自定义action
//
//					// 获取扩展属性 此处省略
//					// message.getStringAttribute("");
//
//					final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
//					IntentFilter cmdFilter = new IntentFilter(
//							CMD_TOAST_BROADCAST);
//
//					if (broadCastReceiver == null) {
//						broadCastReceiver = new BroadcastReceiver() {
//
//							@Override
//							public void onReceive(Context context, Intent intent) {
//								// TODO Auto-generated method stub
//							}
//						};
//
//						// 注册广播接收者
//						mContext.registerReceiver(broadCastReceiver, cmdFilter);
//					}
//
//					Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
//					broadcastIntent.putExtra("cmd_value", action);
//					mContext.sendBroadcast(broadcastIntent, null);
//
//					break;
//				}
//				case EventDeliveryAck:
//					message.setDelivered(true);
//					break;
//				case EventReadAck:
//					message.setAcked(true);
//					break;
//				// add other events in case you are interested in
//				default: {
//					break;
//				}
//
//				}
//				Log.e("xxx_Easemob_suc", "suc");
//			}
//		};
//
//		EMChatManager.getInstance().registerEventListener(eventListener);
//	}
//
//	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// 注销广播
//			abortBroadcast();
//
//			// 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
//			String msgId = intent.getStringExtra("msgid");
//			Log.e("xxx_Easemob_new_msgId", msgId);
//			// 发送方
//			String username = intent.getStringExtra("from");
//			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
//			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
//
//			Log.e("xxx_Easemob_new_msg", message.getBody().toString());
//			// 如果是群聊消息，获取到group id
//			if (message.getChatType() == ChatType.GroupChat) {
//				username = message.getTo();
//			}
//			if (!username.equals(username)) {
//				// 消息不是发给当前会话，return
//				return;
//			}
//		}
//	}
//
//	// 实现ConnectionListener接口
//	private class MyConnectionListener implements EMConnectionListener {
//		@Override
//		public void onConnected() {
//			// 已连接到服务器
//			Log.e("xxx_MyConnectionListener_suc", "MyConnectionListener_suc");
//		}
//
//		@Override
//		public void onDisconnected(final int error) {
//			if (error == EMError.USER_REMOVED) {
//				// 显示帐号已经被移除
//			} else if (error == EMError.CONNECTION_CONFLICT) {
//				// 显示帐号在其他设备登陆
//			} else {
//				if (NetUtils.hasNetwork(mContext)) {
//					// 连接不到聊天服务器
//				} else {
//					// 当前网络不可用，请检查网络设置
//				}
//			}
//		}
//	}
//
}
