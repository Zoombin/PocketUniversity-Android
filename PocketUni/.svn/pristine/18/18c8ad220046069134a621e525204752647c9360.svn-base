package com.xyhui.widget;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CRelativeLayout;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.AdBanner;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;

public class AdBannerLayout extends CRelativeLayout {

	private ViewPager mViewPager;
	private Button btn_clear;

	private BannerPagerAdapter mBannerPagerAdapter;
	private ArrayList<? extends Object> mDataList;
	private Timer mTimer;
	private TimerTask mTask;
	private String mSchoolId;
	private String mPlace;

	public AdBannerLayout(Context context) {
		super(context);
	}

	public AdBannerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setContentView(R.layout.widget_ad_layout);
	}

	public AdBannerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void linkUiVar() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		btn_clear = (Button) findViewById(R.id.btn_clear);
	}

	private void bindListener() {
		mViewPager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mViewPager.getParent().requestDisallowInterceptTouchEvent(true);

				if (MotionEvent.ACTION_UP == event.getAction()) {
					reload();
				}

				if (MotionEvent.ACTION_MOVE == event.getAction()) {
					pause();
				}

				return false;
			}
		});

		btn_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setVisibility(GONE);
				pause();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (inited()) {
					int index = mViewPager.getCurrentItem();
					VolleyLog.d("getCurrentItem(index: %d)", index);
					// 如果当前是最大位置，跳转到第一个位置
					if (++index >= mDataList.size()) {
						index = 0;
					}
					mViewPager.setCurrentItem(index, true);
				}
				break;
			}
		}
	};

	private CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			mDataList = JSONUtils.fromJson(response, new TypeToken<ArrayList<AdBanner>>() {
			});

			// 初始化适配器
			mBannerPagerAdapter = new BannerPagerAdapter(getContext());
			mBannerPagerAdapter.setDataList(mDataList);
			mViewPager.setAdapter(mBannerPagerAdapter);

			if (inited()) {
				setVisibility(VISIBLE);
			} else {
				setVisibility(GONE);
			}

			reload();
		}
	};

	public void init(String place) {
		linkUiVar();
		bindListener();

		// 加载完广告数据之前，默认不显示
		setVisibility(GONE);

		mPlace = place;
		mSchoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);
		new Api(callback, getContext()).adList(PuApp.get().getToken(), mSchoolId, mPlace);
	}

	public boolean inited() {
		return (null != mDataList && mDataList.size() > 0);
	}

	public void reload() {
		if (GONE == getVisibility() && inited()) {
			init(mPlace);
		}
		if (mTimer == null && mTask == null) {
			if (inited()) {
				mTimer = new Timer();

				mTask = new TimerTask() {
					public void run() {
						Message message = new Message();
						message.what = 1;
						mHandler.sendMessage(message);
					}
				};

				mTimer.schedule(mTask, 3000, 3000);
			}
		}
	}

	public void pause() {
		if (mTimer != null && mTask != null) {
			mTask.cancel();
			mTimer.cancel();
			mTask = null;
			mTimer = null;
		}
	}
}
