package com.xyhui.activity;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.more.PhoneBindingActivity;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class WebViewActivity extends FLActivity {

	public static final int TYPE_NORMAL_PAGE = 0;
	public static final int TYPE_IMAGE = 1;
	public static final int TYPE_TRAVEL_LOTTERY = 2;
	public static final int TYPE_CHARGE = 3;

	public static final int GET = 0;
	public static final int POST = 1;

	private Button btn_back;
	private Button btn_refresh;
	private TextView navbar_TitleText;

	private LinearLayout emptyUrl_layout;
	private LinearLayout loading_layout;
	private LinearLayout timeout_layout;

	private WebView mWebView;

	private String mTitle;
	private String mUrl;

	private int mType = TYPE_NORMAL_PAGE;

	private boolean isLoadingError = false;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_webview);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_refresh = (Button) findViewById(R.id.btn_refresh);
		navbar_TitleText = (TextView) findViewById(R.id.navbar_TitleText);

		mWebView = (WebView) findViewById(R.id.webView);

		emptyUrl_layout = (LinearLayout) findViewById(R.id.emptyUrl_layout);
		loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
		timeout_layout = (LinearLayout) findViewById(R.id.timeout_layout);
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Check if there's history
				if (mWebView.canGoBack() && TYPE_CHARGE != mType) {
					mWebView.goBack();
				} else {
					finish();
				}
			}
		});

		btn_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭返回
				isLoadingError = false;
				mWebView.reload();
			}
		});

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// 开始
				VolleyLog.d("--------------------------onPageStarted--------------------------");
				mWebView.removeAllViews();
				if (!dialogShowing() && TYPE_TRAVEL_LOTTERY != mType) {
					showProgress();
				}
				btn_refresh.setEnabled(false);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description,
					String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				// 加载失败
				VolleyLog.d("--------------------------onReceivedError--------------------------");
				isLoadingError = true;
				dismissProgress();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// 结束
				VolleyLog.d("--------------------------onPageFinished--------------------------");
				if (isLoadingError) {
					timeout_layout.setVisibility(View.VISIBLE);
				} else {
					timeout_layout.setVisibility(View.GONE);
				}

				if (TYPE_TRAVEL_LOTTERY != mType) {
					dismissProgress();
				}
				btn_refresh.setVisibility(View.VISIBLE);
				btn_refresh.setEnabled(true);
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
				builder.setMessage(message)
						.setNeutralButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.dismiss();
							}
						}).show();
				result.cancel();
				return true;
			}

			/**
			 * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
			 */
			@Override
			public boolean onJsConfirm(WebView view, String url, String message,
					final JsResult result) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
				builder.setTitle("lizongbo的Android webview测试confirm对话框").setMessage(message)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.confirm();
							}
						}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.cancel();
							}
						}).setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								result.cancel();
							}
						}).show();
				return true;
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void ensureUi() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEBVIEW_TITLE)) {
			mTitle = getIntent().getStringExtra(Params.INTENT_EXTRA.WEBVIEW_TITLE);
			navbar_TitleText.setText(mTitle);
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEBVIEW_URL)) {
			mUrl = getIntent().getStringExtra(Params.INTENT_EXTRA.WEBVIEW_URL);
			emptyUrl_layout.setVisibility(View.GONE);
			mWebView.setVisibility(View.VISIBLE);
		} else {
			emptyUrl_layout.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.GONE);
			return;
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEBVIEW_TYPE)) {
			mType = getIntent().getIntExtra(Params.INTENT_EXTRA.WEBVIEW_TYPE, TYPE_NORMAL_PAGE);
			if (TYPE_IMAGE == mType) {
				mWebView.setBackgroundColor(Color.BLACK);
				mWebView.setVerticalScrollBarEnabled(false);
				mWebView.setHorizontalScrollBarEnabled(false);
			}

			if (TYPE_TRAVEL_LOTTERY == mType) {
				new Api(ChanceCB, mActivity).lyCj(PuApp.get().getToken());
			}
		}

		loading_layout.setVisibility(View.GONE);
		timeout_layout.setVisibility(View.GONE);
		btn_refresh.setVisibility(View.GONE);
		btn_refresh.setEnabled(false);

		WebSettings webSet = mWebView.getSettings();
		webSet.setUseWideViewPort(true);
		webSet.setLoadWithOverviewMode(true);
		webSet.setJavaScriptEnabled(true);
		webSet.setJavaScriptCanOpenWindowsAutomatically(true);
		// 支持缩放
		webSet.setSupportZoom(true);
		// 使用内置缩放机制
		webSet.setBuiltInZoomControls(true);
		CookieManager.getInstance().setAcceptCookie(true);
		mWebView.setInitialScale(0);

		VolleyLog.d("--------------------------webView.loadUrl--------------------------");

		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEBVIEW_MECHANISM)
				&& POST == getIntent().getIntExtra(Params.INTENT_EXTRA.WEBVIEW_MECHANISM, GET)) {
			PrefUtil prefUtil = new PrefUtil();
			String postData = String.format("%s=%s&%s=%s", PuApp.OAUTH_TOKEN,
					prefUtil.getPreference(Params.LOCAL.TOKEN), PuApp.OAUTH_TOKEN_SECRET,
					prefUtil.getPreference(Params.LOCAL.SECRET));
			mWebView.postUrl(mUrl, EncodingUtils.getBytes(postData, "BASE64"));
		} else {
			mWebView.loadUrl(mUrl);
		}

		mWebView.requestFocus();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && TYPE_CHARGE == mType) {
			finish();
		} else if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			// Check if the key event was the Back button and if there's history
			mWebView.goBack();
			return true;
		}

		// If it wasn't the Back key or there's no web page history, bubble up to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}

	CallBack lyLuckCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			final Lottery lottery = JSONUtils.fromJson(response, Lottery.class);

			if (null != lottery) {
				if (!TextUtils.isEmpty(lottery.msg) && !TextUtils.isEmpty(lottery.img)) {

					LinearLayout linearLayout = new LinearLayout(WebViewActivity.this);

					final TextView text = new TextView(WebViewActivity.this);
					final ImageView image = new ImageView(WebViewActivity.this);

					int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
							getResources().getDisplayMetrics());
					int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
							getResources().getDisplayMetrics());

					LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(pixels,
							pixels);
					imageParams.setMargins(margin, margin, margin, margin);
					image.setLayoutParams(imageParams);

					LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					textParams.setMargins(margin, margin, margin, margin);

					text.setTextSize(18);
					text.setText(lottery.msg);
					text.setLayoutParams(textParams);

					linearLayout.setLayoutParams(textParams);
					linearLayout.setBackgroundColor(Color.WHITE);
					linearLayout.addView(image);
					linearLayout.addView(text);

					UrlImageViewHelper.setUrlDrawable(image, lottery.img, R.drawable.img_default,
							UrlImageViewHelper.CACHE_DURATION_THREE_DAYS, null);

					new AlertDialog.Builder(mActivity).setTitle("每日旅游抽奖").setView(linearLayout)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									if (1 == lottery.win) {
										String mobile = new PrefUtil()
												.getPreference(Params.LOCAL.MOBILE);
										if (TextUtils.isEmpty(mobile)) {
											new AlertDialog.Builder(mActivity)
													.setTitle("您还没有绑定手机, 现在去绑定吗?")
													.setPositiveButton("确定",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int whichButton) {
																	Intent intent = new Intent(
																			mActivity,
																			PhoneBindingActivity.class);
																	startActivity(intent);
																}
															})
													.setNegativeButton("取消",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int whichButton) {
																}
															}).show();
										}
									}
									dialog.dismiss();
								}
							}).setCancelable(false).show();
				}
			}
		}
	};

	CallBack ChanceCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			Chance chance = JSONUtils.fromJson(response, Chance.class);

			if (null != chance && 1 == chance.show) {
				new AlertDialog.Builder(mActivity).setTitle(chance.title).setMessage(chance.desc)
						.setPositiveButton("抽奖", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								new Api(lyLuckCB, mActivity).lyLucky(PuApp.get().getToken());
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).setCancelable(false).show();
			}
		}
	};

	class Chance {
		int show;
		String title;
		String desc;
	}

	class Lottery {
		int win;
		String msg;
		String img;
	}
}
