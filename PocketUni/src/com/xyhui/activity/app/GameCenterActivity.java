package com.xyhui.activity.app;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Game;
import com.xyhui.utils.DownloadImpl;
import com.xyhui.utils.FileUtil;
import com.xyhui.utils.MySQLiteHelper;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

/**
 * @创建人： dengzh
 * @创建时间:2013-12-11 下午2:38:07
 * @版本 V1.0
 * @Copyright (c) 2013 by 苏州威博世网络科技有限公司.
 */
public class GameCenterActivity extends FLActivity {

	private final String INSTALLED_NO = "0";
	private final String INSTALLED_YES = "1";
	private final String URL_GAMECENTER = "http://pu.websharp.com.cn/mobile_android/index.aspx";
//	 private final String URL_GAMECENTER =
//	 "http://putest.websharp.com.cn/mobile_android/index.aspx";
	// private final String NEW_CENTURY_GAME_ID =
	// "ada01e61-5084-43de-9057-68b547b3d358";

	private Button btnRefresh;
	private Button btnBack;
	private WebView webView;

	private String mUrl;
	private MySQLiteHelper mMySQLite;
	private ArrayList<GameInfo> mGameList;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEBVIEW_URL)) {
			mUrl = getIntent().getStringExtra(Params.INTENT_EXTRA.WEBVIEW_URL);
			VolleyLog.d("get url: %s", mUrl);
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_game_center);

		btnBack = (Button) findViewById(R.id.btn_back);
		btnRefresh = (Button) findViewById(R.id.btn_refresh);
		webView = (WebView) findViewById(R.id.webView);
	}

	@Override
	public void bindListener() {
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Check if there's history
				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					finish();
				}
			}
		});

		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.loadUrl(webView.getUrl());
			}
		});
	}

	@Override
	public void ensureUi() {
		mMySQLite = new MySQLiteHelper(this);
		setWebView();

		showProgress();
		new Api(gameListCB, mActivity).getGameList();
	}

	private void loadUrl() {
		if (null != mUrl) {
			webView.loadUrl(mUrl);
		} else {
			PrefUtil pref = new PrefUtil();
			String token = pref.getPreference(Params.LOCAL.TOKEN);
			String secret = pref.getPreference(Params.LOCAL.SECRET);
			String uid = pref.getPreference(Params.LOCAL.UID);
			String url = String.format(URL_GAMECENTER
					+ "?studentID=%s&oauth_token=%s&oauth_token_secret=%s",
					uid, token, secret);
			webView.loadUrl(url);
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	private void setWebView() {
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAppCacheEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

		// WebView中用来跟页面交互js的类,其中的方法,可以在页面上通过Android.方法名(参数)来调用
		webView.addJavascriptInterface(new WebAppInterface(this), "Android");

		webView.setWebChromeClient(new WebChromeClient() {

		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// 开始
				VolleyLog
						.d("--------------------------onPageStarted--------------------------");
				btnRefresh.setEnabled(false);
				showProgress();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// 结束
				VolleyLog
						.d("--------------------------onPageFinished--------------------------");
				btnRefresh.setEnabled(true);
				dismissProgress();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				VolleyLog.d(url);
				view.loadUrl(url);
				return true;

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}

		// If it wasn't the Back key or there's no web page history, bubble up
		// to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * @创建人： dengzh
	 * @创建时间:2013-12-11 下午2:39:25
	 * @版本 V1.0
	 * @Copyright (c) 2013 by 苏州威博世网络科技有限公司.
	 */
	class WebAppInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		WebAppInterface(Context c) {
			mContext = c;
		}

		/**
		 * 得到已经安装的游戏的信息
		 * 
		 * @return 已安装游戏的信息
		 */
		@JavascriptInterface
		public String GetAppInstallInfo() {
			// 获取数据库中所有游戏(有过下载的:已安装\未安装)
			List<Game> gameList = mMySQLite.getAllGames();

			if (gameList.size() > 0) {
				String gameInfo = "";
				for (Game game : gameList) {
					if (PuApp.get().isInstalled(game.getPkgName())) {// 已安装
						// 更新已安装游戏在数据库中的状态为YES
						if (INSTALLED_NO.equals(game.getInstalled())) {
							game.setInstalled(INSTALLED_YES);
						}

						// 返回已安装游戏的信息
						gameInfo += game.toString();
					} else if (INSTALLED_YES.equals(game.getInstalled())) {// 未安装
						// 更新安装后又卸载的游戏在数据库中的状态为NO
						game.setInstalled(INSTALLED_NO);
					}

					mMySQLite.updateGame(game);
				}

				if (gameInfo.length() > 0) {
					return gameInfo.substring(0, gameInfo.length() - 1);
				}
			}

			// 如果没有任何信息,返回空
			return "";
		}

		/**
		 * 下载apk
		 * 
		 * @param apkUrl
		 * @param pkgName
		 * @param launchPath
		 * @param gameName
		 * @param gameUID
		 * @param iconUrl
		 */
		@JavascriptInterface
		public void downloadApp(final String apkUrl, final String pkgName,
				final String launchPath, final String gameName,
				final String gameUID, final String iconUrl, final String version) {
			String installed = INSTALLED_NO;
			Boolean isInstalled = PuApp.get().isInstalled(pkgName);

			if (isInstalled) {
				installed = INSTALLED_YES;
			}

			String apkFileName = pkgName + ".apk";

			String versionName = getVersionName(pkgName);

			if (isInstalled && version.equals(versionName)) {// 提示已下载
				new AlertDialog.Builder(mContext)
						.setTitle("游戏已下载")
						.setMessage("该游戏已安装,无需重复下载")
						.setPositiveButton("打开",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										openApplication(gameUID);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								}).setCancelable(true).show();
			} else {// 开始下载
				DownloadImpl download = new DownloadImpl(mContext, apkUrl,
						gameName, apkFileName);
				download.startDownload();
			}

			// 更新数据库中游戏相关信息
			mMySQLite.addOrUpdateGame(new Game(gameUID, gameName, pkgName,
					launchPath, installed, apkFileName, apkUrl, iconUrl,
					version));
		}

		/**
		 * 在这儿可以启动一个启用程序
		 * 
		 * @param gameUID
		 */
		@JavascriptInterface
		public void openApplication(final String gameUID) {
			PrefUtil prefUtil = new PrefUtil();
			String oauth_token = prefUtil.getPreference(Params.LOCAL.TOKEN);
			String oauth_token_secret = prefUtil
					.getPreference(Params.LOCAL.SECRET);

			Game game = mMySQLite.getGame(gameUID);

			if (!TextUtils.isEmpty(oauth_token)
					&& !TextUtils.isEmpty(oauth_token_secret)) {
				// 启动游戏
				Intent intent = new Intent();
				ComponentName cn = new ComponentName(game.getPkgName(),
						game.getLaunchPath());
				intent.setComponent(cn);

				// 按照token#tokensecret的格式将token信息写入sd卡指定位置,方便游戏读取
				FileUtil.writeContent(oauth_token + "#" + oauth_token_secret,
						"token.txt");
				intent.putExtra("token", oauth_token + "#" + oauth_token_secret);
				intent.setData(Uri
						.parse(oauth_token + "#" + oauth_token_secret));

				mContext.startActivity(intent);
			}
		}
	}

	CallBack gameListCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();

			mGameList = JSONUtils.fromJson(response,
					new TypeToken<ArrayList<GameInfo>>() {
					});

			if (null != mGameList && mGameList.size() > 0) {
				for (GameInfo gameInfo : mGameList) {
					Game game = mMySQLite.getGame(gameInfo.InnerID);

					String installed = INSTALLED_NO;
					String versionName = "";

					if (PuApp.get().isInstalled(gameInfo.PackageName)) {
						installed = INSTALLED_YES;
						versionName = getVersionName(gameInfo.PackageName);
					}

					if (null != game) {
						game.setInstalled(installed);
						game.setVersionName(versionName);
						mMySQLite.updateGame(game);
					} else {
						String apkFileName = gameInfo.PackageName + ".apk";
						// 更新数据库中游戏相关信息
						mMySQLite.addGame(new Game(gameInfo.InnerID,
								gameInfo.GameName, gameInfo.PackageName,
								gameInfo.OpenPath, installed, apkFileName,
								gameInfo.DownloadUrl, gameInfo.Img64,
								versionName));
					}
				}
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "没有获取游戏列表数据");
			}

			loadUrl();
		}

		@Override
		public void onFailure(String message) {
			loadUrl();
		}
	};

	class GameInfo {
		String InnerID;
		String GameName;
		String PackageName;
		String OpenPath;
		String Img64;
		String FilePath;
		String FileSize;
		String Version_Android;
		String DownloadUrl;
	}
}
