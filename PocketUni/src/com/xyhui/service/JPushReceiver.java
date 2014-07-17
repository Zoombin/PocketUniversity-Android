package com.xyhui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.IntentCompat;
import cn.jpush.android.api.JPushInterface;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.LoadingActivity;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.weibo.ChatDetailListActivity;
import com.xyhui.types.ExtraMsg;
import com.xyhui.utils.Params;

public class JPushReceiver extends BroadcastReceiver {
	private String mTitle;
	private String mContent;
	private ExtraMsg mExtraMsg;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		VolleyLog.d("onReceive - " + intent.getAction());

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			VolleyLog.d("Unhandled intent");
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			VolleyLog.d("收到了自定义消息。消息内容是：%s ", bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
			mTitle = bundle.getString(JPushInterface.EXTRA_TITLE);
			mContent = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			VolleyLog.d(extra);
			mExtraMsg = JSONUtils.fromJson(extra, ExtraMsg.class);
			if (Params.JPush.TYPE_PRIVETE_MSG.equals(mExtraMsg.type)) {
				gotoChat(context);
			}
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			VolleyLog.d("收到了通知");
			// 在这里可以做些统计，或者做些其他工作
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			// 在这里可以自己写代码去定义用户点击后的行为
			VolleyLog.d("用户点击打开了通知");

			// 统计用户打开通知的事件
			JPushInterface.reportNotificationOpened(context,
					bundle.getString(JPushInterface.EXTRA_MSG_ID));

			// 清除该通知
			int notificaitonId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			JPushInterface.clearNotificationById(PuApp.get(), notificaitonId);

			loadApp(context);
		} else {
			VolleyLog.d("Unhandled intent");
		}
	}

	private void loadApp(Context context) {
		Intent load = new Intent(context, LoadingActivity.class);
		load.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(load);
	}

	private void gotoChat(Context context) {
		if (PuApp.get().isLogon()) {
			if (PuApp.get().getFrontPage().contains(ChatDetailListActivity.class.getSimpleName())) {
				Intent chat = new Intent(context, ChatDetailListActivity.class);
				chat.putExtra(Params.INTENT_EXTRA.MESSAGE_UID, mExtraMsg.uid);
				chat.putExtra(Params.INTENT_EXTRA.USERNAME, mExtraMsg.uname);
				chat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(chat);
			} else {
				msgNotification(context);
			}
		} else {
			loadApp(context);
		}
	}

	private void msgNotification(Context context) {
		Intent intent = null;

		mTitle = String.format(mTitle + ":来自%s", mExtraMsg.uname);

		if (null != mExtraMsg) {
			intent = new Intent(context, ChatDetailListActivity.class);
			intent.putExtra(Params.INTENT_EXTRA.MESSAGE_UID, mExtraMsg.uid);
			intent.putExtra(Params.INTENT_EXTRA.USERNAME, mExtraMsg.uname);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent pIntent = PendingIntent.getActivity(context, 5, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Notification n = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher).setTicker(mTitle)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentTitle(mTitle)
				.setContentText(mContent).setContentIntent(pIntent).build();

		n.defaults |= Notification.DEFAULT_SOUND;

		// 更新通知栏
		notificationManager.notify(R.id.notification_update_installed, n);
		VolleyLog.d("notification, notify(msg)");
	}
}
