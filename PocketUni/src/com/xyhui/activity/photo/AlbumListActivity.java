package com.xyhui.activity.photo;

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

public class AlbumListActivity extends FLActivity {

	private Button btn_back;

	private PullToRefreshListView photo_listview;
	private AlbumList mAlbumListView;

	private String user_id;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.USER_ID)) {
			user_id = getIntent().getStringExtra(Params.INTENT_EXTRA.USER_ID);
			VolleyLog.d("got userid:%s", user_id);
		}

		if (TextUtils.isEmpty(user_id)) {
			VolleyLog.d("no userid and user_name");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_photo_albumlist);

		btn_back = (Button) findViewById(R.id.btn_back);
		photo_listview = (PullToRefreshListView) findViewById(R.id.photo_listview);
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
		mAlbumListView = new AlbumList(photo_listview, mActivity, user_id);
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
				if (mAlbumListView != null) {
					mAlbumListView.refreshListViewStart();
				}
			}
		}
	};
}
