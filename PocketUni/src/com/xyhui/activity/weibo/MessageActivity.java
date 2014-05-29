package com.xyhui.activity.weibo;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class MessageActivity extends FLActivity {
	/**
	 * 私信
	 */
	public static final int MSG_MESSAGE = 1;

	/**
	 * 转发
	 */
	public static final int MSG_TRANSPOND = 2;

	/**
	 * 评论
	 */
	public static final int MSG_COMMENTS = 3;

	/**
	 * 赞
	 */
	public static final int MSG_ZAN = 4;

	private Button btn_back;

	private Button btn_message;
	private Button btn_transpond;
	private Button btn_comments;
	private Button btn_zan;
	private Button current_btn;

	private PullToRefreshListView message_listview;
	private PullToRefreshListView transpond_listview;
	private PullToRefreshListView comments_listview;
	private PullToRefreshListView zan_listview;
	private PullToRefreshListView current_listview;

	private ChatList mMessageListView;
	private WeiboList mWeiboListView;
	private CommentList mCommentsListView;
	private WeiboList mZanListView;

	private int msg_type = MSG_MESSAGE;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.MSG_TYPE)) {
			msg_type = getIntent().getIntExtra(Params.INTENT_EXTRA.MSG_TYPE, MSG_MESSAGE);
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_message);

		btn_back = (Button) findViewById(R.id.btn_back);

		btn_message = (Button) findViewById(R.id.btn_message);
		btn_transpond = (Button) findViewById(R.id.btn_atme);
		btn_comments = (Button) findViewById(R.id.btn_comments);
		btn_zan = (Button) findViewById(R.id.btn_zan);

		message_listview = (PullToRefreshListView) findViewById(R.id.message_listview);
		transpond_listview = (PullToRefreshListView) findViewById(R.id.atme_listview);
		comments_listview = (PullToRefreshListView) findViewById(R.id.comments_listview);
		zan_listview = (PullToRefreshListView) findViewById(R.id.zan_listview);
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

		btn_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (current_btn != btn_message) {
					msg_type = MSG_MESSAGE;
					selectMsgByType();
				}
			}
		});

		btn_transpond.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (current_btn != btn_transpond) {
					msg_type = MSG_TRANSPOND;
					selectMsgByType();
				}
			}
		});

		btn_comments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (current_btn != btn_comments) {
					msg_type = MSG_COMMENTS;
					selectMsgByType();
				}
			}
		});

		btn_zan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (current_btn != btn_zan) {
					msg_type = MSG_ZAN;
					selectMsgByType();
				}
			}
		});
	}

	@Override
	public void ensureUi() {
		message_listview.setVisibility(View.GONE);
		transpond_listview.setVisibility(View.GONE);
		comments_listview.setVisibility(View.GONE);
		zan_listview.setVisibility(View.GONE);
		selectMsgByType();
	}

	public void selectMsgByType() {

		if (current_btn != null) {
			current_btn.setSelected(false);
		}

		if (current_listview != null) {
			current_listview.setVisibility(View.GONE);
		}

		switch (msg_type) {
		case MSG_MESSAGE:
			if (mMessageListView == null) {
				mMessageListView = new ChatList(message_listview, mActivity);
			} else {
				mMessageListView.refreshListViewStart();
			}
			current_btn = btn_message;
			current_listview = message_listview;
			break;
		case MSG_TRANSPOND:
			if (mWeiboListView == null) {
				mWeiboListView = new WeiboList(transpond_listview, mActivity, WeiboList.METIONS);
			} else {
				mWeiboListView.refreshListViewStart();
			}
			current_btn = btn_transpond;
			current_listview = transpond_listview;
			break;
		case MSG_COMMENTS:
			if (mCommentsListView == null) {
				mCommentsListView = new CommentList(comments_listview, mActivity);
			} else {
				mCommentsListView.refreshListViewStart();
			}
			current_btn = btn_comments;
			current_listview = comments_listview;
			break;
		case MSG_ZAN:
			if (mZanListView == null) {
				mZanListView = new WeiboList(zan_listview, mActivity, WeiboList.ZAN);
			} else {
				mZanListView.refreshListViewStart();
			}
			current_btn = btn_zan;
			current_listview = zan_listview;
			break;
		default:
			break;
		}

		current_btn.setSelected(true);
		current_listview.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		switch (msg_type) {
		case MSG_MESSAGE:
			mMessageListView.refreshListViewStart();
			break;
		case MSG_TRANSPOND:
			mWeiboListView.refreshListViewStart();
			break;
		case MSG_COMMENTS:
			mCommentsListView.refreshListViewStart();
			break;
		case MSG_ZAN:
			mZanListView.refreshListViewStart();
			break;
		}
	}
}
