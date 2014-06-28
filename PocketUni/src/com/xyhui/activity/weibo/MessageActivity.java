package com.xyhui.activity.weibo;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class MessageActivity extends FLActivity {

	private final static int MSG_MESSAGE_ME = 0;
	private final static int MSG_MESSAGE_SYSTEM = 1;

	private int msg_type = MSG_MESSAGE_ME;

	private Button btn_back;

	private Button btn_message;
	private Button btn_systemmessage;
	private Button current_btn;

	private PullToRefreshListView listview_message_me;
	private PullToRefreshListView listview_message_system;
	private PullToRefreshListView current_listview;

	private ChatList mMessageListView;
	private SystemMsgList mSystemMsgList;

	@Override
	public void init() {
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_message);

		btn_back = (Button) findViewById(R.id.btn_back);

		btn_message = (Button) findViewById(R.id.btn_message);
		btn_systemmessage = (Button) findViewById(R.id.btn_systemmessage);

		listview_message_me = (PullToRefreshListView) findViewById(R.id.listview_message_me);
		listview_message_system = (PullToRefreshListView) findViewById(R.id.listview_message_system);
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
					msg_type = MSG_MESSAGE_ME;
					selectMsgByType();
				}
			}
		});

		btn_systemmessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (current_btn != btn_systemmessage) {
					msg_type = MSG_MESSAGE_SYSTEM;
					selectMsgByType();
				}
			}
		});

	}

	@Override
	public void ensureUi() {
		listview_message_me.setVisibility(View.GONE);
		listview_message_system.setVisibility(View.GONE);
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
		case MSG_MESSAGE_ME:
			if (mMessageListView == null) {
				mMessageListView = new ChatList(listview_message_me, mActivity);
			} else {
				mMessageListView.refreshListViewStart();
			}
			current_btn = btn_message;
			current_listview = listview_message_me;
			break;
		case MSG_MESSAGE_SYSTEM:
			if (mSystemMsgList == null) {
				mSystemMsgList = new SystemMsgList(listview_message_system,
						mActivity);
			} else {
				mSystemMsgList.refreshListViewStart();
			}
			current_btn = btn_systemmessage;
			current_listview = listview_message_system;
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
		case MSG_MESSAGE_ME:
			mMessageListView.refreshListViewStart();
			break;
		case MSG_MESSAGE_SYSTEM:
			mSystemMsgList.refreshListViewStart();
			break;
		}
	}
}
