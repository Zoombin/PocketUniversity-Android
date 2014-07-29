package com.xyhui.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.City;
import com.xyhui.types.School;
import com.xyhui.types.Token;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class SigninActivity extends FLActivity {

	private final String CLIENT_ANDROID = "1";

	private Button btn_select_school;
//	private Button btn_select_city;
	private EditText edit_email;
	private EditText edit_password;
	private Button btn_signin;

	private PrefUtil mPrefUtil;
	/**
	 * 界面跳转的重试时间（防止要用到的数据未加载完全）
	 */
	private final int RETRY_DURATION = 500;
	private School mSchool;
//	private City mCity;
	/**
	 * 判断当前activity是否已销毁
	 */
	private boolean mFinished;

	private Token mToken;

	@Override
	public void init() {
		mPrefUtil = new PrefUtil();
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_signin);

		btn_select_school = (Button) findViewById(R.id.btn_select_school);
//		btn_select_city = (Button) findViewById(R.id.btn_select_city);
		edit_email = (EditText) findViewById(R.id.edit_email);
		edit_password = (EditText) findViewById(R.id.edit_password);
		btn_signin = (Button) findViewById(R.id.btn_signin);
	}

	@Override
	public void bindListener() {
//		btn_select_city.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				closeKeyboard(edit_email);
//				// 打开城市列表
//				Intent intent = new Intent();
//				intent.setClass(mActivity, CityListActivity.class);
//				startActivity(intent);
//			}
//		});

		btn_select_school.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (null == mCity) {
//					NotificationsUtil.ToastTopMsg(mActivity, "请先选择城市");
//					return;
//				}

				closeKeyboard(edit_email);
				// 打开学校列表
				Intent intent = new Intent();
				intent.setClass(mActivity, SchoolListActivity.class);
				startActivity(intent);
			}
		});

		btn_signin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				// 提交登录
//				if (mCity == null) {
//					NotificationsUtil.ToastBottomMsg(mActivity, "请选择城市");
//					return;
//				}

				if (mSchool == null) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请选择学校");
					return;
				}

 				mPrefUtil.setPreference(Params.LOCAL.CITYID, mSchool.cityId);
//				mPrefUtil.setPreference(Params.LOCAL.CITYNAME, city.city);
				
				String email = edit_email.getText().toString().trim();
				String password = edit_password.getText().toString().trim();

				if (TextUtils.isEmpty(email)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请填写学号");
					return;
				}

				if (TextUtils.isEmpty(password)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请填写登录密码");
					return;
				}

				setSignEnable(false);
				email = email + mSchool.email;
				new Api(loginCB, mActivity).login(email, password, CLIENT_ANDROID);
				closeKeyboard(edit_email);
			}
		});
	}

	@Override
	public void ensureUi() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.user.SCHOOL");
		filter.addAction("android.user.CITY");
		VolleyLog.v("Receiver Registed");
		registerReceiver(mEvtReceiver, filter);
		registerReceiver(mCityReceiver, filter);

//		String cityName = mPrefUtil.getPreference(Params.LOCAL.CITYNAME);
//		String cityId = mPrefUtil.getPreference(Params.LOCAL.CITYID);
//		if (null != cityName && null != cityId) {
//			mCity = new City(cityId, cityName);
//			btn_select_city.setText(mCity.city);
//		}

		String schoolName = mPrefUtil.getPreference(Params.LOCAL.SCHOOLNAME);
		String schoolId = mPrefUtil.getPreference(Params.LOCAL.SCHOOLID);
		String schoolEmail = mPrefUtil.getPreference(Params.LOCAL.SCHOOLEMAIL);
		if (null != schoolName && null != schoolId && null != schoolEmail) {
			mSchool = new School();
			mSchool.name = schoolName;
			mSchool.school = schoolId;
			mSchool.email = schoolEmail;

			btn_select_school.setText(mSchool.name);
		}

		String uName = mPrefUtil.getPreference(Params.LOCAL.UNAME);
		if (null != uName) {
			edit_email.setText(uName);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setSignEnable(true);
	}

	private void setSignEnable(boolean b) {
		if (b) {
			btn_signin.setEnabled(true);
			btn_signin.setText("登陆PocketUni");
		} else {
			btn_signin.setEnabled(false);
			btn_signin.setText("正在登陆PocketUni...");
		}
	}

	private void testOneSchool() {
		ArrayList<School> schools = PuApp.get().getLocalDataMgr().getSchools();

		if (schools != null && !schools.isEmpty()) {
			schools.remove(0);
			ArrayList<School> citySchools = new ArrayList<School>();
			String cityId = mPrefUtil.getPreference(Params.LOCAL.CITYID);

			for (int i = 0; i < schools.size(); i++) {
				School school = schools.get(i);
				String schoolCityId = school.cityId;

				if (cityId.equals(schoolCityId) && citySchools.size() < 2) {
					citySchools.add(school);
				} else if (citySchools.size() >= 2) {
					break;
				}
			}

			if (citySchools.size() == 1) {
				mSchool = citySchools.get(0);
				btn_select_school.setText(mSchool.name);
				mPrefUtil.setPreference(Params.LOCAL.SCHOOLID, mSchool.school);
			} else {
				btn_select_school.setText("选择所在学校");
			}
		}
	}

	private void setAliasAndTags(final String alias, final Set<String> tags) {
		if (!mFinished) {
			JPushInterface.setAliasAndTags(PuApp.get(), alias, tags, new TagAliasCallback() {
				@Override
				public void gotResult(int arg0, String arg1, Set<String> arg2) {
					VolleyLog.d("ResultCode: %d Alias: %s Tags: %s", arg0, arg1, arg2.toString());
					if (Params.JPush.STATE_OK == arg0) {
						initOrMain();
					} else {
						NotificationsUtil.ToastBottomMsg(mActivity, "推送服务相关注册失败，请检查您的网络连接");
						if (Params.JPush.STATE_TIMEOUT == arg0 && !mFinished) {
							btn_select_school.postDelayed(new Runnable() {
								@Override
								public void run() {
									setAliasAndTags(alias, tags);
								}
							}, 3000);
						}
					}
				}
			});
		}
	}

	CallBack loginCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			mToken = JSONUtils.fromJson(response, Token.class);
			if (null != mToken && !mToken.isEmpty()) {
				mPrefUtil.setPreference(Params.LOCAL.UID, mToken.uid);
				mPrefUtil.setPreference(Params.LOCAL.TOKEN, mToken.oauth_token);
				mPrefUtil.setPreference(Params.LOCAL.SECRET, mToken.oauth_token_secret);
				mPrefUtil
						.setPreference(Params.LOCAL.UNAME, edit_email.getText().toString().trim());
				mPrefUtil.setPreference(Params.LOCAL.NICKNAME, mToken.uname);
				mPrefUtil.setPreference(Params.LOCAL.DEPARTID, mToken.sid1);
				mPrefUtil.setPreference(Params.LOCAL.DEPARTNAME, mToken.sid1Name);

				// JPushInterface.resumePush(PuApp.get());

				// 标签有长度限制：城市用中文名称；有的学校中文名称太长，改用学校id
				Set<String> tags = new HashSet<String>();
//				tags.add(mCity.city);
				tags.add(mSchool.school);
				tags.add(mToken.sid1);
				// 避免某个tag无效导致调用失败，过滤出有效tag
				tags = JPushInterface.filterValidTags(tags);
				setAliasAndTags(mToken.uid, tags);

				PuApp.get().getLocalDataMgr().setEventCats(mActivity);
				PuApp.get().getLocalDataMgr().setGroupCats(mActivity);
				// PuApp.get().getLocalDataMgr().setCourseCats(mActivity);
				PuApp.get().getLocalDataMgr().setUserData(mActivity);
			} else {
				setSignEnable(true);
				NotificationsUtil.ToastBottomMsg(mActivity, "服务器返回错误");
			}
		}

		@Override
		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, message);
			setSignEnable(true);
		}
	};

	private void initOrMain() {
		btn_signin.postDelayed(new Runnable() {
			public void run() {
				VolleyLog.d(System.currentTimeMillis() / 1000 + "");
				if (PuApp.get().getLocalDataMgr().allDataArrived()) {
					Intent intent = new Intent();
					if (!PuApp.get().getLocalDataMgr().isVerified()) {
						// 未验证
						intent.setClass(mActivity, AccountBindingActivity.class);
					} else if (!PuApp.get().getLocalDataMgr().isInited()) {
						// 未初始化
						intent.setClass(mActivity, UserInitActivity.class);
					} else {
						// 进入主程序
						// intent.setClass(mActivity, NewFeatureActivity.class);
						intent.setClass(mActivity, MainActivity.class);
					}
					startActivity(intent);
					finish();
				} else {
					initOrMain();
				}
			}
		}, RETRY_DURATION);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.user.SCHOOL")) {
				if (intent.hasExtra(Params.INTENT_EXTRA.SCHOOL)) {
					mSchool = intent.getParcelableExtra(Params.INTENT_EXTRA.SCHOOL);
					btn_select_school.setText(mSchool.name);
				}
			}
		}
	};

	public BroadcastReceiver mCityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.user.CITY")) {
				if (intent.hasExtra(Params.INTENT_EXTRA.CITY)) {
//					mCity = intent.getParcelableExtra(Params.INTENT_EXTRA.CITY);
//					btn_select_city.setText(mCity.city);

					testOneSchool();
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
		unregisterReceiver(mCityReceiver);
		mFinished = true;
		VolleyLog.d("Receiver UnRegisted:");
	}
}
