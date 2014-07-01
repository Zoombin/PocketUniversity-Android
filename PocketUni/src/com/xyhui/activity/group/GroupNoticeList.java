package com.xyhui.activity.group;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.event.EventViewActivity;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Notice;
import com.xyhui.utils.Params;

public class GroupNoticeList extends CListView {
	private String groupID;

	public GroupNoticeList(PullToRefreshListView lv, Activity activity, int type) {
		super(lv, activity);
		initListViewStart();
	}

	public GroupNoticeList(PullToRefreshListView lv, Activity activity,
			String groupID) {
		super(lv, activity);
		this.groupID = groupID;

		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_event);
	}

	public void refresh(String groupID) {
		this.groupID = groupID;
		refreshListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore,
				R.id.list_item_getmore_title, "查看更多公告");
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

		Notice item = (Notice) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		LVP.add(new CListViewParam(R.id.img_avatar, R.drawable.img_default,
				false));
		LVP.add(new CListViewParam(R.id.text_flag, "", false));
		LVP.add(new CListViewParam(R.id.text_title, "公告内容:" + item.content,
				true));
		LVP.add(new CListViewParam(R.id.text_tips, "发布者:" + item.realname
				+ "\n发布时间:" + item.ctime, true));
		return LVP;
	}

	ListCallBack<ArrayList<Notice>> callback = new ListCallBack<ArrayList<Notice>>(
			GroupNoticeList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Notice>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
			} else {
				NotificationsUtil.ToastTopMsg(PuApp.get().getBaseContext(),
						"没有搜索到相关信息");
				return;
			}
		}
	};

	@Override
	public void asyncData() {
		super.asyncData();

		new Api(callback, mActivity).getGroupNoticeList(PuApp.get().getToken(),
				groupID, mPerpage, page);
	}
}
