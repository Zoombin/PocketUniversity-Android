package com.mslibs.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class NotificationsUtil {

	public static void ToastBottomMsg(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void ToastTopMsg(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
		toast.show();
	}

	public static void ToastLongMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}
