package com.xyhui.activity.event;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Response;
import com.xyhui.utils.MediaPlayerUtils;
import com.xyhui.utils.Params;
import com.xyhui.utils.ShakeDetector;
import com.xyhui.utils.ShakeDetector.OnShakeListener;
import com.xyhui.widget.FLActivity;

public class EventLuckyActivity extends FLActivity {
	private final int TIME_INTERVAL = 2500;

	private MediaPlayerUtils mMpUtils;

	private Button btn_back;
	private Button btn_reset;
	private ImageView lucky_bg;
	private AnimationDrawable mAnim;

	// 防止快速摇动时连续调用相关监听器的功能
	private boolean mShakeFinished = true;

	private String mEventId;
	private LuckyUser mEventLuckyUser;

	private ShakeDetector mShakeDetector;
	private OnShakeListener mOnShakeListener = new OnShakeListener() {

		@Override
		public void onShake() {
			if (mShakeFinished) {
				mShakeFinished = false;
				mMpUtils.reset();
				mMpUtils.play("shake.wav", true);
				mAnim.stop();
				mAnim.start();
				lucky_bg.postDelayed(new Runnable() {
					@Override
					public void run() {
						mMpUtils.reset();
						mAnim.stop();
						showProgress();
						new Api(eventLuckyCB, mActivity).eventLucky(PuApp.get().getToken(),
								mEventId);
					}
				}, TIME_INTERVAL);
			}
		}
	};

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.EVENTID)) {
			mEventId = getIntent().getStringExtra(Params.INTENT_EXTRA.EVENTID);
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_event_lucky);

		lucky_bg = (ImageView) findViewById(R.id.lucky_bg);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_reset = (Button) findViewById(R.id.btn_reset);
		mAnim = (AnimationDrawable) lucky_bg.getBackground();
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgress();
				new Api(eventResetCB, mActivity).eventReset(PuApp.get().getToken(), mEventId);
			}
		});
	}

	@Override
	public void ensureUi() {
		mShakeDetector = new ShakeDetector(mActivity);
		mShakeDetector.registerOnShakeListener(mOnShakeListener);
		mMpUtils = new MediaPlayerUtils(mActivity);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mShakeDetector.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mShakeDetector.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMpUtils.onStop();
		mShakeDetector.unregisterOnShakeListener(mOnShakeListener);
	}

	private void showGiftDialog(String luckyUserName) {
		mMpUtils.play("shake_match.wav", false);
		new AlertDialog.Builder(mActivity).setTitle("抽签结果").setMessage("您摇到了" + luckyUserName)
				.setPositiveButton("不过滤继续", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mShakeFinished = true;
					}
				}).setNegativeButton("过滤后继续", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showProgress();
						new Api(eventLuckyConfirmCB, mActivity).eventLuckyConfirm(PuApp.get()
								.getToken(), mEventLuckyUser.id);
						mShakeFinished = true;
					}
				}).setCancelable(false).show();
	}

	CallBack eventLuckyCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			mEventLuckyUser = JSONUtils.fromJson(response, LuckyUser.class);

			if (null != mEventLuckyUser) {
				if (mEventLuckyUser.status == 0) {
					mShakeFinished = true;
					NotificationsUtil.ToastBottomMsg(mActivity, "人已抽完");
				} else if (null != mEventLuckyUser && mEventLuckyUser.status == 1
						&& !TextUtils.isEmpty(mEventLuckyUser.realname)) {
					showGiftDialog(mEventLuckyUser.realname);
				}
			}
		}

		@Override
		public void onFailure(String response) {
			dismissProgress();
			NotificationsUtil.ToastBottomMsg(mActivity, "网络异常");
			mShakeFinished = true;
		}
	};

	CallBack eventLuckyConfirmCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			Response r = JSONUtils.fromJson(response, Response.class);

			if (null != r) {
				if (0 == r.status) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.msg);
				} else if (1 == r.status) {
					NotificationsUtil.ToastBottomMsg(mActivity, "成功过虑!");
				}
			}
		}

		@Override
		public void onFailure(String response) {
			dismissProgress();
			NotificationsUtil.ToastBottomMsg(mActivity, "网络异常");
		}
	};

	CallBack eventResetCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			Response r = JSONUtils.fromJson(response, Response.class);

			if (null != r) {
				if (0 == r.status) {
					NotificationsUtil.ToastBottomMsg(mActivity, "重置失败");
				} else if (1 == r.status) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.msg);
				}
			}
		}

		@Override
		public void onFailure(String response) {
			dismissProgress();
			NotificationsUtil.ToastBottomMsg(mActivity, "网络异常");
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!mShakeFinished) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	class LuckyUser {
		int status;
		String id;
		String realname;
	}
}
