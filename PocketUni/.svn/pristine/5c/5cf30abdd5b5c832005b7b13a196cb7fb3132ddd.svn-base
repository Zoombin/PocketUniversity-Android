package com.xyhui.activity.weibo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class UserHomePageActivity extends FLActivity {
	private Button btn_back;

	private PullToRefreshListView user_view_listview;
	private UserHomePageList mUserViewListView;

	private String user_id;
	private String user_name;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.USER_ID)) {
			user_id = getIntent().getStringExtra(Params.INTENT_EXTRA.USER_ID);
			VolleyLog.d("got userid:%s", user_id);
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.USERNAME)) {
			user_name = getIntent().getStringExtra(Params.INTENT_EXTRA.USERNAME);
			user_name = user_name.replace("@", "");
			VolleyLog.d("got user_name:%s", user_name);
		}

		if (TextUtils.isEmpty(user_id) && TextUtils.isEmpty(user_name)) {
			VolleyLog.d("no userid and user_name");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_user_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		user_view_listview = (PullToRefreshListView) findViewById(R.id.user_view_listview);
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭返回
				finish();
			}
		});
	}

	@Override
	public void ensureUi() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.WEIBOLIST);
		registerReceiver(mEvtReceiver, filter);
		mUserViewListView = new UserHomePageList(user_view_listview, mActivity, user_id, user_name);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Params.INTENT_ACTION.WEIBOLIST)) {
				if (mUserViewListView != null) {
					mUserViewListView.refreshListViewStart();
				}
			}
		}
	};
}
