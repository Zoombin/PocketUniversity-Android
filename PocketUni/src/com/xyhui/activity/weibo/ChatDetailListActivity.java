package com.xyhui.activity.weibo;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class ChatDetailListActivity extends FLActivity {
	private final String TITLE = "新私信";

	private Button btn_back;
	private TextView navbar_TitleText;
	private EditText edit_message;
	private Button btn_send;

	private ListView message_item_listview;
	private ChatDetailList mMessageItemListView;

	private String mTalkerUid;
	private String mTalkerName;
	private String mContent;

	// private Timer mTimer;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.MESSAGE_UID)) {
			mTalkerUid = getIntent().getStringExtra(Params.INTENT_EXTRA.MESSAGE_UID);
		} else {
			finish();
			return;
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.USERNAME)) {
			mTalkerName = getIntent().getStringExtra(Params.INTENT_EXTRA.USERNAME);
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_message_list);

		navbar_TitleText = (TextView) findViewById(R.id.navbar_TitleText);
		btn_back = (Button) findViewById(R.id.btn_back);
		message_item_listview = (ListView) findViewById(R.id.message_item_listview);
		edit_message = (EditText) findViewById(R.id.edit_message);
		btn_send = (Button) findViewById(R.id.btn_send);
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

		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_send.setEnabled(false);
				// 发送私信
				mContent = edit_message.getText().toString();

				if (mContent.length() > 0 && null != mTalkerUid) {
					new Api(callback, mActivity).sendmessage(PuApp.get().getToken(), mTalkerUid,
							mContent);
				}
			}
		});
	}

	@Override
	public void ensureUi() {
		if (null != mTalkerUid) {
			mMessageItemListView = new ChatDetailList(message_item_listview, mActivity, mTalkerUid);
		}

		navbar_TitleText.setText("与" + mTalkerName + "的对话");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		VolleyLog.d("ChatDetailListActivity");
		setIntent(intent);
		mActivity = this;
		init();
		linkUiVar();
		bindListener();
		ensureUi();
	}

	@Override
	protected void onResume() {
		super.onResume();
		edit_message.setText(new PrefUtil().getPreference(Params.LOCAL.EDIT_MESSAGE));

		// mTimer = new Timer();
		// mTimer.scheduleAtFixedRate(new TimerTask() {
		// @Override
		// public void run() {
		// btn_back.post(new Runnable() {
		// @Override
		// public void run() {
		// if (mMessageItemListView.actionType == CListView.IDLE)
		// mMessageItemListView.refreshListViewStart();
		// }
		// });
		// }
		// }, 4000, 4000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		new PrefUtil().setPreference(Params.LOCAL.EDIT_MESSAGE, edit_message.getText().toString());
		// mTimer.cancel();
	}

	private CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			edit_message.setText("");
			btn_send.setEnabled(true);

			if (response.contains("false")) {
				NotificationsUtil.ToastBottomMsg(mActivity, "发送失败");
			} else if (response.equals("0")) {
				NotificationsUtil.ToastBottomMsg(mActivity, "请不要输入特殊的表情字符!");
			} else {
				if (mMessageItemListView != null) {
					mMessageItemListView.refreshListViewStart();
				} else {
					mMessageItemListView = new ChatDetailList(message_item_listview, mActivity,
							mTalkerUid);
				}

				JSONObject extras = new JSONObject();
				String extra = null;

				try {
					extras.put("type", Params.JPush.TYPE_PRIVETE_MSG);
					extras.put("uid", new PrefUtil().getPreference(Params.LOCAL.UID));
					extras.put("uname", new PrefUtil().getPreference(Params.LOCAL.NICKNAME));
					extra = extras.toString();
					VolleyLog.d(extra);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				new Api(null, mActivity).jsend(PuApp.get().getToken(), mTalkerUid, mContent,
						TITLE, extra);
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			btn_send.setEnabled(true);
		}
	};
}
