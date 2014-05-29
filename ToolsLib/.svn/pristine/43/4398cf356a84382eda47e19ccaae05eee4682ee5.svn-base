package com.mslibs.widget;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mslibs.utils.Preferences;
import com.mslibs.utils.VolleyLog;

public abstract class CActivity extends FragmentActivity {
	protected Activity mActivity;

	// UI效果变量
	private ProgressDialog mDialog;
	private int mDialogCount;

	public abstract void linkUiVar();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = this;

		init();
		linkUiVar();
		bindListener();
		ensureUi();
	}

	public void init() {

	}

	public void bindListener() {

	}

	public void ensureUi() {

	}

	public void showProgress(String title, String message) {

		mDialogCount++;
		VolleyLog.v("------------------» showProgress[%d]", mDialogCount);

		if (null == mActivity || mDialogCount > 1 || mDialog != null)
			return;

		mDialog = ProgressDialog.show(mActivity, title, message, true, true);

	}

	public void showProgress() {
		showProgress("正在获取内容", "请稍候...");
	}

	public void dismissProgress() {

		mDialogCount--;
		VolleyLog.v("------------------» dismissProgress[%d]", mDialogCount);

		if (dialogShowing() || mDialog == null) {
			return;
		}

		try {
			mDialog.dismiss();
			mDialog = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean dialogShowing() {
		return mDialogCount > 0;
	}

	public void startCameraIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				"tmp_upload.jpg"));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

		startActivityForResult(intent, Preferences.REQUEST_CODE.TAKE_PHOTO);
	}

	public void startGalleryIntent() {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, Preferences.REQUEST_CODE.GET_PHOTO);
		} catch (Exception ex) {

		}
	}

	public void BuildImageDialog(Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("什么方式上传图片呢?");
		builder.setPositiveButton("拍照上传", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startCameraIntent();
			}
		});
		builder.setNegativeButton("相册选择", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startGalleryIntent();

			}
		});
		builder.show();

	}

	public void hideSoftInput(View v) {
		VolleyLog.d("hideSoftInput");
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(v.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// 打开软键盘
	public void openKeyboard() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

			}
		}, 200);
	}

	// 关闭软键盘
	public void closeKeyboard(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	// 获取密度
	public float getMetricsDensity() {
		final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.density;
	}

	public String getVersion() {
		String v = "";
		try {
			v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return v;
	}

	public String getVersionName(String pkgName) {
		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(pkgName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public int getVersionCode(String pkgName) {
		int versionCode = 0;
		try {
			versionCode = getPackageManager().getPackageInfo(pkgName, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
