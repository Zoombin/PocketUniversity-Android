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
import com.xyhui.types.GroupDoc;
import com.xyhui.types.GroupDocs;
import com.xyhui.utils.Params;

public class GroupDocList extends CListView {
	private String mGroupId;

	public GroupDocList(PullToRefreshListView lv, Activity activity, String goupid) {
		super(lv, activity);

		mGroupId = goupid;

		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_group_doc);
	}

	public void refresh() {
		refreshListViewStart();
	}

	public void search(String key) {
		refreshListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 1000;
		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多共享");
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
				GroupDoc item = (GroupDoc) mListItems.get(i);
				// 打开部落主页
				Intent intent = new Intent(mActivity, GroupDocViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.DOC, item);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		GroupDoc item = (GroupDoc) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.text_title, item.name, true));
		LVP.add(new CListViewParam(R.id.text_info, item.getDesc(), true));
		return LVP;
	}

	ListCallBack<GroupDocs> callback = new ListCallBack<GroupDocs>(GroupDocList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<GroupDocs>() {
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
		new Api(callback, mActivity).groupdoc(PuApp.get().getToken(), mGroupId);
	}

}
