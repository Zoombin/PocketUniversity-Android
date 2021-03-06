package com.xyhui.activity.event;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
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
import com.xyhui.types.EventNews;
import com.xyhui.utils.Params;

public class EventNewsList extends CListView {
	private String eventID;

	public EventNewsList(PullToRefreshListView lv, Activity activity, String eid) {
		super(lv, activity);
		eventID = eid;

		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_event_news);
	}

	public void refresh() {
		refreshListViewStart();
	}

	public void search(String key) {
		refreshListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;

		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多活动新闻");
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
				EventNews item = (EventNews) mListItems.get(i);

				Intent intent = new Intent();
				intent.setClass(mActivity, EventNewsViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.EVENTNEWS_ID, item.id);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		EventNews item = (EventNews) obj;

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.text_title, item.title, true));
		LVP.add(new CListViewParam(R.id.text_date, item.cTime, true));
		return LVP;
	}

	ListCallBack<ArrayList<EventNews>> callback = new ListCallBack<ArrayList<EventNews>>(
			EventNewsList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EventNews>>() {
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

		new Api(callback, mActivity).getEventNewsList(PuApp.get().getToken(), eventID, mPerpage,
				page);
	}

}
