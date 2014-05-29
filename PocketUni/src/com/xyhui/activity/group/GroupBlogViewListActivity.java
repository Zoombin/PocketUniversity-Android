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

public class GroupBlogViewListActivity extends FLActivity {
	private Button btn_back;
	private PullToRefreshListView blog_view_listview;
	private GroupBlogViewList mGroupBlogViewListView;

	private Button tabbtn_refresh;
	private Button tabbtn_reply;

	private String tid;

	private String group_id;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.BLOG_ID)) {
			tid = getIntent().getStringExtra(Params.INTENT_EXTRA.BLOG_ID);
		} else {
			finish();
			return;
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUP_ID)) {
			group_id = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);
		} else {
			finish();
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_blog_view);

		btn_back = (Button) findViewById(R.id.btn_back);

		blog_view_listview = (PullToRefreshListView) findViewById(R.id.blog_view_listview);

		tabbtn_refresh = (Button) findViewById(R.id.tabbtn_refresh);
		tabbtn_reply = (Button) findViewById(R.id.tabbtn_reply);
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
		tabbtn_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 刷新文章
				if (mGroupBlogViewListView != null) {
					mGroupBlogViewListView.refresh();
				}
			}
		});
		tabbtn_reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 评论文章
				Intent intent = new Intent();
				intent.setClass(mActivity, WeiboEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT,
						Params.INTENT_VALUE.WEIBO_BLOGREPLY);
				intent.putExtra(Params.INTENT_EXTRA.BLOG_ID, tid);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, group_id);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public void ensureUi() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.BLOGVIEW);
		registerReceiver(mEvtReceiver, filter);
		mGroupBlogViewListView = new GroupBlogViewList(blog_view_listview, mActivity, tid);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Params.INTENT_ACTION.BLOGVIEW)) {
				if (mGroupBlogViewListView != null) {
					mGroupBlogViewListView.refresh();
				}
			}
		}
	};
}
