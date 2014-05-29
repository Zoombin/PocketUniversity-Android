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
import com.xyhui.activity.weibo.WeiboEditActivity;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GroupBlogListActivity extends FLActivity {
	private Button btn_back;
	private Button btn_newblog;
	private PullToRefreshListView blog_listview;
	private GroupBlogList mGroupBlogListView;

	private String GID;
	private int isMemeber = 0;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUP_ID)) {
			GID = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);
		} else {
			finish();
			return;
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUPMEMBER)) {
			isMemeber = getIntent().getIntExtra(Params.INTENT_EXTRA.GROUPMEMBER, 0);
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_blog_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_newblog = (Button) findViewById(R.id.btn_newblog);
		blog_listview = (PullToRefreshListView) findViewById(R.id.blog_listview);
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
		btn_newblog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 新建文章
				Intent intent = new Intent();
				intent.setClass(mActivity, WeiboEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, GID);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT, Params.INTENT_VALUE.WEIBO_BLOG);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public void ensureUi() {

		if (isMemeber != 1) {
			btn_newblog.setVisibility(View.GONE);
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.BLOGLIST);
		registerReceiver(mEvtReceiver, filter);
		mGroupBlogListView = new GroupBlogList(blog_listview, mActivity, GID);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Params.INTENT_ACTION.BLOGLIST)) {
				if (mGroupBlogListView != null) {
					mGroupBlogListView.refresh();
				}
			}
		}
	};
}
