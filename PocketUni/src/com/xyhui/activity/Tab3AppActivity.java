package com.xyhui.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.xyhui.R;
import com.xyhui.activity.app.ChargeActivity;
import com.xyhui.activity.app.CourseListActivity;
import com.xyhui.activity.app.DonateListActivity;
import com.xyhui.activity.app.GameCenterActivity;
import com.xyhui.activity.app.MobileZoneListActivity;
import com.xyhui.activity.app.NoticeListActivity;
import com.xyhui.activity.app.ShakeActivity;
import com.xyhui.activity.app.TrainListActivity;
import com.xyhui.activity.app.WishHelpActivity;
import com.xyhui.activity.event.EventListActivity;
import com.xyhui.activity.group.GroupListActivity;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Banner;
import com.xyhui.types.MobileApp;
import com.xyhui.utils.DownloadImpl;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.AdBannerLayout;
import com.xyhui.widget.FLTabActivity;

public class Tab3AppActivity extends FLTabActivity {
	public final static String SUZHOU_CITY_ID = "1";
	public final static String VOLUNTEER_PKG_NAME = "com.lifeyoyo.volunteer.pu";
	public final static String MIAOPAI_DOWNLOAD_URL = "http://storage.video.sina.com.cn/apk/140226_wostore.apk?qq-pf-to=pcqq.c2c";
	public final static String MIAOPAI_PKG_NAME = "com.yixia.videoeditor";

	private ImageButton btn_message;
	private ImageButton btn_activity;
	private ImageButton btn_group;
	//private ImageButton btn_jqt;
	//private ImageButton btn_train;
	private ImageButton btn_travel;
	private ImageButton btn_volunteer;
	private ImageButton btn_course;
	private ImageButton btn_mobileZone;
	private ImageButton btn_shake;
	private ImageButton btn_wishhelp;
	private ImageButton btn_app_gamecenter;
	private ImageButton btn_app_charge;
	private ImageButton btn_app_qnh;
	private ImageButton btn_app_miaopai;
	private ImageButton btn_app_love;
	private ImageButton btn_app_dushuhu;

	private AdBannerLayout ad_banner;

	private TextView txt_volunteer;
	private TextView txt_operator_zone;
	private TextView txt_qnh;
	private TextView txt_dushuhu;

	private String mCityId;
	private String mSchoolId;
	private MobileApp mVolunteerApp;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_tab_app);

		btn_message = (ImageButton) findViewById(R.id.btn_app_xntz);
		btn_activity = (ImageButton) findViewById(R.id.btn_app_xyhd);
		btn_group = (ImageButton) findViewById(R.id.btn_app_st);
		//btn_jqt = (ImageButton) findViewById(R.id.btn_app_jqt);
		//btn_train = (ImageButton) findViewById(R.id.btn_app_train);
		btn_travel = (ImageButton) findViewById(R.id.btn_app_ly);
		btn_volunteer = (ImageButton) findViewById(R.id.btn_app_volunteer);
		btn_course = (ImageButton) findViewById(R.id.btn_course);
		btn_mobileZone = (ImageButton) findViewById(R.id.btn_mobilezone);
		btn_shake = (ImageButton) findViewById(R.id.btn_shake);
		btn_wishhelp = (ImageButton) findViewById(R.id.btn_wishhelp);
		btn_app_gamecenter = (ImageButton) findViewById(R.id.btn_app_gamecenter);
		btn_app_charge = (ImageButton) findViewById(R.id.btn_app_charge);
		btn_app_qnh = (ImageButton) findViewById(R.id.btn_app_qnh);
		btn_app_miaopai = (ImageButton) findViewById(R.id.btn_app_miaopai);
		btn_app_love = (ImageButton) findViewById(R.id.btn_app_love);
		btn_app_dushuhu = (ImageButton) findViewById(R.id.btn_app_dushuhu);

		ad_banner = (AdBannerLayout) findViewById(R.id.ad_banner);

		txt_volunteer = (TextView) findViewById(R.id.txt_app_volunteer);
		txt_operator_zone = (TextView) findViewById(R.id.text_operator_zone);
		txt_qnh = (TextView) findViewById(R.id.text_qnh);
		txt_dushuhu = (TextView) findViewById(R.id.txt_dushuhu);
		
	}

	@Override
	public void bindListener() {

		btn_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开校内通知
				Intent intent = new Intent(mActivity, NoticeListActivity.class);
				startActivity(intent);
			}
		});

		btn_activity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开校园活动
				Intent intent = new Intent(mActivity, EventListActivity.class);
				startActivity(intent);
			}
		});

		btn_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开部落
				Intent intent = new Intent(mActivity, GroupListActivity.class);
				startActivity(intent);
			}
		});

//		btn_jqt.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				final MobileApp app = new MobileApp("街区淘", null, R.drawable.app_icon_jqt,
//						"http://www.jiequtao.com/app/download", "com.bangqu.jiequtao");
//
//				// 打开街区淘
//				if (PuApp.get().isInstalled(app)) {
//					String packageName = app.mPkgName;
//					Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
//
//					if (mIntent != null) {
//						try {
//							String school = new PrefUtil().getPreference(Params.LOCAL.SCHOOLNAME);
//							mIntent.putExtra(Params.INTENT_EXTRA.LOCATION, school);
//							startActivity(mIntent);
//						} catch (ActivityNotFoundException err) {
//
//						}
//					}
//				} else {
//					new AlertDialog.Builder(mActivity).setTitle("下载")
//							.setMessage("此应用目前只适合在无锡地区使用，是否下载？")
//							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int whichButton) {
//									Uri uri = Uri.parse(app.mDownloadUrl);
//									Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//									startActivity(intent);
//									dialog.dismiss();
//								}
//							}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int whichButton) {
//									dialog.dismiss();
//								}
//							}).setCancelable(false).show();
//				}
//			}
//		});

//		btn_train.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// 打开培训
//				Intent intent = new Intent(mActivity, TrainListActivity.class);
//				startActivity(intent);
//			}
//		});
		
		btn_app_dushuhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final MobileApp app = new MobileApp(
						"独墅湖超市",
						null,
						R.drawable.app_icon_dushuhu,
						"http://dushuhu.me/redirect.php?source=pu_android",
						"me.dushuhu.android");
				
				// 打开志愿者打卡器
				if (PuApp.get().isInstalled(app)) {
					showProgress();
					launchApp(app);
				} else {
					new AlertDialog.Builder(mActivity).setTitle("下载").setMessage("点击确定开始下载")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.setData(Uri.parse("http://dushuhu.me/redirect.php?source=pu_android"));
									mActivity.startActivity(intent);
								}
							}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

								}
							}).setCancelable(false).show();
				}
			}
		});

		btn_travel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoTravel(WebViewActivity.TYPE_TRAVEL_LOTTERY);
			}
		});

		btn_volunteer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mVolunteerApp = new MobileApp("志愿者打卡器", null, R.drawable.app_icon_volunteer,
						"http://pu.dakaqi.cn/apk/pu_volunteer.apk", VOLUNTEER_PKG_NAME);

				// 打开志愿者打卡器
				if (PuApp.get().isInstalled(mVolunteerApp)) {
					showProgress();
					new Api(volunteerUpdateCB, Tab3AppActivity.this).getVolunteerUpdate();
				} else {
					new AlertDialog.Builder(mActivity).setTitle("下载").setMessage("点击确定开始下载")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									downloadApp(mVolunteerApp);
								}
							}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

								}
							}).setCancelable(false).show();
				}
			}
		});

		btn_course.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开课程
				Intent intent = new Intent(mActivity, CourseListActivity.class);
				startActivity(intent);
			}
		});

		btn_mobileZone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开运营商专区
				Intent intent = new Intent(mActivity, MobileZoneListActivity.class);
				startActivity(intent);
			}
		});

		btn_shake.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开摇一摇
				Intent intent = new Intent(mActivity, ShakeActivity.class);
				startActivity(intent);
			}
		});

		btn_wishhelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开许愿,求助
				Intent intent = new Intent(mActivity, WishHelpActivity.class);
				startActivity(intent);
			}
		});

		btn_app_gamecenter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开游戏中心
				Intent intent = new Intent(mActivity, GameCenterActivity.class);
				startActivity(intent);
			}
		});

		btn_app_charge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开冲值
				Intent intent = new Intent(mActivity, ChargeActivity.class);
				startActivity(intent);
			}
		});

		btn_app_qnh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final MobileApp app = new MobileApp(
						"江苏青年荟",
						null,
						R.drawable.app_icon_qnh,
						"http://www.apk.anzhi.com/data1/apk/201401/14/com.imohoo.gongqing_22667000.apk",
						"com.imohoo.gongqing");

				// 打开江苏青年荟
				if (PuApp.get().isInstalled(app)) {
					launchApp(app);
				} else {
					new AlertDialog.Builder(mActivity).setTitle("下载").setMessage("点击确定开始下载")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									downloadApp(app);
								}
							}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

								}
							}).setCancelable(false).show();
				}
			}
		});

		btn_app_miaopai.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final MobileApp app = new MobileApp("秒拍", null, R.drawable.app_icon_miaopai,
						MIAOPAI_DOWNLOAD_URL, MIAOPAI_PKG_NAME);

				// 打开秒拍
				if (PuApp.get().isInstalled(app)) {
					launchApp(app);
				} else {
					new AlertDialog.Builder(mActivity).setTitle("下载").setMessage("点击确定开始下载")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									downloadApp(app);
								}
							}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

								}
							}).setCancelable(false).show();
				}
			}
		});

		btn_app_love.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开爱心校园
				Intent intent = new Intent(mActivity, DonateListActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void ensureUi() {
		ad_banner.init(Banner.TYPE_APP);

		txt_volunteer.setText("志愿者打卡器");
		txt_volunteer.setSelected(true);

		txt_qnh.setText("江苏青年荟");
		txt_qnh.setSelected(true);

		mCityId = new PrefUtil().getPreference(Params.LOCAL.CITYID);
		mSchoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);

		if (mCityId.equals(SUZHOU_CITY_ID)) {
			txt_operator_zone.setText("移动专区");
		} else {
			txt_operator_zone.setText("运营商专区");
		}

		if(!mSchoolId.equals("473") && !mSchoolId.equals("472") && !mSchoolId.equals("62") && !mSchoolId.equals("4") && !mSchoolId.equals("393") && !mSchoolId.equals("393")) {
		   btn_app_dushuhu.setVisibility(4);
		   txt_dushuhu.setVisibility(4);
		}

		txt_operator_zone.setSelected(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ad_banner.reload();
	}

	@Override
	protected void onPause() {
		super.onPause();
		ad_banner.pause();
	}

	private void gotoTravel(int type) {
		// 打开旅游
		Intent intent = new Intent(mActivity, WebViewActivity.class);
		intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TITLE, "旅游");
		intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TYPE, type);
		intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, "http://wap.yikuaiqu.com?s=PocketUni");
		startActivity(intent);
	}

	private void launchApp(MobileApp app) {
		String packageName = app.mPkgName;
		Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);

		if (mIntent != null) {
			try {
				startActivity(mIntent);
			} catch (ActivityNotFoundException err) {

			}
		}
	}

	private void downloadApp(MobileApp app) {
		String appName = app.mAppName;
		String filename = appName + ".apk";

		DownloadImpl download = new DownloadImpl(mActivity, app.mDownloadUrl, appName, filename);
		download.startDownload();
	}

	CallBack volunteerUpdateCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			VolunteerVersionCode vvc = JSONUtils.fromJson(response, VolunteerVersionCode.class);
			if (null != vvc && vvc.code != 0) {
				// VolleyLog.d("Volunteer:%d", getVersionCode(VOLUNTEER_PKG_NAME));
				if (vvc.code > getVersionCode(VOLUNTEER_PKG_NAME)) {
					new AlertDialog.Builder(mActivity).setTitle("更新").setMessage("点击确定开始下载更新")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									downloadApp(mVolunteerApp);
								}
							}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

								}
							}).setCancelable(false).show();
				} else {
					launchApp(mVolunteerApp);
				}
			}
		}

		public void onFailure(String message) {
			dismissProgress();
			launchApp(mVolunteerApp);
		}
	};

	class VolunteerVersionCode {
		int code;
		Response res;

		class Response {
			String code;
			String desc;
		}
	}
}
