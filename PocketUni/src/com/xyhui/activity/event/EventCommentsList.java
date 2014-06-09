package com.xyhui.activity.event;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.weibo.UserHomePageActivity;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Weibo;
import com.xyhui.types.WeiboComment;
import com.xyhui.utils.Params;

public class EventCommentsList extends CListView {
	private String eventID;

	public EventCommentsList(PullToRefreshListView lv, Activity activity,
			String eid) {
		super(lv, activity);
		eventID = eid;
		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_event_comments);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore,
				R.id.list_item_getmore_title, "查看更多评论");
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

		final EventComment comment = (EventComment) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar,
				R.drawable.avatar00, true);
		avatarLVP.setImgAsync(true);
		avatarLVP.setItemTag(comment.face);
		avatarLVP.setOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		LVP.add(avatarLVP);

		LVP.add(new CListViewParam(R.id.text_nickname, comment.uname, true));
		LVP.add(new CListViewParam(R.id.text_content, comment.comment, true));
		LVP.add(new CListViewParam(R.id.text_datefrom, comment.ctime, true));
		return LVP;
	}

	ListCallBack<ArrayList<EventComment>> callback = new ListCallBack<ArrayList<EventComment>>(
			EventCommentsList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EventComment>>() {
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
		new Api(callback, mActivity).getEventCommentsList(PuApp.get()
				.getToken(), eventID, mPerpage, page);
	}

}
