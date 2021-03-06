package com.xyhui.activity.weibo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class WeiboTopicViewActivity extends FLActivity {
	private PullToRefreshListView weibo_listview;
	private WeiboList mWeiboListView;
	private Button btn_newweibo;
	private TextView navbar_title;

	private String topicName;

	@Override
	public void init() {
		topicName = getIntent().getStringExtra(Params.INTENT_EXTRA.WEIBO_TOPIC_ID);
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_weibotopic);

		weibo_listview = (PullToRefreshListView) this.findViewById(R.id.weibo_listview);
		btn_newweibo = (Button) this.findViewById(R.id.btn_newweibo);
		navbar_title = (TextView) this.findViewById(R.id.navbar_title);
	}

	@Override
	public void bindListener() {

		btn_newweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开发微博窗体
				Intent intent = new Intent();
				intent.setClass(mActivity, WeiboEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT, Params.INTENT_VALUE.WEIBO_NEW);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_TOPIC_ID, topicName);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public void ensureUi() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.WEIBOLIST);
		registerReceiver(mEvtReceiver, filter);

		mWeiboListView = new WeiboList(weibo_listview, mActivity, WeiboList.TOPIC);
		mWeiboListView.setTopicName(topicName);

		navbar_title.setText(topicName);
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
				if (mWeiboListView != null) {
					mWeiboListView.refreshListViewStart();
				}
			}
		}
	};
}
