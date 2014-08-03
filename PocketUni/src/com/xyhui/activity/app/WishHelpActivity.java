package com.xyhui.activity.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.activity.weibo.WeiboEditActivity;
import com.xyhui.activity.weibo.WeiboList;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class WishHelpActivity extends FLActivity {

	private final String WISH_WALL = "许愿墙";
	private final String HELP_WALL = "求助墙";
	private String mCurrentWall = WISH_WALL;

	private PullToRefreshListView weibo_listview;

	private WeiboList mWeiboListView;

	private Button btn_newweibo;
	private Button btn_wish;
	private Button btn_help;

	private Button btn_back;
	private Button btn_my_weibo;
	private Button btn_all_weibo;

	private final int TOPIC_WISH = 0;
	private final int TOPIC_HELP = 1;

	private final int TOPIC_WISH_MINE = 0;
	private final int TOPIC_WISH_ALL = 1;

	private final int TOPIC_HELP_MINE = 2;
	private final int TOPIC_HELP_ALL = 3;

	private int showType = TOPIC_WISH;
	private int subShowType = TOPIC_WISH_ALL;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_topic_wishhelp);

		weibo_listview = (PullToRefreshListView) this.findViewById(R.id.weibo_listview);
		btn_newweibo = (Button) this.findViewById(R.id.btn_newweibo);
		btn_wish = (Button) this.findViewById(R.id.btn_wish);
		btn_help = (Button) this.findViewById(R.id.btn_help);

		btn_back = (Button) findViewById(R.id.btn_back); 
		btn_my_weibo = (Button) findViewById(R.id.btn_my_weibo);
		btn_all_weibo = (Button) findViewById(R.id.btn_all_weibo);
	}

	@Override
	public void bindListener() {

		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		btn_newweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开发微博窗体
				Intent intent = new Intent();
				intent.setClass(mActivity, WeiboEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT, Params.INTENT_VALUE.WEIBO_NEW);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_TOPIC_ID, mCurrentWall);
				mActivity.startActivity(intent);
			}
		});

		btn_wish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 许愿列表
				showType = TOPIC_WISH;
				subShowType = TOPIC_WISH_ALL;
				selectWeiboByType();
			}
		});

		btn_help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 求助列表
				showType = TOPIC_HELP;
				subShowType = TOPIC_HELP_ALL;
				selectWeiboByType();
			}
		});

		btn_my_weibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TOPIC_WISH == showType) {
					subShowType = TOPIC_WISH_MINE;
				} else {
					subShowType = TOPIC_HELP_MINE;
				}

				selectWeiboByType();
			}
		});

		btn_all_weibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TOPIC_WISH == showType) {
					subShowType = TOPIC_WISH_ALL;
				} else {
					subShowType = TOPIC_HELP_ALL;
				}

				selectWeiboByType();
			}
		});
	}

	@Override
	public void ensureUi() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.WEIBOLIST);
		registerReceiver(mEvtReceiver, filter);
		selectWeiboByType();
	}

	public void selectWeiboByType() {
		if (showType == TOPIC_WISH) {
			btn_wish.setSelected(true);
			btn_help.setSelected(false);
			btn_my_weibo.setText("我的许愿");
			btn_all_weibo.setText("许愿广场");
			mCurrentWall = WISH_WALL;
		} else {
			btn_wish.setSelected(false);
			btn_help.setSelected(true);
			btn_my_weibo.setText("我的求助");
			btn_all_weibo.setText("求助广场");
			mCurrentWall = HELP_WALL;
		}

		if (subShowType == TOPIC_WISH_MINE) {
			btn_my_weibo.setSelected(true);
			btn_all_weibo.setSelected(false);
			mWeiboListView = new WeiboList(weibo_listview, mActivity, WeiboList.MY_TOPIC);
			mWeiboListView.setTopicName(WISH_WALL);
		} else if (subShowType == TOPIC_WISH_ALL) {
			btn_my_weibo.setSelected(false);
			btn_all_weibo.setSelected(true);
			mWeiboListView = new WeiboList(weibo_listview, mActivity, WeiboList.TOPIC);
			mWeiboListView.setTopicName(WISH_WALL);
		} else if (subShowType == TOPIC_HELP_MINE) {
			btn_my_weibo.setSelected(true);
			btn_all_weibo.setSelected(false);
			mWeiboListView = new WeiboList(weibo_listview, mActivity, WeiboList.MY_TOPIC);
			mWeiboListView.setTopicName(HELP_WALL);
		} else if (subShowType == TOPIC_HELP_ALL) {
			btn_my_weibo.setSelected(false);
			btn_all_weibo.setSelected(true);
			mWeiboListView = new WeiboList(weibo_listview, mActivity, WeiboList.TOPIC);
			mWeiboListView.setTopicName(HELP_WALL);
		}
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
