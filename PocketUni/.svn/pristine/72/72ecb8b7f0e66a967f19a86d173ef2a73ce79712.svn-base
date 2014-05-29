package com.xyhui.widget;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mobstat.StatService;
import com.mslibs.widget.CActivity;

public abstract class FLActivity extends CActivity {

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
		JPushInterface.onPause(this);
		// VolleyLog.d(getClass().getSimpleName());
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		JPushInterface.onResume(this);
		// VolleyLog.d(getClass().getSimpleName());
	}
}
