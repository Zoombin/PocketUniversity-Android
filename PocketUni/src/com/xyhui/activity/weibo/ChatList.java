package com.xyhui.activity.weibo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Message;
import com.xyhui.utils.Params;
import com.xyhui.utils.SpanUtils;

public class ChatList extends CListView {
	public ChatList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_message);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多私信");
		super.ensureUi();

		super.setGetMoreClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getmoreListViewStart();
			}
		});

		super.setItemOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = (Integer) v.getTag();
				Message item = (Message) mListItems.get(i);

				// 打开私信回复
				Intent intent = new Intent(mActivity, ChatDetailListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.MESSAGE_UID, item.talk_uid);
				intent.putExtra(Params.INTENT_EXTRA.USERNAME, item.talk_uname);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final Message item = (Message) obj;

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.avatar00, true);
		if (!TextUtils.isEmpty(item.from_face)) {
			avatarLVP.setImgAsync(true);
			avatarLVP.setItemTag(item.from_face);
		}
		avatarLVP.setOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开个人主页
				Intent intent = new Intent();
				intent.setClass(mActivity, UserHomePageActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.USER_ID, item.from_uid + "");
				mActivity.startActivity(intent);
			}
		});
		
		LVP.add(avatarLVP);

		if (item.isNew.equals("0")) {
			LVP.add(new CListViewParam(R.id.img_new_message, R.drawable.icon_new_message, false));
		} else {
			LVP.add(new CListViewParam(R.id.img_new_message, R.drawable.icon_new_message, true));
		}

		String chat_name;

		if (null != item.talk_uid && item.from_uid == Integer.valueOf(item.talk_uid)) {
			chat_name = item.talk_uname + " 对 你 说：";
		} else {
			chat_name = String.format("你 对 %s 说", item.talk_uname);
		}

		LVP.add(new CListViewParam(R.id.text_nickname, chat_name, true));
		LVP.add(new CListViewParam(R.id.text_date, item.ctime, true));
		
		SpannableString spanStr = SpanUtils.txtToImg(mActivity, item.content);
		CListViewParam messageLVP = new CListViewParam(R.id.text_message, spanStr, true);
		messageLVP.setOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 打开私信回复
				Intent intent = new Intent(mActivity, ChatDetailListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.MESSAGE_UID, item.talk_uid);
				intent.putExtra(Params.INTENT_EXTRA.USERNAME, item.talk_uname);
				mActivity.startActivity(intent);
			}
		});
		LVP.add(messageLVP);

		return LVP;
	}

	ListCallBack<ArrayList<Message>> callback = new ListCallBack<ArrayList<Message>>(ChatList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Message>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
			}
		}
	};

	@Override
	public void asyncData() {
		super.asyncData();
		new Api(callback, mActivity).message(PuApp.get().getToken(), mPerpage, page);
	}
}
