package com.xyhui.activity.group;

import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
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
import com.xyhui.types.Dynamic;

public class GroupDynamicList extends CListView {
	private String groupId;

	public GroupDynamicList(PullToRefreshListView lv, Activity activity,
			String groupId) {
		// 在父类将执行 initListItemResource();ensureUi();
		super(lv, activity);
		this.groupId = groupId;
		initListViewStart();
	}


	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.list_item_dynamic);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore,
				R.id.list_item_getmore_title, "查看更多动态");
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

		final Dynamic dynamic = (Dynamic) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.text_tips, dynamic.content, true));
		LVP.add(new CListViewParam(R.id.text_time, dynamic.ctime, true));
		return LVP;
	}

	ListCallBack<ArrayList<Dynamic>> callback = new ListCallBack<ArrayList<Dynamic>>(
			GroupDynamicList.this) {

		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Dynamic>>() {
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
		new Api(callback, mActivity).feedList(PuApp.get().getToken(), groupId, mPerpage, page);
	}
}
