package com.xyhui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.xyhui.R;
import com.xyhui.activity.app.NavMapListActivity;
import com.xyhui.activity.app.NoticeListActivity;
import com.xyhui.activity.event.EventListActivity;
import com.xyhui.activity.group.GroupListActivity;
import com.xyhui.activity.more.AccountManageActivity;
import com.xyhui.activity.weibo.MessageActivity;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.EventBannerLayout;
import com.xyhui.widget.FLTabActivity;

public class Tab1HomeActivity extends FLTabActivity {

	private EventBannerLayout index_banner;

	private LinearLayout layout_notice;
	private LinearLayout layout_group;
	private LinearLayout layout_searchfriend;
	private LinearLayout layout_event;
	private LinearLayout layout_navigation;
	private LinearLayout layout_accountsetting;

	private TextView text_groupcount;
	private TextView text_announcecount;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_tab_index);

		index_banner = (EventBannerLayout) findViewById(R.id.index_banner);
		layout_notice = (LinearLayout) findViewById(R.id.layout_notice);
		layout_group = (LinearLayout) findViewById(R.id.layout_group);
		layout_searchfriend = (LinearLayout) findViewById(R.id.layout_searchfriend);
		layout_event = (LinearLayout) findViewById(R.id.layout_event);
		layout_navigation = (LinearLayout) findViewById(R.id.layout_navigation);
		layout_accountsetting = (LinearLayout) findViewById(R.id.layout_accountsetting);
		text_groupcount = (TextView) findViewById(R.id.text_groupcount);
		text_announcecount = (TextView) findViewById(R.id.text_announcecount);
	}

	@Override
	public void bindListener() {
		layout_notice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开校内通知
				Intent intent = new Intent(mActivity, NoticeListActivity.class);
				startActivity(intent);
			}
		});

		layout_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开校园活动
				Intent intent = new Intent(mActivity, EventListActivity.class);
				startActivity(intent);
			}
		});
		layout_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开部落
				Intent intent = new Intent(mActivity, GroupListActivity.class);
				startActivity(intent);
			}
		});
		layout_navigation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开校内导航
				Intent intent = new Intent(mActivity, NavMapListActivity.class);
				startActivity(intent);
			}
		});

		layout_searchfriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开消息
				Intent intent = new Intent(mActivity, MessageActivity.class);
				startActivity(intent);
			}
		});

		layout_accountsetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 账号管理
				Intent intent = new Intent(mActivity, AccountManageActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void ensureUi() {
		index_banner.init();
	}

	@Override
	protected void onResume() {
		super.onResume();

		String schoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);
		if (TextUtils.isEmpty(schoolId)) {
			schoolId = "1";
		}
		long time = new PrefUtil().getLongPreference(Params.LOCAL.ANNOUNCETIME);
		if (time <= 0) {
			time = 1;
		}
		new Api(callback, mActivity).userHomeInfo(PuApp.get().getToken(), schoolId, time);

		index_banner.reload();
	}

	@Override
	protected void onPause() {
		super.onPause();
		index_banner.pause();
	}

	private CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			LoginInfo loginInfo = JSONUtils.fromJson(response, LoginInfo.class);

			if (null != loginInfo) {
				if (!TextUtils.isEmpty(loginInfo.group)) {
					text_groupcount.setText(String.format("已加入%s个部落", loginInfo.group));
				}

				if (!TextUtils.isEmpty(loginInfo.mobile)) {
					new PrefUtil().setPreference(Params.LOCAL.MOBILE, loginInfo.mobile);
				} else {
					new PrefUtil().setPreference(Params.LOCAL.MOBILE, null);
				}

				if (!TextUtils.isEmpty(loginInfo.announce)) {
					text_announcecount.setText(String.format("有%s条新的通知", loginInfo.announce));
				}
			}
		}
	};

	static class LoginInfo {
		String announce;
		String group;
		String year;
		String mobile;
	}
}
