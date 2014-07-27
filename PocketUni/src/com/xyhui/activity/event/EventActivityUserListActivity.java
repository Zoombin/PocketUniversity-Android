package com.xyhui.activity.event;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class EventActivityUserListActivity extends FLActivity {
	private Button btn_back;

	private PullToRefreshListView event_activity_user_listview;

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
		setContentView(R.layout.activity_event_activity_user_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		event_activity_user_listview = (PullToRefreshListView) findViewById(R.id.event_activity_user_listview);
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
		new EventActivityUserList(event_activity_user_listview, mActivity, eventid);
	}
}
