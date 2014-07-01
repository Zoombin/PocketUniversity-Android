package com.xyhui.activity.group;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GroupEventListActivity extends FLActivity {
	private Button btn_back;

	private PullToRefreshListView all_event_listview;
	private GroupEventList groupEventListView;

	private String groupIP;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_group_event_list);

		btn_back = (Button) findViewById(R.id.btn_back);

		all_event_listview = (PullToRefreshListView) findViewById(R.id.all_event_listview);

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
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void ensureUi() {
		groupIP = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);
		all_event_listview.setVisibility(View.VISIBLE);

		search();
	}

	void search() {
		groupEventListView = new GroupEventList(all_event_listview, mActivity,
				groupIP);
	}
}
