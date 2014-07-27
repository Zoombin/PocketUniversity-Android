package com.xyhui.activity.event;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.weibo.UserHomePageActivity;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.EventBanner.EUser;
import com.xyhui.types.EventUser;
import com.xyhui.utils.Params;

public class EventActivityUserList extends CListView {
	private String eventID;

	public EventActivityUserList(PullToRefreshListView lv, Activity activity, String eid) {
		super(lv, activity);
		eventID = eid;
		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_eventuser);
	}

	public void refresh() {
		refreshListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多");

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
				EUser euser = (EUser) mListItems.get(i);

				// 打开选手详情
				Intent intent = new Intent();
				intent.setClass(mActivity, UserHomePageActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.USER_ID, euser.uid);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final EUser item = (EUser) obj;

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam imgAvatarLVP = new CListViewParam(R.id.img_avatar_joiner, R.drawable.img_default,
				true);
		imgAvatarLVP.setImgAsync(true);
		imgAvatarLVP.setItemTag(item.uface);
		LVP.add(imgAvatarLVP);

		LVP.add(new CListViewParam(R.id.text_nickname_joiner, item.realname, true));
		return LVP;
	}

	ListCallBack<ArrayList<EUser>> callback = new ListCallBack<ArrayList<EUser>>(
			EventActivityUserList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EUser>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
			}
		}
	};

	@Override
	public void asyncData() {
		super.asyncData();
		new Api(callback, mActivity).getEUserList(PuApp.get().getToken(), eventID,
				mPerpage, page);
	}
}
