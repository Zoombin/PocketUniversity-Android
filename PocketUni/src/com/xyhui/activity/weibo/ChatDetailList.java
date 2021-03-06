package com.xyhui.activity.weibo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Message2;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;

public class ChatDetailList extends CListView {
	private String mTalkerUId;
	private String mUid;

	public ChatDetailList(ListView lv, Activity activity, String talkerUid) {
		super(lv, activity);
		mTalkerUId = talkerUid;
		mUid = new PrefUtil().getPreference(Params.LOCAL.UID);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.list_item_message_item);
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
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		final Message2 message = (Message2) obj;

		boolean isMyMessage = mUid.equalsIgnoreCase(message.from_uid);

		if (isMyMessage) {
			LVP.add(new CListViewParam(R.id.left_layout, null, false));
			LVP.add(new CListViewParam(R.id.right_layout, null, true));
			CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar_right,
					R.drawable.avatar00, true);

			if (!TextUtils.isEmpty(message.from_face)) {
				avatarLVP.setImgAsync(true);
				avatarLVP.setItemTag(message.from_face);
			}
			avatarLVP.setOnclickLinstener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 打开个人主页
					Intent intent = new Intent();
					intent.setClass(mActivity, UserHomePageActivity.class);
					intent.putExtra(Params.INTENT_EXTRA.USER_ID, mUid);
					mActivity.startActivity(intent);
				}
			});
			LVP.add(avatarLVP);
			LVP.add(new CListViewParam(R.id.text_message_right, message.from_uname + ":\n"
					+ message.content, true));
			LVP.add(new CListViewParam(R.id.text_date_right, message.getHumanReadableTime(), true));

		} else {
			LVP.add(new CListViewParam(R.id.left_layout, null, true));
			LVP.add(new CListViewParam(R.id.right_layout, null, false));
			CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar_left,
					R.drawable.avatar00, true);
			if (!TextUtils.isEmpty(message.from_face)) {
				avatarLVP.setImgAsync(true);
				avatarLVP.setItemTag(message.from_face);
			}
			avatarLVP.setOnclickLinstener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 打开个人主页
					Intent intent = new Intent();
					intent.setClass(mActivity, UserHomePageActivity.class);
					intent.putExtra(Params.INTENT_EXTRA.USER_ID, message.from_uid);
					mActivity.startActivity(intent);
				}
			});
			LVP.add(avatarLVP);
			LVP.add(new CListViewParam(R.id.text_message_left, message.from_uname + ":\n"
					+ message.content, true));
			LVP.add(new CListViewParam(R.id.text_date_left, message.getHumanReadableTime(), true));
		}
		return LVP;
	}

	ListCallBack<ArrayList<Message2>> callback = new ListCallBack<ArrayList<Message2>>(
			ChatDetailList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Message2>>() {
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
	public void refreshListViewFinish() {
		for (int i = 0; i < mListItems.size(); i++) {
			mDateList.add(matchListItem(mListItems.get(i), i));
		}

		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void asyncData() {
		super.asyncData();
		new Api(callback, mActivity).showByUid(PuApp.get().getToken(), mTalkerUId, page, mPerpage);
	}
}
