package com.xyhui.activity.group;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GroupDynamicListActivity extends FLActivity {

	private PullToRefreshListView weibo_listview;

	private GroupDynamicList mDynamicList;

	private Button btn_back;
	private Button btn_operate;
	private String groupIP;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_group_dynamic);
		groupIP = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);
		weibo_listview = (PullToRefreshListView) findViewById(R.id.weibo_listview);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_operate = (Button) findViewById(R.id.btn_operate);

	}

	@Override
	public void bindListener() {

		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn_operate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 添加新的
				// 动态
				Intent intent = new Intent(mActivity,
						GroupDynamicEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, groupIP);
				startActivity(intent);
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

		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.DYNAMICLIST);
		registerReceiver(mEvtReceiver, filter);
		selectWeiboByType();
	}

	public void selectWeiboByType() {

		weibo_listview.setVisibility(View.VISIBLE);
		mDynamicList = new GroupDynamicList(weibo_listview, mActivity,groupIP);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mDynamicList != null) {
				mDynamicList.refreshListViewStart();
			}
		}
	};
}
