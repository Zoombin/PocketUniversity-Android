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
import com.xyhui.types.EventBanner;
import com.xyhui.utils.Params;

public class GroupEventList extends CListView {
	private String groupID;


	public GroupEventList(PullToRefreshListView lv, Activity activity, int type) {
		super(lv, activity);
		initListViewStart();
	}

	public GroupEventList(PullToRefreshListView lv, Activity activity,String groupID) {
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
				R.id.list_item_getmore_title, "查看更多活动");
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
				EventBanner item = (EventBanner) mListItems.get(i);

				// 打开活动首页
				Intent intent = new Intent();
				intent.setClass(mActivity, EventViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.EVENTID, item.id);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		EventBanner item = (EventBanner) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar,
				R.drawable.img_default, true);
		avatarLVP.setImgAsync(true);
		avatarLVP.setItemTag(item.cover);
		LVP.add(avatarLVP);
		// VolleyLog.d(item.is_school_event);
		if ("0".equalsIgnoreCase(item.is_school_event)) {
			LVP.add(new CListViewParam(R.id.text_flag, "", false));
		} else {
			// 显示标识
			LVP.add(new CListViewParam(R.id.text_flag, "校", true));
		}
		LVP.add(new CListViewParam(R.id.text_title, item.title, true));
		LVP.add(new CListViewParam(R.id.text_tips, item.getTips(), true));
		return LVP;
	}

	ListCallBack<ArrayList<EventBanner>> callback = new ListCallBack<ArrayList<EventBanner>>(
			GroupEventList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EventBanner>>() {
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

		new Api(callback, mActivity).getGroupEventList(PuApp.get().getToken(),
				groupID, mPerpage, page);
	}
}
