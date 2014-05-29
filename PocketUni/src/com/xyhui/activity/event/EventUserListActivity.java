package com.xyhui.activity.event;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class EventUserListActivity extends FLActivity {
	private Button btn_back;
	private Button btn_search;

	private PullToRefreshListView event_user_listview;

	private String eventid;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.EVENTID)) {
			eventid = getIntent().getStringExtra(Params.INTENT_EXTRA.EVENTID);
		} else {
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_event_user_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_search = (Button) findViewById(R.id.btn_search);
		event_user_listview = (PullToRefreshListView) findViewById(R.id.event_user_listview);
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

		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开寻找活动
				Intent intent = new Intent();
				intent.putExtra("EVENT_ID", eventid);
				intent.setClass(mActivity, EventVoteSearchActivity.class);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public void ensureUi() {
		new EventUserList(event_user_listview, mActivity, eventid, null);
	}
}
