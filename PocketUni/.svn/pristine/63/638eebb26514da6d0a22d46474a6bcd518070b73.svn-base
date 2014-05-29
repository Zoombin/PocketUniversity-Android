package com.xyhui.widget;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public abstract class FLTabActivity extends FLActivity {
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(mActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			// 3种退出方式:第三种有问题

			// PuApp.get().cleanOnTerminate();
			// finish();
			// System.exit(0);

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			// boolean moved = moveTaskToBack(false);
			// VolleyLog.d("Moved? %b", moved);
		}
	}
}
