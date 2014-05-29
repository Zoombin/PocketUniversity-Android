package com.xyhui.activity.weibo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.WeiboTopic;
import com.xyhui.utils.Params;

public class WeiboTopicList extends CListView {
	public WeiboTopicList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.list_item_nav_map);
	}

	@Override
	public void ensureUi() {
		mPerpage = 1000;

		super.ensureUi();

		super.setItemOnclickLinstener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = (Integer) v.getTag();

				WeiboTopic item = (WeiboTopic) mListItems.get(i);

				Intent intent = new Intent();
				intent.setClass(mActivity, WeiboTopicViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_TOPIC_ID, item.name);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		WeiboTopic item = (WeiboTopic) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.text_title, item.getTopic(), true));
		return LVP;
	}

	ListCallBack<ArrayList<WeiboTopic>> callback = new ListCallBack<ArrayList<WeiboTopic>>(
			WeiboTopicList.this) {

		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<WeiboTopic>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (null != mT && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
			}
		}
	};

	@Override
	public void asyncData() {
		((CActivity) mActivity).showProgress();
		new Api(callback, mActivity).recommendTopic(PuApp.get().getToken());
	}
}
