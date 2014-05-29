package com.xyhui.activity.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.weibo.UserHomePageActivity;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.utils.MediaPlayerUtils;
import com.xyhui.utils.Params;
import com.xyhui.utils.ShakeDetector;
import com.xyhui.utils.ShakeDetector.OnShakeListener;
import com.xyhui.utils.ShareSDKUtil;
import com.xyhui.widget.FLActivity;

public class ShakeActivity extends FLActivity {
	private final int NO_FREE_TIMES = 0;
	private final int ITEM_NOTHING = 1;
	private final int ITEM_PU_COIN = 2;
	private final int ITEM_PEOPLE = 3;

	private String mTitle;
	private String mMessage;

	private final int TIME_INTERVAL = 1500;
	private MediaPlayerUtils mMpUtils;

	private TextView tv_left_times;
	private ImageView shake_bg;
	private Button btn_back;

	// 用来标记一次摇晃的结束,默认为已经结束
	private boolean mShakeFinished = true;

	private ShakedData mShakedData;
	private ShakeData mShakeData;

	private ShakeDetector mShakeDetector;

	private AnimationDrawable mAnim;

	private OnShakeListener mOnShakeListener = new OnShakeListener() {

		@Override
		public void onShake() {
			if (mShakeFinished) {
				mShakeFinished = false;
				mMpUtils.reset();
				mMpUtils.play("shake.wav", false);
				mAnim.start();
				shake_bg.postDelayed(new Runnable() {
					@Override
					public void run() {
						mMpUtils.reset();
						mAnim.stop();
						showProgress();
						new Api(shakedCB, mActivity).shakeYY(PuApp.get().getToken());
					}
				}, TIME_INTERVAL);
			}
		}
	};

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_app_shake);

		tv_left_times = (TextView) findViewById(R.id.tv_left_times);
		shake_bg = (ImageView) findViewById(R.id.shake_bg);
		btn_back = (Button) findViewById(R.id.btn_back);
		mAnim = (AnimationDrawable) shake_bg.getBackground();
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
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
		// 如果没处理完当前的摇晃就进入了其他页面,回来的时候应该接着处理,而不是执行下面的代码
		if (mShakeFinished) {
			new Api(shakeCB, mActivity).shakeStatus(PuApp.get().getToken());
			mShakeFinished = false;
		}

		try {
			mShakeDetector.start();
		} catch (UnsupportedOperationException e) {
			NotificationsUtil.ToastBottomMsg(mActivity, "请重新进入");
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mShakeDetector.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mShakeDetector.unregisterOnShakeListener(mOnShakeListener);
		mMpUtils.onStop();
	}

	CallBack shakeCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			tv_left_times.setVisibility(View.VISIBLE);
			mShakeData = JSONUtils.fromJson(response, ShakeData.class);

			if (null != mShakeData) {
				tv_left_times.setText(String.format("今日剩余次数：%d", mShakeData.rest));
				if (mShakeData.rest > 0 && mShakeData.cost > 0) {
					showGiftDialog(0, mShakeData.cost);
				} else {
					mShakeFinished = true;
				}
			}
		}

		@Override
		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "网络异常");
			mShakeFinished = true;
		}
	};

	CallBack shakedCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();

			mShakedData = JSONUtils.fromJson(response, ShakedData.class);

			if (null != mShakedData) {
				if (0 == mShakedData.status) {
					NotificationsUtil.ToastBottomMsg(mActivity, mShakedData.msg);
					mShakeFinished = true;
				} else if (1 == mShakedData.status) {
					tv_left_times.setText(String.format("今日剩余次数：%d", mShakedData.rsp.rest));
					showGiftDialog(mShakedData.data.type, 0);
				}
			}
		}

		@Override
		public void onFailure(String message) {
			dismissProgress();
			NotificationsUtil.ToastBottomMsg(mActivity, "网络异常");
			mShakeFinished = true;
		}
	};

	private void showGiftDialog(final int type, int nextCost) {
		if (NO_FREE_TIMES != type) {
			mMpUtils.play("shake_match.wav", false);
		}

		switch (type) {
		case NO_FREE_TIMES:
			mTitle = "注意";
			mMessage = String.format("今天的免费摇奖次数已经用完了，如果继续,系统将会扣除你%.2f个PU币，继续吗？", nextCost / 100.0);
			break;
		case ITEM_NOTHING:
			mTitle = "再加把劲吧！";
			mMessage = "这次啥都没摇到";
			break;
		case ITEM_PU_COIN:
			mTitle = "恭喜!";
			mMessage = String.format("摇到了%.2f个PU币", mShakedData.data.win / 100.0);
			break;
		case ITEM_PEOPLE:
			mTitle = "运气不错哦！";
			mMessage = String.format("摇到了一个来自%s的%s生", mShakedData.data.school,
					mShakedData.data.sex);
			break;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity).setTitle(mTitle)
				.setMessage(mMessage).setNegativeButton("确定", new OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 上次对话框不是提示无免费剩余次数,可以继续摇,下次需要花pu币
						if (NO_FREE_TIMES != type && null != mShakedData
								&& null != mShakedData.rsp && mShakedData.rsp.cost > 0
								&& mShakedData.rsp.rest > 0) {
							showGiftDialog(NO_FREE_TIMES, mShakedData.rsp.cost);
						} else {
							mShakeFinished = true;
						}
					}
				}).setCancelable(false);

		if (ITEM_PU_COIN == type) {
			builder.setPositiveButton("分享", new OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					mShakeFinished = true;
					String content = "我在PocketUni上" + mMessage + ",快来下载体验吧:http://pocketuni.net/";
					ShareSDKUtil.showShare(mActivity, false, content, null, null);
				}
			});
		} else if (ITEM_PEOPLE == type) {
			builder.setPositiveButton("去看看", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mShakeFinished = true;
					// 打开个人主页
					Intent intent = new Intent(mActivity, UserHomePageActivity.class);
					intent.putExtra(Params.INTENT_EXTRA.USER_ID, mShakedData.data.win + "");
					startActivity(intent);
				}
			});
		} else if (NO_FREE_TIMES == type) {
			builder.setPositiveButton("退出", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mShakeFinished = true;
					finish();
				}
			});
		}

		builder.create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!mShakeFinished) {
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	class ShakedData {
		int status;
		String msg;
		Gift data;
		ShakeData rsp;

		class Gift {
			int type;
			int win;
			String realname;
			String school;
			String sex;
		}
	}

	class ShakeData {
		int rest;// 剩余次数，
		int times;// 当前次数
		int cost;// 费用
	}
}
