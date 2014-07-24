package com.xyhui.activity.group;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.Client;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Group;
import com.xyhui.types.Groups;
import com.xyhui.utils.Params;
import com.xyhui.utils.SpanUtils;

public class GroupList extends CListView {

	public static final int GROUP_HOT = 1;
	public static final int GROUP_MY = 2;
	public static final int GROUP_SEARCH = 3;
	public static final int GROUP_STAR = 4;

	private int group_type = GROUP_HOT;

	private String[] ids = new String[] { "0", "0", "0", "0" };

	private String groupNameKeyWord;

	public GroupList(PullToRefreshListView lv, Activity activity, int type) {
		super(lv, activity);

		group_type = type;

		if (GROUP_SEARCH != group_type) {
			initListViewStart();
		}
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_group);
	}

	public void refresh() {
		refreshListViewStart();
	}

	public void search(String key) {
		groupNameKeyWord = key;
		initListViewStart();
	}

	public void getHotGroup(String[] ids) {
		this.ids = ids;
		initListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;

		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多部落");
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

				Group item = (Group) mListItems.get(i);

				// 打开部落主页
				Intent intent = new Intent();
				intent.setClass(mActivity, GroupViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, item.id);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		Group item = (Group) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.group_avatar,
				true);
		if (!TextUtils.isEmpty(item.logo) && !"default.gif".equalsIgnoreCase(item.logo)) {

			String logo = Client.UPLOAD_URL + item.logo;
			avatarLVP.setImgAsync(true);
			avatarLVP.setItemTag(logo);

		}

		LVP.add(avatarLVP);

		LVP.add(new CListViewParam(R.id.text_nickname, item.name, true));
		LVP.add(new CListViewParam(R.id.text_params, "成员：" + item.membercount + "\n创建："
				+ SpanUtils.getTimeString(item.ctime) + "\n类型：" + display(item.schoolname) + " "
				+ display(item.cname0) + " " + display(item.cname1), true));
		return LVP;
	}

	public String display(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		return str;
	}

	ListCallBack<Groups> callback = new ListCallBack<Groups>(GroupList.this) {

		@Override
		public void setType() {
			myType = new TypeToken<Groups>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && mT.data != null && !mT.data.isEmpty()) {
				for (int i = 0; i < mT.data.size(); i++) {
					mListItems.add(mT.data.get(i));
				}
			} else {
				NotificationsUtil.ToastTopMsg(PuApp.get().getBaseContext(), "没有搜索到相关信息");
				return;
			}
		}
	};

	@Override
	public void asyncData() {
		super.asyncData();

		if (GROUP_HOT == group_type) {
			new Api(callback, mActivity).allgroup(PuApp.get().getToken(), ids, mPerpage, page);
		} else if (GROUP_STAR == group_type) {
				new Api(callback, mActivity).stargroup(PuApp.get().getToken(), ids, mPerpage, page);
		} else if (GROUP_MY == group_type) {
			new Api(callback, mActivity).mygroup(PuApp.get().getToken(), mPerpage, page);
		} else if (GROUP_SEARCH == group_type) {
			new Api(callback, mActivity).groupsearch(PuApp.get().getToken(), groupNameKeyWord,
					mPerpage, page);
		}
	}
}
