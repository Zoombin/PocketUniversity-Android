package com.xyhui.activity.group;

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
import com.xyhui.types.GroupTopic;
import com.xyhui.types.GroupTopics;
import com.xyhui.utils.Params;

public class GroupBlogList extends CListView {
	private String mGroupId;

	public GroupBlogList(PullToRefreshListView lv, Activity activity, String goupid) {
		super(lv, activity);

		mGroupId = goupid;

		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_group_blog);
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
				"查看更多帖子");
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
				GroupTopic item = (GroupTopic) mListItems.get(i);

				Intent intent = new Intent();
				intent.setClass(mActivity, GroupBlogViewListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.BLOG_ID, item.id);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, mGroupId);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		GroupTopic item = (GroupTopic) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.text_title, item.title, true));
		LVP.add(new CListViewParam(R.id.text_info, item.getDesc(), true));
		return LVP;
	}

	ListCallBack<GroupTopics> callback = new ListCallBack<GroupTopics>(GroupBlogList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<GroupTopics>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && mT.data != null && !mT.data.isEmpty()) {
				for (int i = 0; i < mT.data.size(); i++) {
					mListItems.add(mT.data.get(i));
				}
			}
		}
	};

	@Override
	public void asyncData() {
		super.asyncData();
		new Api(callback, mActivity).grouptopic(PuApp.get().getToken(), mGroupId, mPerpage, page);
	}

}
