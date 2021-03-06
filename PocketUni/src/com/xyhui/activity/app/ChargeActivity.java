package com.xyhui.activity.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.citicbank.cyberpay.aidl.ICyberPayRegister;
import com.citicbank.cyberpay.util.PayClient;
import com.mslibs.utils.MD5;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.WebViewActivity;
import com.xyhui.alipay.Keys;
import com.xyhui.alipay.Result;
import com.xyhui.alipay.Rsa;
import com.xyhui.utils.DownloadImpl;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class ChargeActivity extends FLActivity {
	private final String CCB_PAYMENT_GATEWAY = "https://ibsbjstar.ccb.com.cn/app/ccbMain";

	private final String MERCHANTID = "105320582990055";
	private final String POSID = "081276692";
	private final String BRANCHID = "322000000";
	private final String CURCODE = "01";
	private final String TXCODE = "520100";
	private final String TYPE = "1";
	private final String PUB = "2caac9b1de0f9a87b77748e1020111";
	private final String CLIENTIP = "58.210.175.67";
	private final String PROINFO = "PocketUni";

	private ImageButton mBtn10;
	private ImageButton mBtn50;
	private ImageButton mBtn100;
	private ImageButton mBtn200;

	private ImageButton mBtnCustom;
	private EditText mEditFee;

	private Button mBtnSubmit;
	private Button mBtnBack;

	private String mFee;

	/**
	 * 中信：商户编号
	 */
	private final String MERID = "MERID";

	/**
	 * 应当从服务器获取
	 */
	private String mMerID = "302305051920001";

	/**
	 * 中信：商户订单号
	 */
	private final String ORDERNO = "ORDERNO";

	/**
	 * 应当从服务器获取
	 */
	private String mOrderNo = "20130910094025";

	private final String CYBERPAY_PKG_NAME = "com.citicbank.cyberpay.ui";
	private final String CYBERPAY_APK_DOWNLOAD = "http://down.apk.hiapk.com/down?aid=2683557&em=13";

	/**
	 * 声明工具类对象
	 */
	private PayClient mMainPay = new PayClient(mActivity);

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_charge);

		mBtn10 = (ImageButton) findViewById(R.id.btn_10);
		mBtn50 = (ImageButton) findViewById(R.id.btn_50);
		mBtn100 = (ImageButton) findViewById(R.id.btn_100);
		mBtn200 = (ImageButton) findViewById(R.id.btn_200);

		mBtnCustom = (ImageButton) findViewById(R.id.btn_custom);
		mEditFee = (EditText) findViewById(R.id.edit_fee);

		mBtnSubmit = (Button) findViewById(R.id.btn_submit);
		mBtnBack = (Button) findViewById(R.id.btn_back);
	}

	@Override
	public void bindListener() {
		mBtn10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFee = "10";
				refreshBackground(mBtn10);
				mEditFee.setEnabled(false);
			}
		});

		mBtn50.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFee = "50";
				refreshBackground(mBtn50);
				mEditFee.setEnabled(false);
			}
		});

		mBtn100.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFee = "100";
				refreshBackground(mBtn100);
				mEditFee.setEnabled(false);
			}
		});

		mBtn200.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFee = "200";
				refreshBackground(mBtn200);
				mEditFee.setEnabled(false);
			}
		});

		mBtnCustom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditFee.setEnabled(true);
				refreshBackground(mBtnCustom);
				mEditFee.requestFocus();
				openKeyboard();
			}
		});

		mBtnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mEditFee.isEnabled()) {
					mFee = mEditFee.getText().toString();
				}

				Builder builder = new AlertDialog.Builder(mActivity);
				builder.setTitle("选择支付方式");

				builder.setItems(R.array.app_charge, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
						case 0:
							// 建行支付
							// Intent intent = new Intent(Intent.ACTION_VIEW);
							// intent.setData(Uri.parse(getCCBLink(
							// new PrefUtil().getPreference(Params.LOCAL.UID),
							// getOutTradeNo(), mFee)));
							// intent.addCategory(Intent.CATEGORY_DEFAULT);
							// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							// startActivity(intent);

							Intent intent = new Intent(mActivity, WebViewActivity.class);
							intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TITLE, "PU币充值");
							intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TYPE,
									WebViewActivity.TYPE_CHARGE);
							intent.putExtra(
									Params.INTENT_EXTRA.WEBVIEW_URL,
									getCCBLink(new PrefUtil().getPreference(Params.LOCAL.UID),
											getOutTradeNo(), mFee));
							startActivity(intent);
							break;
						case 1:
							// 支付宝支付
							startPay(new Product("PU币冲值", mFee, mFee));
							break;
						case 2:
							// 中信支付
							startCyberPay();
							break;
						case 3:
							dialog.cancel();
							break;
						}
					}
				}).show();
			}
		});

		mBtnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void ensureUi() {
		mBtn10.performClick();
	}

	/**
	 * 刷新金额选择图片
	 * 
	 * @param btn
	 */
	private void refreshBackground(ImageButton btn) {
		mBtn10.setImageResource(R.drawable.checkbox_n);
		mBtn50.setImageResource(R.drawable.checkbox_n);
		mBtn100.setImageResource(R.drawable.checkbox_n);
		mBtn200.setImageResource(R.drawable.checkbox_n);
		mBtnCustom.setImageResource(R.drawable.checkbox_n);
		btn.setImageResource(R.drawable.checkbox_o);
	}

	/**
	 * 中信支付(参数应该从服务器获取，而不是现在写死的)
	 */
	private void startCyberPay() {
		if (!PuApp.get().isInstalled(CYBERPAY_PKG_NAME)) {
			new AlertDialog.Builder(mActivity).setTitle("下载").setMessage("点击确定下载异度支付客户端")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							DownloadImpl download = new DownloadImpl(mActivity,
									CYBERPAY_APK_DOWNLOAD, "异度支付客户端", "CyberPay.apk");
							download.startDownload();
						}
					}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}
					}).setCancelable(false).show();
		} else {
			mMainPay.registerCallBack(mICyberPayListener);

			JSONObject json_data = new JSONObject();
			try {
				json_data.put(MERID, mMerID);
				json_data.put(ORDERNO, mOrderNo);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mMainPay.startPay(json_data.toString());
		}
	}

	// 客户端支付结果的回调函数
	private ICyberPayRegister mICyberPayListener = new ICyberPayRegister.Stub() {
		@Override
		public void payEnd(String data) throws RemoteException {
			if ("01".equals(data)) {
				// 支付成功
			} else if ("02".equals(data)) {
				// 支付失败
			} else if ("03".equals(data)) {
				// 支付取消
			} else if ("04".equals(data)) {
				// 未安装客户端
			}
			try {
				mMainPay.unregisterCallBack(mICyberPayListener);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * @return 跳转到建行支付页面的链接
	 */
	private String getCCBLink(String uid, String orderid, String payment) {
		String remark1 = uid;
		String remark2 = "";
		String gateway = "";
		String reginfo = uid;
		String referer = "";

		String beforeMd5 = String
				.format("MERCHANTID=%s&POSID=%s&BRANCHID=%s&ORDERID=%s&PAYMENT=%s&CURCODE=%s&TXCODE=%s&REMARK1=%s&REMARK2=%s&TYPE=%s&PUB=%s&GATEWAY=%s&CLIENTIP=%s&REGINFO=%s&PROINFO=%s&REFERER=%s",
						MERCHANTID, POSID, BRANCHID, orderid, payment, CURCODE, TXCODE, remark1,
						remark2, TYPE, PUB, gateway, CLIENTIP, reginfo, PROINFO, referer);
		String mac = MD5.MD5Encode(beforeMd5);
		return String.format("%s?%s&MAC=%s", CCB_PAYMENT_GATEWAY, beforeMd5, mac);
	}

	/**
	 * @return 支付宝订单信息
	 */
	private String getNewOrderInfo(Product product) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);

		sb.append("\"&out_trade_no=\"");
		sb.append(getOutTradeNo());

		sb.append("\"&subject=\"");
		sb.append(product.getSubject());

		sb.append("\"&body=\"");
		sb.append(product.getBody());

		sb.append("\"&total_fee=\"");
		sb.append(product.getPrice());

		sb.append("\"&notify_url=\"");
		// 网址需要做URL编码
		try {
			sb.append(URLEncoder.encode("http://pocketuni.net/addons/libs/alipay/notify_url.php",
					"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"utf-8");

		sb.append("\"&show_url=\"");
		try {
			sb.append(URLEncoder.encode("http://m.alipay.com", "UTF_8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		sb.append("\"&payment_type=\"1");

		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	/**
	 * @return 支付宝订单号
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();

		String key = format.format(date);
		key += new PrefUtil().getPreference(Params.LOCAL.UID);

		VolleyLog.d("outTradeNo: %s", key);
		return key;
	}

	/**
	 * @return 支付宝签名类型
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * 进入支付宝支付
	 * 
	 * @param product
	 */
	private void startPay(Product product) {
		try {
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(product);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign, "UTF-8");
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			VolleyLog.d("info = %s", info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(ChargeActivity.this, mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					VolleyLog.d("result = %s", result);

					Message.obtain(mHandler, 0, result).sendToTarget();
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(ChargeActivity.this, "请求远程服务失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 显示支付宝的支付结果
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Result result = new Result((String) msg.obj);
			Toast.makeText(ChargeActivity.this, result.getResult(), Toast.LENGTH_SHORT).show();
		};
	};

	class Product {
		private String mSubject;
		private String mBody;
		private String mPrice;

		public Product(String subject, String body, String price) {
			setSubject(subject);
			setBody(body);
			setPrice(price);
		}

		public String getSubject() {
			return mSubject;
		}

		public void setSubject(String mSubject) {
			this.mSubject = mSubject;
		}

		public String getBody() {
			return mBody;
		}

		public void setBody(String mBody) {
			this.mBody = mBody;
		}

		public String getPrice() {
			return mPrice;
		}

		public void setPrice(String mPrice) {
			this.mPrice = mPrice;
		}
	}
}