package com.xyhui.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.loopj.android.http.RequestParams;
import com.mslibs.utils.VolleyLog;
import com.xyhui.types.MobileApp;
import com.xyhui.utils.FileUtil;
import com.xyhui.utils.LocalDataManager;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.utils.PuAppConfig;

public class PuApp extends Application {

	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
	public static final String BAIDU_MAP_KEY = "E219E7F630C5AFB461EA51235757BA528B0F00D8";

	private static PuApp mInstance;
	private PrefUtil mPrefUtil;
	private LocalDataManager mLocalDataMgr;
	private PuAppConfig mPuAppConfig;
	private BMapManager mBMapManager;

	@Override
	public void onCreate() {
		super.onCreate();
		VolleyLog.d("MainApplication onCreate");

		mInstance = this;

		if (checkForCrashOnLastRun(this)) {
			VolleyLog.d("Clearing cache due to crash on previous run.");
		}

		mPrefUtil = new PrefUtil();
		mLocalDataMgr = new LocalDataManager();
		mPuAppConfig = new PuAppConfig();
		mPuAppConfig.registReceiver();

		ShareSDK.initSDK(this);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		initMapManager();
	}

	public static PuApp get() {
		return mInstance;
	}

	public LocalDataManager getLocalDataMgr() {
		return mLocalDataMgr;
	}

	public BMapManager getBMapMgr() {
		return mBMapManager;
	}

	public void initMapManager() {
		if (null == mBMapManager) {
			mBMapManager = new BMapManager(this);
			mBMapManager.init(PuApp.BAIDU_MAP_KEY, new MyGeneralListener());
		}
	}

	public void cleanOnTerminate() {
		if (null != mBMapManager) {
			mBMapManager.destroy();
			mBMapManager = null;
		}

		mPuAppConfig.unregistReceiver();
		ShareSDK.stopSDK(this);
	}

	public Boolean installPackage(File mFile) {
		Uri uri = Uri.fromFile(mFile);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

		return true;
	}

	public boolean isInstalled(MobileApp app) {
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(app.mPkgName, 0);
		} catch (NameNotFoundException e) {
			VolleyLog.e(e, "");
			packageInfo = null;
		}
		if (packageInfo == null) {
			return false;
		}
		return true;
	}

	public boolean isInstalled(String pkgName) {
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(pkgName, 0);
		} catch (NameNotFoundException e) {
			VolleyLog.e(e, "");
			packageInfo = null;
		}
		if (packageInfo == null) {
			return false;
		}
		return true;
	}

	public RequestParams getToken() {
		RequestParams params = new RequestParams();
		String oauth_token = mPrefUtil.getPreference(Params.LOCAL.TOKEN);
		String oauth_token_secret = mPrefUtil.getPreference(Params.LOCAL.SECRET);
		if (!TextUtils.isEmpty(oauth_token) && !TextUtils.isEmpty(oauth_token_secret)) {
			params.put(OAUTH_TOKEN, oauth_token);
			params.put(OAUTH_TOKEN_SECRET, oauth_token_secret);
		}
		return params;
	}

	public boolean isLogon() {
		return !TextUtils.isEmpty(mPrefUtil.getPreference(Params.LOCAL.UID));
	}

	public void logout(Context context) {
		mPrefUtil.setPreference(Params.LOCAL.UID, null);
		mPrefUtil.setPreference(Params.LOCAL.MOBILE, null);
		mPrefUtil.setPreference(Params.LOCAL.ANNOUNCETIME, 0);

		PuApp.get().getLocalDataMgr().clearData();

		// JPushInterface.stopPush(this);

		// 进入登陆界面
		Intent intent = new Intent(context, LoadingActivity.class);
		intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		((Activity) context).finish();
	}

	// 返回除包名之外的全限定名称
	public String getFrontPage() {
		ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		ComponentName componentInfo = taskInfo.get(0).topActivity;
		VolleyLog.d("CURRENT Activity ::" + componentInfo.getShortClassName()
				+ "   Package Name :  " + componentInfo.getPackageName());
		return componentInfo.getShortClassName();
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(mInstance.getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG)
						.show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(mInstance.getApplicationContext(), "输入正确的检索条件！", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {

			}
		}
	}

	/**
	 * Checks whether this application was crashed on previous run.
	 * 
	 * @param context
	 * @return true if this application was crashed on previous run, false otherwise.
	 */
	private boolean checkForCrashOnLastRun(Context context) {
		File crashFile = new File(context.getCacheDir(), "crashed");
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(crashFile));
		return crashFile.delete();
	}

	/**
	 * The class used to do something when application crashed.
	 * 
	 * @author wuss
	 * @since 2012-5-13
	 * 
	 */
	class CrashHandler implements Thread.UncaughtExceptionHandler {

		private final File mFile;

		private final Thread.UncaughtExceptionHandler mOriginalHandler;

		public CrashHandler(File file) {
			mOriginalHandler = Thread.getDefaultUncaughtExceptionHandler();
			mFile = file;
		}

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			try {
				mFile.createNewFile();
			} catch (IOException e) {

			}

			StringWriter writer = new StringWriter();
			ex.printStackTrace(new PrintWriter(writer, true));

			// 把FC信息写到文件中
			writeToFile(writer);

			// 默认异常, 必须放在最后面(它之后的代码不会执行, I don't know why.)
			mOriginalHandler.uncaughtException(thread, ex);
		}

		/**
		 * write FC message to file
		 * 
		 * @param msg
		 *            Fc message
		 */
		private void writeToFile(StringWriter writer) {
			if (VolleyLog.LOG_ENABLED) {
				OutputStream os = null;
				try {
					String msg = writer.toString();

					File fcFile = new File(FileUtil.getFilename("log"));
					if (!fcFile.exists()) {
						fcFile.mkdirs();
					}
					os = new FileOutputStream(new File(fcFile, "fc.txt"), true);
					msg = DateFormat.getDateTimeInstance().format(new Date()) + "\r\n"
							+ msg.replace("\n", "\r\n") + "\r\n\r\n\r\n";
					os.write(msg.getBytes("utf-8"));
					os.flush();
				} catch (Exception e) {
				} finally {
					if (null != os) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
